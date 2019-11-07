package com.mama.soccer.dagger.modules;

import android.app.Application;

import com.mama.soccer.data.fetch.ContentRefresher;
import com.mama.soccer.ui.main.CardTopic;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    Application mApplication;

    public ApplicationModule(Application application) {
        mApplication = application;
    }

    @Singleton
    @Provides
    Application providesApplication() {
        return mApplication;
    }

    @Singleton
    @Provides
    List<CardTopic> providesCardTopics(Application app) {
        return CardTopic.getCardTopics(app.getApplicationContext());
    }

    @Singleton
    @Provides
    ExecutorService providesExecutorService() {
        return Executors.newFixedThreadPool(12);
    }

    @Singleton
    @Provides
    ContentRefresher providesContentRefresher(ExecutorService executorService, List<CardTopic> cardTopics) {
        return new ContentRefresher(executorService, cardTopics);
    }
}
