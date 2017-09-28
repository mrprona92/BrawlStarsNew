package com.mrprona.brawlassistant.hero.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.mrprona.brawlassistant.BeanContainer;
import com.mrprona.brawlassistant.R;
import com.mrprona.brawlassistant.base.adapter.BaseRecyclerAdapter;
import com.mrprona.brawlassistant.base.util.SteamUtils;
import com.mrprona.brawlassistant.hero.api.Hero;
import com.mrprona.brawlassistant.hero.service.HeroService;
import com.mrprona.brawlassistant.item.adapter.holder.ItemHolder;

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


        item = item.replaceAll("\\s+","");

        Hero mHero = heroService.getExactHeroByName(mContext, item);

        final int sdk = android.os.Build.VERSION.SDK_INT;

        if(mHero==null){
            Log.d("BINH", "onBindViewHolder() called with: holder = [" + item + "], position = [" + position + "]");
        }

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
        Glide.with(context).load(SteamUtils.getHeroFullImage(item.toLowerCase())).into(holder.image);
    }
}
