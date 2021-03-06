package com.mrprona.brawlassistant.base.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mrprona.brawlassistant.BeanContainer;
import com.mrprona.brawlassistant.hero.api.HeroStats;
import com.mrprona.brawlassistant.hero.dao.AbilityDao;
import com.mrprona.brawlassistant.hero.dao.HeroDao;
import com.mrprona.brawlassistant.hero.dao.HeroStatsDao;
import com.mrprona.brawlassistant.item.dao.ItemDao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: ABadretdinov
 * Date: 29.08.13
 * Time: 11:07
 */
public class Helper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "brawlstars.db";
    public static final int DATABASE_VERSION = 4;
    private Context mContext;

    /*public static final String CREATE_ITEMS_FROM="create table if not exists "+
            " items_from ( _id integer PRIMARY KEY AUTOINCREMENT, item_id integer not null, need_id integer not null);";*/

    public Helper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        List<CreateTableDao> allDaos = BeanContainer.getInstance().getAllDaos();
        for (CreateTableDao dao : allDaos) {
            dao.onCreate(db);
        }
        db.execSQL("create table if not exists updated_version(version integer not null);");
        db.execSQL("insert into updated_version (version) values(0);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < DATABASE_VERSION) {
            reinitHeroesAndItems(db);
        }
     /*   List<CreateTableDao> allDaos = BeanContainer.getInstance().getAllDaos();
        for (CreateTableDao dao : allDaos) {
            dao.onUpgrade(db, oldVersion, newVersion);
        }*/
        onCreate(db);
    }

    private void reinitHeroesAndItems(SQLiteDatabase db) {
        db.execSQL("drop table " + ItemDao.TABLE_NAME);
        db.execSQL("drop table " + ItemDao.ITEMS_FROM_MAPPER_TABLE_NAME);
        db.execSQL("drop table " + HeroDao.TABLE_NAME);
        db.execSQL("drop table " + HeroStatsDao.TABLE_NAME);
        db.execSQL("drop table " + AbilityDao.TABLE_NAME);
        db.execSQL("update updated_version set version=0;");
    }

    public Map<Long, Integer> getAllStatsHero() {
        Map<Long, Integer> statsList = new HashMap<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + HeroStatsDao.TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                HeroStats heroStats = new HeroStats();
                heroStats.setId(cursor.getInt(0));
                heroStats.setPrimaryStat(cursor.getInt(20));
                // Adding contact to list
                statsList.put(heroStats.getId(), heroStats.getPrimaryStat());
            } while (cursor.moveToNext());
        }
        // return contact list
        return statsList;
    }
}