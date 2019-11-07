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

public class CardView extends BindableCardView<CardData> {

    @BindView(R.id.team1)
    @Getter
    ImageView mTeam1ImageView;

    @BindView(R.id.titleBar)
    TextView mTitleBar;

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
                .load(cardData.getTeam1Image())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mTeam1ImageView);

        mTitleBar.setText(String.format(Locale.getDefault(), "%s", cardData.getTitle()));
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.card_movie;
    }
}