package com.mama.soccer.ui.main;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mama.soccer.R;
import com.mama.soccer.data.models.CardData;
import com.mama.soccer.ui.base.BindableCardView;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import lombok.Getter;

/**
 * Created by <a href="mailto:marcus@gabilheri.com">Marcus Gabilheri</a>
 *
 * @author Marcus Gabilheri
 * @version 1.0
 * @since 10/8/16.
 */
public class CardView extends BindableCardView<CardData> {

    @BindView(R.id.poster_iv)
    @Getter
    ImageView mPosterIV;

    @BindView(R.id.vote_average_tv)
    TextView mVoteAverageTV;

    @Getter
    private CardData cardData;

    public CardView(Context context) {
        super(context);
        ButterKnife.bind(this);
    }

    @Override
    protected void bind(CardData cardData) {
        this.cardData = cardData;
        Glide.with(getContext())
                .load(cardData.getTileImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mPosterIV);
        mVoteAverageTV.setText(String.format(Locale.getDefault(), "%s", cardData.getTitle()));
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.card_movie;
    }
}