package com.mama.soccer;

import android.app.Application;

import com.mama.soccer.dagger.components.ApplicationComponent;
import com.mama.soccer.dagger.components.DaggerApplicationComponent;
import com.mama.soccer.dagger.modules.ApplicationModule;
import com.mama.soccer.dagger.modules.HttpClientModule;

import timber.log.Timber;

public class App extends Application {

    private static App instance;
    private ApplicationComponent mApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        // Creates Dagger Graph
        mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .httpClientModule(new HttpClientModule())
                .build();

        mApplicationComponent.inject(this);
    }

    public static App instance() {
        return instance;
    }

    public ApplicationComponent appComponent() {
        return mApplicationComponent;
    }

}
