package com.mama.soccer.dagger.components;

import com.mama.soccer.App;
import com.mama.soccer.dagger.AppScope;
import com.mama.soccer.dagger.modules.ApplicationModule;
import com.mama.soccer.dagger.modules.HttpClientModule;
import com.mama.soccer.ui.main.MainFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by <a href="mailto:marcus@gabilheri.com">Marcus Gabilheri</a>
 *
 * @author Marcus Gabilheri
 * @version 1.0
 * @since 9/4/16.
 */
@AppScope
@Singleton
@Component(modules = {
        ApplicationModule.class,
        HttpClientModule.class,
})
public interface ApplicationComponent {

    void inject(App app);
    void inject(MainFragment mainFragment);
}
