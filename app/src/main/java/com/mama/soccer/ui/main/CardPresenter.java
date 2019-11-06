package com.mama.soccer.ui.main;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v17.leanback.widget.Presenter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by <a href="mailto:marcus@gabilheri.com">Marcus Gabilheri</a>
 *
 * @author Marcus Gabilheri
 * @version 1.0
 * @since 10/8/16.
 */

public class CardPresenter extends Presenter implements View.OnClickListener {

    private Context context;
    public CardPresenter(Context context) {
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder(new CardView(parent.getContext()));
        setOnClickListener(viewHolder, this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item) {
        ((CardView) viewHolder.view).bind((String) item);
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {

    }

    public void onClick(View view) {
        Log.i("MoviePresenter", "Reached...item clicked..." + view);
        if(view instanceof  CardView) {
            CardView movieCardView = (CardView)view;
            Log.i("MoviePresenter", "Reached...itemData = " + movieCardView.getItemData());
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(movieCardView.getItemData()));
            context.startActivity(i);
        }
    }
}
