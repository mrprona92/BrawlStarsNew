package com.mrprona.brawlassistant.hero.task;

import android.content.Context;

import com.mrprona.brawlassistant.R;
import com.mrprona.brawlassistant.base.service.TaskRequest;
import com.mrprona.brawlassistant.base.util.FileUtils;
import com.mrprona.brawlassistant.hero.api.Skill;
import com.google.gson.Gson;

/**
 * Created by ABadretdinov
 * 20.08.2015
 * 16:38
 */
public class SkillsLoadRequest extends TaskRequest<Skill.List> {

    private Context mContext;
    private String mHeroDotaId;

    public SkillsLoadRequest(Context context, String heroDotaId) {
        super(Skill.List.class);
        this.mContext = context;
        this.mHeroDotaId = heroDotaId;
    }

    @Override
    public Skill.List loadData() throws Exception {
        String locale = mContext.getString(R.string.language);

        String json = FileUtils
                .getTextFromAsset(mContext, "heroes/" + mHeroDotaId + "/skills_" + locale + ".json");
        return new Gson().fromJson(json, Skill.List.class);
    }
}
