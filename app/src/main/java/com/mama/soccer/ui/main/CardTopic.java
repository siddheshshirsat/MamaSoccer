package com.mama.soccer.ui.main;

import android.content.Context;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.util.Log;

import com.mama.soccer.data.models.CardData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.Builder;
import lombok.Data;
import lombok.Value;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

/**
 * Created by <a href="mailto:marcus@gabilheri.com">Marcus Gabilheri</a>
 *
 * @author Marcus Gabilheri
 * @version 1.0
 * @since 10/11/16.
 */

@Data
@Builder
public class CardTopic {
//    private int page;
    private int id;
    private ArrayObjectAdapter adapter;
    private String title;
    private String urlSuffix;
    private PublishSubject<CardData> publishSubject;

    private static List<CardTopic> sTopics;

    public static List<CardTopic> getCardTopics(Context context) {
        if(sTopics == null) {

            CardPresenter cardPresenter = new CardPresenter(context);
            sTopics = new ArrayList<CardTopic>(Arrays.asList(
                    CardTopic.builder()
                            .id(0)
                            .adapter(new ArrayObjectAdapter(cardPresenter))
                            .title("Serie A")
                            .urlSuffix("seria-a")
                            .publishSubject(PublishSubject.create())
                            .build(),

                    CardTopic.builder()
                            .id(1)
                            .adapter(new ArrayObjectAdapter(cardPresenter))
                            .title("Primere League")
                            .urlSuffix("primere-league")
                            .publishSubject(PublishSubject.create())
                            .build(),

                    CardTopic.builder()
                            .id(2)
                            .adapter(new ArrayObjectAdapter(cardPresenter))
                            .title("La Liga")
                            .urlSuffix("la-liga")
                            .publishSubject(PublishSubject.create())
                            .build(),

                    CardTopic.builder()
                            .id(3)
                            .adapter(new ArrayObjectAdapter(cardPresenter))
                            .title("Bundesliga")
                            .urlSuffix("bundesliga")
                            .publishSubject(PublishSubject.create())
                            .build(),

                    CardTopic.builder()
                            .id(4)
                            .adapter(new ArrayObjectAdapter(cardPresenter))
                            .title("Champions League")
                            .urlSuffix("champions-league")
                            .publishSubject(PublishSubject.create())
                            .build(),

                    CardTopic.builder()
                            .id(5)
                            .adapter(new ArrayObjectAdapter(cardPresenter))
                            .title("Europa League")
                            .urlSuffix("europa-league")
                            .publishSubject(PublishSubject.create())
                            .build(),

                    CardTopic.builder()
                            .id(6)
                            .adapter(new ArrayObjectAdapter(cardPresenter))
                            .title("NBA")
                            .urlSuffix("nba")
                            .publishSubject(PublishSubject.create())
                            .build()
            ));
            for(CardTopic cardTopic: sTopics) {
                cardTopic.decoratePublishSubject();
            }
        }
        return  sTopics;
    }

    public void clear() {
        this.adapter.clear();
        this.publishSubject = PublishSubject.create();
        decoratePublishSubject();
    }

    private void decoratePublishSubject() {
        publishSubject
                .onBackpressureBuffer()
                .distinct()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(cardData -> {
                    bindMovieResponse(cardData);
//                    startEntranceTransition();
                }, e -> {
                    Log.e("MainFragment", "Error updating UI with new event links", e);
                });

    }

    private void bindMovieResponse(CardData cardData) {
        adapter.add(cardData);
    }
}
