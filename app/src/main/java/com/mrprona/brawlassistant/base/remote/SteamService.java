package com.mrprona.brawlassistant.base.remote;

import com.mrprona.brawlassistant.base.api.ti4.PrizePoolHolder;
import com.mrprona.brawlassistant.news.api.AppNewsHolder;

import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by ABadretdinov
 * 16.03.2015
 * 14:17
 */
public interface SteamService {


    @GET("/IEconDOTA2_570/GetTournamentPrizePool/v1")
//ti4 = 600
    PrizePoolHolder getLeaguePrizePool(@Query("leagueid") long leagueId);

    @GET("/ISteamNews/GetNewsForApp/v0002/?appid=570&count=50&format=json")
    AppNewsHolder getNews(@Query("enddate") Long enddate);


}