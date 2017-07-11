package com.mrprona.dota2assitant.base.remote;

import com.mrprona.dota2assitant.base.api.ti4.PrizePoolHolder;
import com.mrprona.dota2assitant.news.api.AppNewsHolder;

import java.util.List;

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