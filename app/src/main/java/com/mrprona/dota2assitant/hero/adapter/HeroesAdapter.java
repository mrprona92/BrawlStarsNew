package com.mrprona.dota2assitant.hero.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.mrprona.dota2assitant.R;
import com.mrprona.dota2assitant.base.adapter.BaseRecyclerAdapter;
import com.mrprona.dota2assitant.base.util.SteamUtils;
import com.mrprona.dota2assitant.hero.adapter.holder.HeroHolder;
import com.bumptech.glide.Glide;
import com.mrprona.dota2assitant.hero.api.Hero;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * User: ABadretdinov
 * Date: 29.08.13
 * Time: 12:25
 */
public class HeroesAdapter extends BaseRecyclerAdapter<Hero, HeroHolder> implements Filterable {
    private List<Hero> filtered;
    private Context mContext;
    private Map<Long, Integer> mMapType;


    public HeroesAdapter(List<Hero> heroes, Context current, Map<Long, Integer> mMapType) {
        super(heroes);
        filtered = mData;
        this.mContext = current;
        this.mMapType = mMapType;
    }

    @Override
    public int getItemCount() {
        return filtered.size();
    }

    @Override
    public Hero getItem(int position) {
        return filtered.get(position);
    }

    @Override
    public HeroHolder onCreateViewHolder(ViewGroup parent, int position) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hero_row, parent, false);
        return new HeroHolder(view, mOnItemClickListener);
    }

    @Override
    public void onBindViewHolder(HeroHolder holder, int position) {
        Hero hero = getItem(position);
        holder.name.setText(hero.getLocalizedName());
        final int sdk = android.os.Build.VERSION.SDK_INT;

        String type = hero.getTier();

        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            if (type.equalsIgnoreCase("common")) {
                holder.heroType.setImageDrawable(mContext.getResources().getDrawable(R.drawable.common_character));
            } else if (type.equalsIgnoreCase("rare")) {
                holder.heroType.setImageDrawable(mContext.getResources().getDrawable(R.drawable.rare));
            } else if (type.equalsIgnoreCase("epic")) {
                holder.heroType.setImageDrawable(mContext.getResources().getDrawable(R.drawable.epic));
            } else if (type.equalsIgnoreCase("legendary")) {
                holder.heroType.setImageDrawable(mContext.getResources().getDrawable(R.drawable.legendary));
            }
        } else {
            if (type.equalsIgnoreCase("common")) {
                holder.heroType.setImageDrawable(mContext.getResources().getDrawable(R.drawable.common_character));
            } else if (type.equalsIgnoreCase("rare")) {
                holder.heroType.setImageDrawable(mContext.getResources().getDrawable(R.drawable.rare));
            } else if (type.equalsIgnoreCase("epic")) {
                holder.heroType.setImageDrawable(mContext.getResources().getDrawable(R.drawable.epic));
            } else if (type.equalsIgnoreCase("legendary")) {
                holder.heroType.setImageDrawable(mContext.getResources().getDrawable(R.drawable.legendary));
            }
        }

        Context context = holder.name.getContext();
        Glide.with(context).load(SteamUtils.getHeroFullImage(hero.getDotaId())).into(holder.image);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                List<Hero> filteredHeroes = new ArrayList<Hero>();
                if (constraint == null) {
                    filterResults.values = mData;
                    filterResults.count = mData.size();
                    return filterResults;
                }
                String lowerConstr = constraint.toString().toLowerCase();
                for (Hero hero : mData) {
                    if (hero.getLocalizedName().toLowerCase().contains(lowerConstr) || hero.getName().toLowerCase().contains(lowerConstr)) {
                        filteredHeroes.add(hero);
                    }
                }
                filterResults.count = filteredHeroes.size();
                filterResults.values = filteredHeroes;
                return filterResults;
            }

            @Override
            @SuppressWarnings("unchecked")
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filtered = (ArrayList<Hero>) results.values;
                if (filtered == null) {
                    filtered = new ArrayList<Hero>();
                }
                notifyDataSetChanged();
            }
        };
    }
}
