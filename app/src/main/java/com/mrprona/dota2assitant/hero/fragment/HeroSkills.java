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
import android.widget.ImageView;
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

    private List<String> mListUpgrade;

    private HeroInfo mHeroInfo;

    private List<DefaultAbilityInfo> mListDefaultAbility = new ArrayList<>();


    private SkillAdapter mSkillAdapter;

    private RecyclerView mRecycleView;


    public static HeroSkills newInstance(Hero hero, HeroInfo mHeroInfo) {
        HeroSkills fragment = new HeroSkills();
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
        mListUpgrade = mHeroInfo.getPriority();
        addHeroRoles(mListUpgrade);
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


    private void addHeroRoles(List<String> listUpgrade) {
        LinearLayout rolesHolder = (LinearLayout) findViewById(R.id.roles_holder);
        rolesHolder.removeAllViews();
        for (String mString : listUpgrade) {
            rolesHolder.addView(initRoleUI(mString));
        }

    }

    private View initRoleUI(String text) {
        View child = getLayoutInflater(null).inflate(R.layout.hero_skill_upgrade, null);
        TextView mTextView = (TextView) child.findViewById(R.id.lblUpgradeName);
        ImageView mImageView = (ImageView) child.findViewById(R.id.imgSKillUpgrade);

        StringBuffer strbuff=new StringBuffer(text);
        char ch=Character.toUpperCase(strbuff.charAt(3));
        strbuff.setCharAt(3, ch);
        mTextView.setText(strbuff.toString());


        String skill = text.substring(3, text.length());
        if (skill.equalsIgnoreCase("health")) {
            mImageView.setImageDrawable(mAppContext.getResources().getDrawable(R.drawable.ic_upgrade_health));
        } else if (skill.equalsIgnoreCase("super")) {
            mImageView.setImageDrawable(mAppContext.getResources().getDrawable(R.drawable.ic_upgrade_super));
        } else if (skill.equalsIgnoreCase("attack")) {
            mImageView.setImageDrawable(mAppContext.getResources().getDrawable(R.drawable.ic_upgrade_attack));
        }

        return child;
    }


}
