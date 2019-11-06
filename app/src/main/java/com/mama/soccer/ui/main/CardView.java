package com.mama.soccer.ui.main;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mama.soccer.R;
import com.mama.soccer.ui.base.BindableCardView;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by <a href="mailto:marcus@gabilheri.com">Marcus Gabilheri</a>
 *
 * @author Marcus Gabilheri
 * @version 1.0
 * @since 10/8/16.
 */
public class CardView extends BindableCardView<String> {

    @BindView(R.id.poster_iv)
    ImageView mPosterIV;

    @BindView(R.id.vote_average_tv)
    TextView mVoteAverageTV;

    String itemData;

    public CardView(Context context) {
        super(context);
        ButterKnife.bind(this);
    }

    @Override
    protected void bind(String itemData) {
        this.itemData = itemData;
        Glide.with(getContext())
                .load("https://talksport.com/wp-content/uploads/sites/5/2019/10/NINTCHDBPICT000535500603.jpg")
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mPosterIV);
        mVoteAverageTV.setText(String.format(Locale.getDefault(), "%s", itemData));
    }

    public ImageView getPosterIV() {
        return mPosterIV;
    }

    public String getItemData() {
        return itemData;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.card_movie;
    }
}