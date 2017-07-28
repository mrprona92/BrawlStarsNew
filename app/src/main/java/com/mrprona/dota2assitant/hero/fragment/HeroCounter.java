package com.mrprona.dota2assitant.hero.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.mrprona.dota2assitant.R;
import com.mrprona.dota2assitant.base.fragment.SCBaseFragment;
import com.mrprona.dota2assitant.hero.adapter.CounterAdapter;
import com.mrprona.dota2assitant.hero.api.Hero;
import com.util.responses.DefaultAbilityInfo;
import com.util.responses.HeroInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * User: ABadretdinov
 * Date: 16.01.14
 * Time: 15:57
 */
public class HeroCounter extends SCBaseFragment {

    private Hero mHero;

    private HeroInfo mHeroInfo;

    private List<DefaultAbilityInfo> mListDefaultAbility = new ArrayList<>();


    private CounterAdapter favorableAdapter;

    private CounterAdapter difficultAdapter;

    private CounterAdapter hardCounterAdapter;

    private RecyclerView mRecycleView;





    public static HeroCounter newInstance(Hero hero, HeroInfo mHeroInfo) {
        HeroCounter fragment = new HeroCounter();
        fragment.mHero = hero;
        fragment.mHeroInfo = mHeroInfo;
        return fragment;
    }

    @Override
    public int getToolbarTitle() {
        return 0;
    }

    @Override
    public String getToolbarTitleString() {
        return null;
    }

    @Override
    protected int getViewContent() {
        return R.layout.recycler_content;
    }

    @Override
    protected void initUI() {
        mRecycleView = (RecyclerView) findViewById(R.id.list);


        if(mHeroInfo.getHardCounters().isEmpty()){

        }else{
            favorableAdapter = new CounterAdapter(mHeroInfo.getHardCounters(), mAppContext);
            mRecycleView.setItemAnimator(new DefaultItemAnimator());
            mRecycleView.setAdapter(favorableAdapter);
            mRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        }

        if(mHeroInfo.getHardCounters().isEmpty()){

        }else{
            favorableAdapter = new CounterAdapter(mHeroInfo.getHardCounters(), mAppContext);
            mRecycleView.setItemAnimator(new DefaultItemAnimator());
            mRecycleView.setAdapter(favorableAdapter);
            mRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        }


        if(mHeroInfo.getHardCounters().isEmpty()){

        }else{
            favorableAdapter = new CounterAdapter(mHeroInfo.getHardCounters(), mAppContext);
            mRecycleView.setItemAnimator(new DefaultItemAnimator());
            mRecycleView.setAdapter(favorableAdapter);
            mRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        }



    }

    @Override
    protected void initControls() {

    }

    @Override
    protected void initData() {

    }

    @Override
    public void hideInformation() {

    }

    @Override
    protected void registerListeners() {

    }

    @Override
    protected void unregisterListener() {

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onStart() {

        super.onStart();
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }


    public RecyclerView.LayoutManager getLayoutManager(Context context) {
        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        return manager;
    }
}
