package com.mrprona.brawlassistant.hero.adapter.pager;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.mrprona.brawlassistant.R;
import com.mrprona.brawlassistant.hero.api.Hero;
import com.mrprona.brawlassistant.hero.fragment.HeroCounter;
import com.mrprona.brawlassistant.hero.fragment.HeroSkills;
import com.mrprona.brawlassistant.hero.fragment.HeroStatInfo;
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
                return HeroCounter.newInstance(hero, mHeroInfo);
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
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
