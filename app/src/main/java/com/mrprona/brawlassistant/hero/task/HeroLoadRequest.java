package com.mrprona.brawlassistant.hero.task;

import android.content.Context;

import com.mrprona.brawlassistant.BeanContainer;
import com.mrprona.brawlassistant.base.service.TaskRequest;
import com.mrprona.brawlassistant.hero.api.Hero;
import com.mrprona.brawlassistant.hero.service.HeroService;

/**
 * Created by ABadretdinov
 * 20.08.2015
 * 14:44
 */
public class HeroLoadRequest extends TaskRequest<Hero.List> {

    private Context mContext;
    private String mFilter;

    public HeroLoadRequest(Context context, String filter) {
        super(Hero.List.class);
        mContext = context;
        mFilter = filter;
    }

    @Override
    public Hero.List loadData() throws Exception {
        HeroService heroService = BeanContainer.getInstance().getHeroService();
        return heroService.getFilteredHeroes(mContext, mFilter);
    }
}
