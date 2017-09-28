package com.mrprona.brawlassistant.hero.task;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mrprona.brawlassistant.base.service.TaskRequest;
import com.mrprona.brawlassistant.base.util.FileUtils;
import com.util.responses.HeroInfo;

import java.io.File;
import java.lang.reflect.Type;
import java.util.Collection;

/**
 * Created by ABadretdinov
 * 20.08.2015
 * 15:15
 */
public class HeroInfoLoadRequest extends TaskRequest<HeroInfo> {

    private Context mContext;
    private String mHeroDotaId;

    public HeroInfoLoadRequest(Context context, String heroDotaId) {
        super(HeroInfo.class);
        this.mContext = context;
        this.mHeroDotaId = heroDotaId;
    }

    @Override
    public HeroInfo loadData() throws Exception {
        String responsesEntity = FileUtils.getTextFromAsset(mContext,
                "heroes"
                        + File.separator + mHeroDotaId.toLowerCase()
                        + File.separator + "responses.json");

        Type collectionType = new TypeToken<Collection<HeroInfo>>() {}.getType();
        Collection<HeroInfo> enums = new Gson().fromJson(responsesEntity, collectionType);
        HeroInfo[] listHero = enums.toArray(new HeroInfo[enums.size()]);
        return listHero[0];
    }
}
