package com.mama.soccer.ui.main;

import android.os.Bundle;
import android.os.Handler;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;
import android.support.v17.leanback.widget.VerticalGridPresenter;
import android.util.Log;

import com.mama.soccer.data.models.CardData;

import java.util.List;

public class VerticalGridSupportFragment extends android.support.v17.leanback.app.VerticalGridSupportFragment {
    private static final int NUM_COLUMNS = 1;
    private static final int NUM_ITEMS = 50;
    private static final int HEIGHT = 200;
    private static final boolean TEST_ENTRANCE_TRANSITION = true;
    private ArrayObjectAdapter mAdapter;
    private List<CardTopic> cardTopics;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i("VerticalGridPresenter", "onCreate");
        super.onCreate(savedInstanceState);
        setTitle("Leanback Vertical Grid Demo");
        setupFragment();
        if (TEST_ENTRANCE_TRANSITION) {
            // don't run entrance transition if fragment is restored.
            if (savedInstanceState == null) {
                prepareEntranceTransition();
            }
        }
        // simulates in a real world use case  data being loaded two seconds later
        new Handler().postDelayed(new Runnable() {
            public void run() {
                loadData();
                startEntranceTransition();
            }
        }, 2000);
    }
    private void loadData() {
        for (int i = 0; i < NUM_ITEMS; i++) {
            CardData cardData = CardData.builder().title("testTitle")
                    .tileImage("https://talksport.com/wp-content/uploads/sites/5/2019/10/NINTCHDBPICT000535500603.jpg")
                    .ctaHttpUrl("testCta")
                    .build();
            cardTopics.get(0).getAdapter().add(cardData);
            cardTopics.get(1).getAdapter().add(cardData);
            cardTopics.get(2).getAdapter().add(cardData);
            cardTopics.get(3).getAdapter().add(cardData);
            cardTopics.get(4).getAdapter().add(cardData);
        }
    }
    private void setupFragment() {
        VerticalGridPresenter gridPresenter = new VerticalGridPresenter();
        gridPresenter.setNumberOfColumns(NUM_COLUMNS);
        setGridPresenter(gridPresenter);

        cardTopics = CardTopic.getCardTopics(new CardPresenter(getContext()));
        mAdapter = new ArrayObjectAdapter(new ListRowPresenter());
        for (int i = 0; i < 7; i++) {
            HeaderItem headerItem = new HeaderItem(i,"Test Row Header");
            ListRow listRow = new ListRow(headerItem, cardTopics.get(i).getAdapter());
            mAdapter.add(listRow);
        }
        setAdapter(mAdapter);
        setOnItemViewClickedListener(new OnItemViewClickedListener() {
            @Override
            public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item,
                                      RowPresenter.ViewHolder rowViewHolder, Row row) {
                Log.i("VerticalGridPresenter", "onItemClicked: " + item + " row " + row);
            }
        });
    }
}
