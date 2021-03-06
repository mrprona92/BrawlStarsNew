package com.mrprona.brawlassistant.base.task;

import android.content.Context;
import android.content.res.AssetManager;

import com.mrprona.brawlassistant.BeanContainer;
import com.mrprona.brawlassistant.base.dao.DatabaseManager;
import com.mrprona.brawlassistant.base.service.LocalUpdateService;
import com.mrprona.brawlassistant.base.service.TaskRequest;
import com.mrprona.brawlassistant.base.util.FileUtils;

import java.io.File;

/**
 * Created by ABadretdinov
 * 20.08.2015
 * 16:59
 */
public class UpdateLoadRequest extends TaskRequest<String> {

    private Context mContext;

    public UpdateLoadRequest(Context context) {
        super(String.class);
        this.mContext = context;
    }

    @Override
    public String loadData() throws Exception {
        DatabaseManager manager = DatabaseManager.getInstance(mContext);

        LocalUpdateService localUpdateService = BeanContainer.getInstance().getLocalUpdateService();
        AssetManager assetManager = mContext.getAssets();
        String[] files = assetManager.list("updates");
        for (String fileName : files) {
            int fileVersion = Integer.valueOf(fileName.split("\\.")[0]);
            String sql = FileUtils.getTextFromAsset(mContext, "updates" + File.separator + fileName);
            localUpdateService.update(mContext, sql, fileVersion);
        }
        return "";
    }
}
