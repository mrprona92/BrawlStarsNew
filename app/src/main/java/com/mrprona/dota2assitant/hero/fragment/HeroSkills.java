package com.mrprona.dota2assitant.hero.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mrprona.dota2assitant.R;
import com.mrprona.dota2assitant.base.fragment.SCBaseFragment;
import com.mrprona.dota2assitant.base.service.LocalSpiceService;
import com.mrprona.dota2assitant.base.util.Utils;
import com.mrprona.dota2assitant.hero.adapter.SkillAdapter;
import com.mrprona.dota2assitant.hero.api.Hero;
import com.octo.android.robospice.SpiceManager;
import com.util.responses.DefaultAbilityInfo;
import com.util.responses.HeroInfo;
import com.util.responses.Role5Info;

import java.util.ArrayList;
import java.util.List;

/**
 * User: ABadretdinov
 * Date: 16.01.14
 * Time: 15:57
 */
public class HeroSkills extends SCBaseFragment {
    private SpiceManager mSpiceManager = new SpiceManager(LocalSpiceService.class);

    private Hero mHero;

    private HeroInfo mHeroInfo;

    private List<DefaultAbilityInfo> mListDefaultAbility = new ArrayList<>();


    private SkillAdapter mSkillAdapter;

    private RecyclerView mRecycleView;


    public static HeroSkills newInstance(Hero hero, HeroInfo mHeroInfo) {
        HeroSkills fragment = new HeroSkills();
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
        mSkillAdapter = new SkillAdapter(getContext());
        mRecycleView.setItemAnimator(new DefaultItemAnimator());
        mRecycleView.setAdapter(mSkillAdapter);
        mRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    protected void initControls() {

    }

    @Override
    protected void initData() {
        DefaultAbilityInfo ability1 = new DefaultAbilityInfo();
        ability1.setName(mHeroInfo.getmSuperAbility().getName());
        ability1.setDescription(mHeroInfo.getmSuperAbility().getDescription());
        ability1.setAttackDeplay(mHeroInfo.getmSuperAbility().getAttackDeplay());
        ability1.setDamage(mHeroInfo.getmSuperAbility().getDamage());
        ability1.setRange(mHeroInfo.getmSuperAbility().getRange());
        ability1.setTypeAbility(mHeroInfo.getmSuperAbility().getTypeAbility());
        mListDefaultAbility.add(mHeroInfo.getmDefaultAbility());
        mListDefaultAbility.add(ability1);
        mSkillAdapter.setListAdapter(mListDefaultAbility);
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
        if (mSpiceManager.isStarted()) {
            mSpiceManager.shouldStop();
        }
        super.onDestroy();
    }


    public RecyclerView.LayoutManager getLayoutManager(Context context) {
        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        return manager;
    }



    private void addHeroRoles(View root, Role5Info role5Info) {
        LinearLayout rolesHolder = (LinearLayout) root.findViewById(R.id.roles_holder);
        rolesHolder.removeAllViews();

        if (role5Info.getType() != null) {
            rolesHolder.addView(initRoleUI(role5Info.getType()));
        }
        if (role5Info.getRole() != null) {
            rolesHolder.addView(initRoleUI(role5Info.getRole()));
        }

        if (role5Info.getSpeed() != null) {
            rolesHolder.addView(initRoleUI(role5Info.getSpeed()));
        }

        if (role5Info.getHitpoints() != null) {
            rolesHolder.addView(initRoleUI(role5Info.getHitpoints()));
        }

        if (role5Info.getTier() != null) {
            rolesHolder.addView(initRoleUI(role5Info.getTier()));
        }
    }

    private View initRoleUI(String text) {
        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                height);

        TextView textView = new TextView(getActivity());
        textView.setLayoutParams(layoutParams);
        textView.setText(text);
        textView.setTextColor(getResources().getColor(R.color.cmn_black));
        textView.setSingleLine(true);
        textView.setGravity(Gravity.CENTER_VERTICAL);
        textView.setCompoundDrawablePadding(Utils.dpSize(getActivity(), 5));
        return textView;
    }



}
