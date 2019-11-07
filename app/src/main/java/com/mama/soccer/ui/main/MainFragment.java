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
import android.util.Log;

import com.mama.soccer.App;
import com.mama.soccer.data.fetch.ContentRefresher;
import com.mama.soccer.data.models.CardData;
import com.mama.soccer.ui.base.GlideBackgroundManager;

import java.util.List;

import javax.inject.Inject;

public class MainFragment extends BrowseSupportFragment implements OnItemViewSelectedListener {

    private GlideBackgroundManager mBackgroundManager;

    @Inject
    List<CardTopic> cardTopics;

    @Inject
    ContentRefresher contentRefresher;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        App.instance().appComponent().inject(this);

        // The background manager allows us to manage a dimmed background that does not interfere with the rows
        // It is the preferred way to set the background of a fragment
        mBackgroundManager = new GlideBackgroundManager(getActivity());

        // The brand color will be used as the background for the Headers fragment
//        setBrandColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
//        setHeadersState(HEADERS_ENABLED);
//        setHeadersTransitionOnBackEnabled(true);

        createRows();
//        prepareEntranceTransition();

        // trigger content fetching 2 seconds after activity start
        contentRefresher.refresh();
    }

    /**
     * Creates the rows and sets up the adapter of the fragment
     */
    private void createRows() {
        // Creates the RowsAdapter for the Fragment
        // The ListRowPresenter tells to render ListRow objects
        ArrayObjectAdapter rowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());
        for (int i = 0; i < cardTopics.size(); i++) {
            CardTopic cardTopic = cardTopics.get(i);

            HeaderItem headerItem = new HeaderItem(cardTopic.getId(), cardTopic.getTitle());
            ListRow listRow = new ListRow(headerItem, cardTopic.getAdapter());

            rowsAdapter.add(listRow);
        }
        // Sets this fragments Adapter.
        // The setAdapter method is defined in the BrowseFragment of the Leanback Library
        setAdapter(rowsAdapter);
        setOnItemViewSelectedListener(this);
    }

    @Override
    public void onItemSelected(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {
        Log.i("MainFragment", "Reached...item selected");
        //mBackgroundManager.loadImage("https://cdn.footystats.org/img/competitions/italy-serie-a.png");
    }

    public void fetchTestCards() {
        for(int i=0; i<cardTopics.size(); i++) {
            CardData cardData = CardData.builder().title("testTitle")
                    .team1Image("https://talksport.com/wp-content/uploads/sites/5/2019/10/NINTCHDBPICT000535500603.jpg")
                    .team2Image("https://talksport.com/wp-content/uploads/sites/5/2019/10/NINTCHDBPICT000535500603.jpg")
                    .ctaHttpUrl("testCta")
                    .build();
            cardTopics.get(i).getPublishSubject().onNext(cardData);
            cardTopics.get(i).getPublishSubject().onNext(cardData);
            cardTopics.get(i).getPublishSubject().onNext(cardData);
            cardTopics.get(i).getPublishSubject().onNext(cardData);
            cardTopics.get(i).getPublishSubject().onNext(cardData);
            cardTopics.get(i).getPublishSubject().onNext(cardData);
        }
    }

}
