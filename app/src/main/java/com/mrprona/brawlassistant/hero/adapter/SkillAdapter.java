package com.mrprona.brawlassistant.hero.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mrprona.brawlassistant.R;
import com.util.responses.DefaultAbilityInfo;

import java.util.List;

/**
 * Created by Ravi Tamada on 18/05/16.
 */
public class SkillAdapter extends RecyclerView.Adapter<SkillAdapter.MyViewHolder> {

    private Context mContext;
    private List<DefaultAbilityInfo> abilityInfos;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView lblAttackDelayValue, lblDameValue, lblRechargeValue, lblRangeValue, lblSkillTypeValue;
        public TextView lblSkillInfo, lblSkillName;
        public ImageView thumbnail;
        public LinearLayout llRechargeTime;
        public TextView lblSkillTypeMain;


        public MyViewHolder(View view) {
            super(view);
            lblSkillInfo = (TextView) view.findViewById(R.id.lblSkillInfo);
            lblSkillName = (TextView) view.findViewById(R.id.lblSkillName);
            lblAttackDelayValue = (TextView) view.findViewById(R.id.lblAttackDelayValue);
            lblDameValue = (TextView) view.findViewById(R.id.lblDameValue);
            lblRechargeValue = (TextView) view.findViewById(R.id.lblRechargeValue);
            lblRangeValue = (TextView) view.findViewById(R.id.lblRangeValue);
            lblSkillTypeValue = (TextView) view.findViewById(R.id.lblSkillTypeValue);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            llRechargeTime = (LinearLayout) view.findViewById(R.id.llRechargeTime);
            lblSkillTypeMain = (TextView) view.findViewById(R.id.lblSkillTypeMain);
        }

    }


    public SkillAdapter(Context mContext) {
        this.mContext = mContext;

    }

    public void setListAdapter(List<DefaultAbilityInfo> abilityInfos) {
        this.abilityInfos = abilityInfos;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.abilities_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        DefaultAbilityInfo abilityInfo = abilityInfos.get(position);
        holder.lblSkillInfo.setText(abilityInfo.getDescription());
        holder.lblSkillName.setText(abilityInfo.getName());


        if (abilityInfo.getTimeCharger() != null) {
            Glide.with(mContext).load(R.drawable.ability_default).into(holder.thumbnail);
            holder.llRechargeTime.setVisibility(View.VISIBLE);
            holder.lblRechargeValue.setText(abilityInfo.getTimeCharger());
            holder.lblSkillTypeMain.setText(mContext.getString(R.string.ability_type_default));
        } else {
            Glide.with(mContext).load(R.drawable.ability_super).into(holder.thumbnail);
            holder.llRechargeTime.setVisibility(View.GONE);
            holder.lblSkillTypeMain.setText(mContext.getString(R.string.ability_type_super));
        }

        holder.lblAttackDelayValue.setText(abilityInfo.getAttackDeplay());
        holder.lblDameValue.setText(abilityInfo.getDamage());
        holder.lblRangeValue.setText(abilityInfo.getRange());
        holder.lblSkillTypeValue.setText(abilityInfo.getTypeAbility());
    }


    /**
     * Click listener for popup menu items
     */


    @Override
    public int getItemCount() {
        Log.d("BINH", "getItemCount() called" + abilityInfos.size());
        return abilityInfos.size();
    }


}
