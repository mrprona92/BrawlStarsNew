package com.mrprona.brawlassistant.hero.task;

import android.content.Context;

import com.mrprona.brawlassistant.BeanContainer;
import com.mrprona.brawlassistant.base.service.TaskRequest;
import com.mrprona.brawlassistant.hero.api.CarouselHero;
import com.mrprona.brawlassistant.hero.service.HeroService;

/**
 * Created by ABadretdinov
 * 20.08.2015
 * 14:42
 */
public class CarouselHeroesLoadRequest extends TaskRequest<CarouselHero.List> {

    private Context mContext;
    private String mFilter;
    private String mSearch;

    public CarouselHeroesLoadRequest(Context context, String filter, String search) {
        super(CarouselHero.List.class);
        mContext = context;
        mFilter = filter;
        mSearch = search;
    }

    @Override
    public CarouselHero.List loadData() throws Exception {
        HeroService heroService = BeanContainer.getInstance().getHeroService();
        return heroService.getCarouselHeroes(mContext, mFilter, mSearch);
    }
}
