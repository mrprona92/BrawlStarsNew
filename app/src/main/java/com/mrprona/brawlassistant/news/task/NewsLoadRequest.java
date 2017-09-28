package com.mrprona.brawlassistant.news.task;

import android.content.Context;

import com.mrprona.brawlassistant.BeanContainer;
import com.mrprona.brawlassistant.base.service.TaskRequest;
import com.mrprona.brawlassistant.news.api.AppNews;
import com.mrprona.brawlassistant.news.service.NewsService;

/**
 * Created by ABadretdinov
 * 20.08.2015
 * 15:53
 */
public class NewsLoadRequest extends TaskRequest<AppNews> {
    private Long mFromDate;
    private Context mContext;

    public NewsLoadRequest(Context context, Long fromDate) {
        super(AppNews.class);
        this.mContext = context;
        this.mFromDate = fromDate;
    }

    @Override
    public AppNews loadData() throws Exception {

        BeanContainer container = BeanContainer.getInstance();
        NewsService newsService = container.getNewsService();
        return newsService.getNews(mContext, mFromDate);
    }
}
