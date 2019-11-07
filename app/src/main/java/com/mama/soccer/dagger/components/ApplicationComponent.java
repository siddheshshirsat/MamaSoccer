package com.mama.soccer.dagger.components;

import com.mama.soccer.App;
import com.mama.soccer.dagger.AppScope;
import com.mama.soccer.dagger.modules.ApplicationModule;
import com.mama.soccer.dagger.modules.HttpClientModule;
import com.mama.soccer.ui.main.MainActivity;
import com.mama.soccer.ui.main.MainFragment;

import javax.inject.Singleton;

import dagger.Component;

@AppScope
@Singleton
@Component(modules = {
        ApplicationModule.class,
        HttpClientModule.class,
})
public interface ApplicationComponent {

    void inject(App app);
    void inject(MainFragment mainFragment);
    void inject(MainActivity mainActivity);
}
