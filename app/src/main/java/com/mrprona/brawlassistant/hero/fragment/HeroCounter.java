package com.mrprona.brawlassistant.hero.fragment;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mrprona.brawlassistant.R;
import com.mrprona.brawlassistant.base.fragment.SCBaseFragment;
import com.mrprona.brawlassistant.hero.adapter.CounterAdapter;
import com.mrprona.brawlassistant.hero.api.Hero;
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

    public HeroCounter() {

    }


    public static HeroCounter newInstance(Hero hero, HeroInfo mHeroInfo) {
        HeroCounter fragment = new HeroCounter();

        Bundle args = new Bundle();
        args.putSerializable("hero", hero);
        args.putSerializable("heroinfo", mHeroInfo);
        fragment.setArguments(args);

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

        mHeroInfo = (HeroInfo) getArguments().getSerializable(
                "heroinfo");

        mHero = (Hero) getArguments().getSerializable(
                "hero");

        if ((mHeroInfo.getFavorableMatchups() == null)||(mHeroInfo.getFavorableMatchups().size()==0)) {
            llfavorable.setVisibility(View.GONE);
        } else {
            llfavorable.setVisibility(View.VISIBLE);
            favorableAdapter = new CounterAdapter(mHeroInfo.getFavorableMatchups(), mAppContext);
            listFavoable.setAdapter(favorableAdapter);
            layoutManagerForRecycler(listFavoable);
        }

        if (mHeroInfo.getDifficultMatchups() == null||(mHeroInfo.getDifficultMatchups().size()==0)) {
            lldifficult.setVisibility(View.GONE);
        } else {
            lldifficult.setVisibility(View.VISIBLE);
            difficultAdapter = new CounterAdapter(mHeroInfo.getDifficultMatchups(), mAppContext);
            listDifficult.setAdapter(difficultAdapter);
            layoutManagerForRecycler(listDifficult);
        }

        if (mHeroInfo.getHardCounters() == null||(mHeroInfo.getHardCounters().size()==0)) {
            llHardCounter.setVisibility(View.GONE);
        } else {
            llHardCounter.setVisibility(View.VISIBLE);
            hardCounterAdapter = new CounterAdapter(mHeroInfo.getHardCounters(), mAppContext);
            listHardCounter.setAdapter(hardCounterAdapter);
            layoutManagerForRecycler(listHardCounter);
        }

        if(mHeroInfo.getTips()!=null)
        addHeroRoles(mHeroInfo.getTips());

    }


    private void layoutManagerForRecycler(RecyclerView mRecycleView) {
        mRecycleView.setItemAnimator(new DefaultItemAnimator());
        mRecycleView.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        mRecycleView.setHasFixedSize(true);
        mRecycleView.setNestedScrollingEnabled(false);
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

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }


    private void addHeroRoles(List<String> listTips) {
        LinearLayout rolesHolder = (LinearLayout) findViewById(R.id.roles_tips);
        rolesHolder.removeAllViews();
        for (String mString : listTips) {
            rolesHolder.addView(initRoleUI(mString));
        }

    }

    private View initRoleUI(String text) {
        View child = getLayoutInflater(null).inflate(R.layout.hero_tips, null);
        TextView mTextView = (TextView) child.findViewById(R.id.lblTipsName);
        //ImageView mImageView = (ImageView) child.findViewById(R.id.imgSKillUpgrade);
        mTextView.setText(text.toString()+"");

        return child;
    }



}
