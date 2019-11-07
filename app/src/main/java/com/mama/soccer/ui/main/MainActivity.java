package com.mama.soccer.ui.main;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;

import com.mama.soccer.App;
import com.mama.soccer.R;
import com.mama.soccer.data.fetch.ContentRefresher;

import java.util.List;

import javax.inject.Inject;

public class MainActivity extends FragmentActivity {

    @Inject
    List<CardTopic> cardTopics;

    @Inject
    ContentRefresher contentRefresher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.instance().appComponent().inject(this);

        setContentView(R.layout.main_activity);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d("MainActivity", "Reached..." + keyCode);
        switch (keyCode) {
            case KeyEvent.KEYCODE_MENU:
                for(CardTopic cardTopic: cardTopics) {
                    cardTopic.clear();
                }
                contentRefresher.refresh();
                return true;
        }
        return false;
    }
}
