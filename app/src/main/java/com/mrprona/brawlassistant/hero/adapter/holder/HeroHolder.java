package com.mrprona.brawlassistant.hero.adapter.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mrprona.brawlassistant.R;
import com.mrprona.brawlassistant.base.adapter.OnItemClickListener;
import com.mrprona.brawlassistant.base.adapter.holder.BaseViewHolder;

/**
 * Created by ABadretdinov
 * 17.12.2014
 * 18:39
 */
public class HeroHolder extends BaseViewHolder {
    public TextView name;
    public ImageView image;
    public ImageView heroType;

    public HeroHolder(View itemView, OnItemClickListener listener) {
        super(itemView, listener);
    }

    @Override
    protected void initView(View itemView) {
        name = (TextView) itemView.findViewById(R.id.name);
        image = (ImageView) itemView.findViewById(R.id.img);
        heroType = (ImageView) itemView.findViewById(R.id.imgHeroType);
    }
}
