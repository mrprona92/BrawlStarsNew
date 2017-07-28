package com.mrprona.dota2assitant.hero.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.bumptech.glide.Glide;
import com.mrprona.dota2assitant.BeanContainer;
import com.mrprona.dota2assitant.R;
import com.mrprona.dota2assitant.base.adapter.BaseRecyclerAdapter;
import com.mrprona.dota2assitant.base.util.SteamUtils;
import com.mrprona.dota2assitant.hero.api.Hero;
import com.mrprona.dota2assitant.hero.service.HeroService;
import com.mrprona.dota2assitant.item.adapter.holder.ItemHolder;
import com.mrprona.dota2assitant.item.api.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Histler
 * Date: 18.01.14
 */
public class CounterAdapter extends BaseRecyclerAdapter<String, ItemHolder> {

    private List<String> mFiltered;
    private Context mContext;
    HeroService heroService = BeanContainer.getInstance().getHeroService();

    public CounterAdapter(List<String> items, Context current) {
        super(items);
        this.mFiltered = mData;
        this.mContext = current;
    }

    @Override
    public int getItemCount() {
        return mFiltered.size();
    }

    @Override
    public String getItem(int position) {
        return mFiltered.get(position);
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row, parent, false);
        return new ItemHolder(view, mOnItemClickListener);
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {

        String item = getItem(position);
        holder.name.setText(item);

        Hero mHero = heroService.getExactHeroByName(mContext, item);

        final int sdk = android.os.Build.VERSION.SDK_INT;

        String heroType = mHero.getTier();

        Drawable tempDrawAble = null;

        if (heroType != null) {
            if (heroType.equalsIgnoreCase("common")) {
                tempDrawAble = mContext.getResources().getDrawable(R.drawable.border_image_item_common);
            }
            if (heroType.equalsIgnoreCase("rare")) {
                tempDrawAble = mContext.getResources().getDrawable(R.drawable.border_image_item_attribute);
            }
            if (heroType.equalsIgnoreCase("epic")) {
                tempDrawAble = mContext.getResources().getDrawable(R.drawable.border_image_item_rare_blue);
            }
            if (heroType.equalsIgnoreCase("legendary")) {
                tempDrawAble = mContext.getResources().getDrawable(R.drawable.border_image_item_artifact);
            }
        }

        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            holder.image.setBackgroundDrawable(tempDrawAble);
        } else {
            holder.image.setBackground(tempDrawAble);
        }

        Context context = holder.name.getContext();
        Glide.with(context).load(SteamUtils.getHeroFullImage(item)).into(holder.image);
    }
}
