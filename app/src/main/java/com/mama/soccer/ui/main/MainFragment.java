package com.mama.soccer.ui.main;

import android.os.Bundle;
import android.support.v17.leanback.app.BrowseSupportFragment;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.OnItemViewSelectedListener;
import android.support.v17.leanback.widget.PlaybackControlsRowPresenter;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;
import android.util.Log;
import android.util.SparseArray;

import com.mama.soccer.App;
import com.mama.soccer.data.models.CardData;
import com.mama.soccer.ui.base.GlideBackgroundManager;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainFragment extends BrowseSupportFragment implements OnItemViewSelectedListener {

    private GlideBackgroundManager mBackgroundManager;

    SparseArray<CardTopic> mTopics;
    ExecutorService executorService;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        App.instance().appComponent().inject(this);

        // The background manager allows us to manage a dimmed background that does not interfere with the rows
        // It is the preferred way to set the background of a fragment
        mBackgroundManager = new GlideBackgroundManager(getActivity());

        executorService = Executors.newFixedThreadPool(10);
        // The brand color will be used as the background for the Headers fragment
//        setBrandColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
//        setHeadersState(HEADERS_ENABLED);
//        setHeadersTransitionOnBackEnabled(true);

        createDataRows();
        createRows();
//        prepareEntranceTransition();
        initialize();

        // trigger content fetching 2 seconds after activity start
        Observable.interval(500, TimeUnit.MILLISECONDS)
                  .take(1)
                  .subscribeOn(Schedulers.io())
                  .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        fetchCards();
                    }
                  });
    }

    /**
     * Creates the data rows objects
     */
    private void createDataRows() {
        mTopics = new SparseArray<>();
        CardPresenter cardPresenter = new CardPresenter(getContext());
        List<CardTopic> cardTopics = CardTopic.getCardTopics(cardPresenter);
        for(CardTopic cardTopic: cardTopics) {
            mTopics.put(cardTopic.getId(), cardTopic);
        }
    }

    /**
     * Creates the rows and sets up the adapter of the fragment
     */
    private void createRows() {
        // Creates the RowsAdapter for the Fragment
        // The ListRowPresenter tells to render ListRow objects
        ArrayObjectAdapter rowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());
        for (int i = 0; i < mTopics.size(); i++) {
            CardTopic cardTopic = mTopics.get(i);

            HeaderItem headerItem = new HeaderItem(cardTopic.getId(), cardTopic.getTitle());
            ListRow listRow = new ListRow(headerItem, cardTopic.getAdapter());

            rowsAdapter.add(listRow);
        }
        // Sets this fragments Adapter.
        // The setAdapter method is defined in the BrowseFragment of the Leanback Library
        setAdapter(rowsAdapter);
        setOnItemViewSelectedListener(this);
    }

    private void initialize() {
        for(int i=0; i<mTopics.size(); i++) {
            final int index = i;

            mTopics.get(i).getPublishSubject()
                    .onBackpressureBuffer()
                    .distinct()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(cardData -> {
                        bindMovieResponse(cardData, index);
                        startEntranceTransition();
                    }, e -> {
                        Log.e("MainFragment", "Error updating UI with new event links", e);
                    });
        }
    }

    private void bindMovieResponse(CardData cardData, int id) {
        CardTopic cardTopic = mTopics.get(id);
//        cardRow.setPage(cardRow.getPage() + 1);
        cardTopic.getAdapter().add(cardData);
    }

    @Override
    public void onItemSelected(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {
        Log.i("MainFragment", "Reached...item selected");
        //mBackgroundManager.loadImage("https://cdn.footystats.org/img/competitions/italy-serie-a.png");
    }

    public void fetchTestCards() {
        for(int i=0; i<mTopics.size(); i++) {
            CardData cardData = CardData.builder().title("testTitle")
                    .tileImage("https://talksport.com/wp-content/uploads/sites/5/2019/10/NINTCHDBPICT000535500603.jpg")
                    .ctaHttpUrl("testCta")
                    .build();
            mTopics.get(i).getPublishSubject().onNext(cardData);
            mTopics.get(i).getPublishSubject().onNext(cardData);
            mTopics.get(i).getPublishSubject().onNext(cardData);
            mTopics.get(i).getPublishSubject().onNext(cardData);
            mTopics.get(i).getPublishSubject().onNext(cardData);
            mTopics.get(i).getPublishSubject().onNext(cardData);
        }
    }

    public void fetchCards() {
        try {
            Document doc = Jsoup.connect("https://mamahd.live/upcoming/").get();
            Elements links = doc.select("a[href]");

            for(Element link: links) {
                String linkText = link.attr("href");
                for(int i=0; i<mTopics.size(); i++) {
                    if(linkText.contains("https://mamahd.live/" + mTopics.get(i).getUrlSuffix())) {
                        executorService.submit(new EventLinkParserTask(linkText, mTopics.get(i)));
                        break;
                    }
                }
            }
        } catch(IOException e) {
            Log.e("MainFragment", "Failed to call main url", e);
        }
    }
}
