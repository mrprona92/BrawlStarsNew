package com.mrprona.dota2assitant.hero.adapter.pager;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.mrprona.dota2assitant.R;
import com.mrprona.dota2assitant.hero.api.Hero;
import com.mrprona.dota2assitant.hero.fragment.HeroDefaultItemBuild;
import com.mrprona.dota2assitant.hero.fragment.HeroSkills;
import com.mrprona.dota2assitant.hero.fragment.HeroStatInfo;
import com.util.responses.HeroInfo;

/**
 * User: ABadretdinov
 * Date: 15.01.14
 * Time: 15:20
 */
public class HeroPagerAdapter extends FragmentStatePagerAdapter {
    private Context context;
    private Hero hero;
    private HeroInfo mHeroInfo;

    public HeroPagerAdapter(FragmentManager fragmentManager, Context context, Hero hero, HeroInfo mHeroInfo) {
        super(fragmentManager);
        this.hero = hero;
        this.context = context;
        this.mHeroInfo = mHeroInfo;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return HeroStatInfo.newInstance(hero, mHeroInfo);
            case 1:
                return HeroSkills.newInstance(hero, mHeroInfo);
            case 2:
                return HeroSkills.newInstance(hero, mHeroInfo);
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getString(R.string.info);
            case 1:
                return context.getString(R.string.skills);
            case 2:
                return context.getString(R.string.counter);
            default:
                return "";
        }
    }
}
