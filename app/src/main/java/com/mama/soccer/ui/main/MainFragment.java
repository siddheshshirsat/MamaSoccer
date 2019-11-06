package com.mama.soccer.ui.main;

import android.os.Bundle;
import android.support.v17.leanback.app.BrowseSupportFragment;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.OnItemViewSelectedListener;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.SparseArray;

import com.mama.soccer.App;
import com.mama.soccer.R;
import com.mama.soccer.ui.base.GlideBackgroundManager;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import timber.log.Timber;

/**
 * Created by <a href="mailto:marcus@gabilheri.com">Marcus Gabilheri</a>
 *
 * @author Marcus Gabilheri
 * @version 1.0
 * @since 10/8/16.
 */
public class MainFragment extends BrowseSupportFragment implements OnItemViewSelectedListener {

    Observable<Long> testEventGenerator;
    private GlideBackgroundManager mBackgroundManager;

    //    private final List<String> TOPIC_NAMES = Arrays.asList("serie-a", "champions-league", "europa-league", "nba");
    private final List<String> TOPIC_NAMES = Arrays.asList("nba-miami");
    private List<PublishSubject<String>> topics;

    SparseArray<CardRow> mRows;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        App.instance().appComponent().inject(this);

        // The background manager allows us to manage a dimmed background that does not interfere with the rows
        // It is the preferred way to set the background of a fragment
        mBackgroundManager = new GlideBackgroundManager(getActivity());

        // The brand color will be used as the background for the Headers fragment
        setBrandColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
        setHeadersState(HEADERS_ENABLED);
        setHeadersTransitionOnBackEnabled(true);

        // The TMDB logo on the right corner. It is necessary to show based on their API usage policy
        // setBadgeDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.powered_by));

        createDataRows();
        createRows();
        prepareEntranceTransition();
        fetchContent();

        testEventGenerator = Observable.interval(2000, TimeUnit.MILLISECONDS);
        testEventGenerator
                .take(1)
                .subscribeOn(Schedulers.io())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        // serieMatches.onNext("serie-a-2");
                        // plMatches.onNext("serie-a-2");
                        testHttp();
                    }
                });
    }

    /**
     * Creates the data rows objects
     */
    private void createDataRows() {
        mRows = new SparseArray<>();
        CardPresenter cardPresenter = new CardPresenter(getContext());
        for(int i=0;i<TOPIC_NAMES.size(); i++) {
            mRows.put(i, new CardRow()
                    .setId(i)
                    .setAdapter(new ArrayObjectAdapter(cardPresenter))
                    .setTitle(TOPIC_NAMES.get(i))
                    .setPage(1)
            );
        }
    }

    /**
     * Creates the rows and sets up the adapter of the fragment
     */
    private void createRows() {
        // Creates the RowsAdapter for the Fragment
        // The ListRowPresenter tells to render ListRow objects
        ArrayObjectAdapter rowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());
        for (int i = 0; i < mRows.size(); i++) {
            CardRow cardRow = mRows.get(i);
            // Adds a new ListRow to the adapter. Each row will contain a collection of Movies
            // That will be rendered using the MoviePresenter
            HeaderItem headerItem = new HeaderItem(cardRow.getId(), cardRow.getTitle());
            ListRow listRow = new ListRow(headerItem, cardRow.getAdapter());
            rowsAdapter.add(listRow);
        }
        // Sets this fragments Adapter.
        // The setAdapter method is defined in the BrowseFragment of the Leanback Library
        setAdapter(rowsAdapter);
        setOnItemViewSelectedListener(this);
    }

    private void fetchContent() {
        topics = new ArrayList<>();
        for(int i=0; i<TOPIC_NAMES.size(); i++) {
            final int index = i;
            PublishSubject<String> currentTopic = PublishSubject.create();
            topics.add(currentTopic);
            currentTopic
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(s -> {
                        bindMovieResponse(s, index);
                        startEntranceTransition();
                    }, e -> {
                        Timber.e(e, "Error fetching now playing movies: %s", e.getMessage());
                    });

        }
    }

    /**
     * Binds a movie response to it's adapter
     * @param response
     *      The response from TMDB API
     * @param id
     *      The ID / position of the row
     */
    private void bindMovieResponse(String response, int id) {
        CardRow cardRow = mRows.get(id);
        cardRow.setPage(cardRow.getPage() + 1);
        cardRow.getAdapter().add(response);
    }

    @Override
    public void onItemSelected(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {
        Log.i("MainFragment", "Reached...item selected");
        mBackgroundManager.loadImage("https://cdn.footystats.org/img/competitions/italy-serie-a.png");
//        // Check if the item is a movie
//        if (item instanceof Movie) {
//            Movie movie = (Movie) item;
//            // Check if the movie has a backdrop
//            if(movie.getBackdropPath() != null) {
//                mBackgroundManager.loadImage(HttpClientModule.BACKDROP_URL + movie.getBackdropPath());
//            } else {
//                // If there is no backdrop for the movie we just use a default one
//                mBackgroundManager.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.material_bg));
//            }
//        }
    }

    public void testHttp() {
        try {
            Document doc = Jsoup.connect("https://mamahd.live/upcoming/").get();
            Elements links = doc.select("a[href]");

            for(Element link: links) {
                String linkText = link.attr("href");
                for(int i=0; i<TOPIC_NAMES.size(); i++) {
//                    Log.i("MainFragment", "Reached...trying " + linkText + " with " + TOPIC_NAMES.get(i));
                    if(linkText.contains("https://mamahd.live/" + TOPIC_NAMES.get(i))) {
//                        Log.i("MainFragment", "Reached...trying " + linkText + " with " + TOPIC_NAMES.get(i));

                        try {
                            Document eventDoc = Jsoup.connect(linkText).get();
                            Elements eventStreamLinks = eventDoc.select("a[href]");
                            for (Element eventStreamLink : eventStreamLinks) {
                                String eventStreamLinkText = eventStreamLink.attr("href");
//                                Log.i("MainFragment", "Reached...eventLinkText = " + eventStreamLinkText);
                                if (eventStreamLinkText.contains("http://www.mamacdn.com/link.php?asad=")) {
                                    Log.i("MainFragment", "Reached...eventStreamLinkText match = " + eventStreamLinkText);
                                    topics.get(i).onNext(eventStreamLinkText.substring("http://www.mamacdn.com/link.php?asad=".length()));
                                }
                            }
                        } catch(IOException e) {
                            Log.e("MainFragment", "Failed to call event url", e);
                        }
                        break;
                    }
                }
            }
        } catch(IOException e) {
            Log.e("MainFragment", "Failed to call main url", e);
        }
    }
}
