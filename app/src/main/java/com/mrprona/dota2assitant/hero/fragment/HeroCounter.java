package com.mrprona.dota2assitant.hero.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.mrprona.dota2assitant.R;
import com.mrprona.dota2assitant.base.fragment.SCBaseFragment;
import com.mrprona.dota2assitant.hero.adapter.CounterAdapter;
import com.mrprona.dota2assitant.hero.api.Hero;
import com.util.responses.DefaultAbilityInfo;
import com.util.responses.HeroInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * User: ABadretdinov
 * Date: 16.01.14
 * Time: 15:57
 */
public class HeroCounter extends SCBaseFragment {

    private Hero mHero;

    private HeroInfo mHeroInfo;

    private List<DefaultAbilityInfo> mListDefaultAbility = new ArrayList<>();


    @BindView(R.id.llHardCounter)
    LinearLayout llHardCounter;

    @BindView(R.id.llfavorable)
    LinearLayout llfavorable;

    @BindView(R.id.lldifficult)
    LinearLayout lldifficult;


    @BindView(R.id.listHardCounter)
    RecyclerView listHardCounter;

    @BindView(R.id.listFavoable)
    RecyclerView listFavoable;

    @BindView(R.id.listDifficult)
    RecyclerView listDifficult;


    private CounterAdapter favorableAdapter;

    private CounterAdapter difficultAdapter;

    private CounterAdapter hardCounterAdapter;


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
        return R.layout.recycler_content_counter;
    }

    @Override
    protected void initUI() {
        if (mHeroInfo.getFavorableMatchups().isEmpty()) {
            llfavorable.setVisibility(View.GONE);
        } else {
            llfavorable.setVisibility(View.VISIBLE);
            favorableAdapter = new CounterAdapter(mHeroInfo.getFavorableMatchups(), mAppContext);
            listFavoable.setItemAnimator(new DefaultItemAnimator());
            listFavoable.setAdapter(favorableAdapter);
            listFavoable.setLayoutManager(new LinearLayoutManager(getContext()));
        }

        if (mHeroInfo.getDifficultMatchups().isEmpty()) {
            lldifficult.setVisibility(View.GONE);
        } else {
            lldifficult.setVisibility(View.VISIBLE);
            difficultAdapter = new CounterAdapter(mHeroInfo.getDifficultMatchups(), mAppContext);
            listDifficult.setItemAnimator(new DefaultItemAnimator());
            listDifficult.setAdapter(difficultAdapter);
            listDifficult.setLayoutManager(new LinearLayoutManager(getContext()));
        }

        if (mHeroInfo.getHardCounters().isEmpty()) {
            llHardCounter.setVisibility(View.GONE);
        } else {
            llHardCounter.setVisibility(View.VISIBLE);
            hardCounterAdapter = new CounterAdapter(mHeroInfo.getHardCounters(), mAppContext);
            listHardCounter.setItemAnimator(new DefaultItemAnimator());
            listHardCounter.setAdapter(hardCounterAdapter);
            listHardCounter.setLayoutManager(new LinearLayoutManager(getContext()));
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
