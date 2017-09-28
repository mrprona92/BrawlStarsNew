package com.mrprona.brawlassistant.news.service;

import android.content.Context;

import com.mrprona.brawlassistant.news.api.AppNews;

/**
 * User: Histler
 * Date: 21.04.14
 */
public interface NewsService {
    AppNews getNews(Context context, Long fromDate);
}
