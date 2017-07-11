package com.mrprona.dota2assitant;

import com.mrprona.dota2assitant.base.dao.CreateTableDao;
import com.mrprona.dota2assitant.base.remote.SteamService;
import com.mrprona.dota2assitant.base.remote.update.UpdateRemoteService;
import com.mrprona.dota2assitant.base.remote.update.UpdateRemoteServiceImpl;
import com.mrprona.dota2assitant.base.service.LocalUpdateService;
import com.mrprona.dota2assitant.base.service.ti4.TI4ServiceImpl;
import com.mrprona.dota2assitant.base.service.update.UpdateService;
import com.mrprona.dota2assitant.base.service.update.UpdateServiceImpl;
import com.mrprona.dota2assitant.hero.dao.AbilityDao;
import com.mrprona.dota2assitant.hero.dao.HeroDao;
import com.mrprona.dota2assitant.hero.dao.HeroStatsDao;
import com.mrprona.dota2assitant.hero.dao.TalentDao;
import com.mrprona.dota2assitant.hero.service.HeroServiceImpl;
import com.mrprona.dota2assitant.item.dao.ItemDao;
import com.mrprona.dota2assitant.item.service.ItemServiceImpl;
import com.mrprona.dota2assitant.news.service.NewsService;
import com.mrprona.dota2assitant.news.service.NewsServiceImpl;

import java.util.ArrayList;
import java.util.List;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;

/**
 * User: ABadretdinov
 * Date: 02.04.14
 * Time: 10:51
 */
public class BeanContainer implements InitializingBean {
    private static final Object MONITOR = new Object();
    private static BeanContainer instance = null;

    private RestAdapter steamRestAdapter;
    private SteamService steamService;

    private RestAdapter twitchRestAdapter;

    private RestAdapter douyuRestAdapter;

    private RestAdapter trackdotaRestAdapter;


    private NewsServiceImpl newsService;


    private TI4ServiceImpl ti4Service;


    private HeroServiceImpl heroService;
    private HeroDao heroDao;
    private TalentDao talentDao;

    private HeroStatsDao heroStatsDao;
    private AbilityDao abilityDao;

    private ItemServiceImpl itemService;
    private ItemDao itemDao;

    private LocalUpdateService localUpdateService;

    private UpdateRemoteService updateRemoteService;
    private UpdateService updateService;

    private List<CreateTableDao> allDaos;




    public BeanContainer() {

        allDaos = new ArrayList<>();
        talentDao= new TalentDao();
        heroDao = new HeroDao();
        heroStatsDao = new HeroStatsDao();
        abilityDao = new AbilityDao();
        itemDao = new ItemDao();


        allDaos.add(talentDao);
        allDaos.add(heroDao);
        allDaos.add(heroStatsDao);
        allDaos.add(itemDao);
        allDaos.add(abilityDao);
        //todo updated_version



        localUpdateService = new LocalUpdateService();




        newsService = new NewsServiceImpl();



        ti4Service = new TI4ServiceImpl();


        heroService = new HeroServiceImpl();

        itemService = new ItemServiceImpl();


        updateRemoteService=new UpdateRemoteServiceImpl();
        updateService=new UpdateServiceImpl();

    }

    public static BeanContainer getInstance() {
        if (instance != null) {
            return instance;
        }
        synchronized (MONITOR) {
            if (instance == null) {
                instance = new BeanContainer();
            }
            instance.initialize();
        }
        return instance;
    }

    @Override
    public void initialize() {
        heroService.initialize();
        itemService.initialize();
        updateService.initialize();
    }


    public LocalUpdateService getLocalUpdateService() {
        return localUpdateService;
    }





    public NewsService getNewsService() {
        return newsService;
    }



    public TI4ServiceImpl getTi4Service() {
        return ti4Service ;
    }



    public HeroDao getHeroDao() {
        return heroDao;
    }


    public TalentDao getTalentDao() {
        return talentDao;
    }

    public HeroStatsDao getHeroStatsDao() {
        return heroStatsDao;
    }

    public ItemDao getItemDao() {
        return itemDao;
    }

    public AbilityDao getAbilityDao() {
        return abilityDao;
    }

    public List<CreateTableDao> getAllDaos() {
        return allDaos;
    }

    public HeroServiceImpl getHeroService() {
        return heroService;
    }


    public ItemServiceImpl getItemService() {
        return itemService;
    }



    public UpdateRemoteService getUpdateRemoteService() {
        return updateRemoteService;
    }

    public UpdateService getUpdateService() {
        return updateService;
    }


    public RestAdapter getTwitchRestAdapter() {
        if(twitchRestAdapter == null){
            twitchRestAdapter =new RestAdapter.Builder()
                    .setEndpoint("https://api.twitch.tv/")
                    .build();
        }
        return twitchRestAdapter;
    }

    public RestAdapter getDouyuRestAdapter(){
        if(douyuRestAdapter==null){
            douyuRestAdapter=new RestAdapter.Builder()
                    .setEndpoint("http://capi.douyucdn.cn/api/v1/searchNew/")
                    .build();
        }
        return douyuRestAdapter;
    }

    public RestAdapter getSteamRestAdapter(){
        if(steamRestAdapter == null){
            steamRestAdapter =new RestAdapter.Builder()
                    .setEndpoint("http://api.steampowered.com/")
                    .setRequestInterceptor(new SteamRequestInterceptor())
                    .build();
        }
        return steamRestAdapter;
    }

    public RestAdapter getTrackdotaRestAdapter(){
        if(trackdotaRestAdapter==null){
            trackdotaRestAdapter=new RestAdapter.Builder()
                    .setEndpoint("http://www.trackdota.com/data/")
                    .build();
        }
        return trackdotaRestAdapter;
    }

    public SteamService getSteamService(){
        if(steamService==null){
            steamService=getSteamRestAdapter().create(SteamService.class);
        }
        return steamService;
    }


    private class SteamRequestInterceptor implements RequestInterceptor{
        @Override
        public void intercept(RequestFacade request) {
            request.addQueryParam("key","54E61DBFB0A2D4A1B24B4C7EC5C5EFFD");
        }
    }
}
