package com.mrprona.brawlassistant.hero.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.mrprona.brawlassistant.base.dao.GeneralDaoImpl;
import com.mrprona.brawlassistant.hero.api.Hero;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ABadretdinov
 * 25.12.2014
 * 11:30
 */
public class HeroDao extends GeneralDaoImpl<Hero> {

    public static final String TABLE_NAME = "hero";

    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_LOCALIZED_NAME = "localized_name";

    public static final String COLUMN_TIER = "tier";

    /*
    * 0- id
    * 1- name
    * 2- 25left
    * 3- 20left
    * 4- 15left
    * 5- 10left
    * 6- 25right
    * 7- 20right
    * 8- 15right
    * 9- 10right
    * */
    private static final String CREATE_TABLE_QUERY = "( "
            + COLUMN_ID + " integer primary key, "
            + COLUMN_NAME + " text default null,"
            + COLUMN_LOCALIZED_NAME + " text default null,"
            + COLUMN_TIER + " text default null);";
    private static final String[] ALL_COLUMNS = {
            COLUMN_ID,
            COLUMN_NAME,
            COLUMN_LOCALIZED_NAME,
            COLUMN_TIER
    };

    @Override
    protected String getNoTableNameDataBaseCreateQuery() {
        return CREATE_TABLE_QUERY;
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public String[] getAllColumns() {
        return ALL_COLUMNS;
    }

    @Override
    public Hero cursorToEntity(Cursor cursor, int index) {
        Hero entity = new Hero();
        int i = index;
        entity.setId(cursor.getLong(i));
        i++;
        entity.setName(cursor.getString(i));
        i++;
        entity.setLocalizedName(cursor.getString(i));
        i++;
        entity.setTier(cursor.getString(i));
        return entity;
    }

    @Override
    protected ContentValues entityToContentValues(Hero entity) {
        ContentValues values = super.entityToContentValues(entity);
        if (!TextUtils.isEmpty(entity.getName())) {
            values.put(COLUMN_NAME, entity.getName());
        } else {
            values.putNull(COLUMN_NAME);
        }
        if (!TextUtils.isEmpty(entity.getLocalizedName())) {
            values.put(COLUMN_LOCALIZED_NAME, entity.getLocalizedName());
        } else {
            values.putNull(COLUMN_LOCALIZED_NAME);
        }
        if (!TextUtils.isEmpty(entity.getTier())) {
            values.put(COLUMN_TIER, entity.getTier());
        } else {
            values.putNull(COLUMN_TIER);
        }
        return values;
    }

    @Override
    public String getDefaultOrderColumns() {
        return COLUMN_LOCALIZED_NAME;
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("drop table if exists " + getTableName());
        onCreate(database);
    }


    public Hero getExactEntity(SQLiteDatabase database, String name) {
        if (TextUtils.isEmpty(name)) {
            return null;
        }
        String lower = name.toLowerCase();
        Cursor cursor = database.query(getTableName(), getAllColumns(), COLUMN_NAME + " like ? or " + COLUMN_LOCALIZED_NAME + " like ?", new String[]{lower, lower}, null, null, getDefaultOrderColumns());
        try {
            Hero entity = null;
            if (cursor.moveToFirst()) {
                entity = cursorToEntity(cursor, 0);
            }
            return entity;
        } finally {
            cursor.close();
        }
    }

    public List<Hero> getEntities(SQLiteDatabase database, String name) {
        if (TextUtils.isEmpty(name)) {
            return getAllEntities(database);
        }
        String lower = "%" + name.toLowerCase() + "%";
        Cursor cursor = database.query(getTableName(), getAllColumns(), COLUMN_NAME + " like ? or " + COLUMN_LOCALIZED_NAME + " like ?", new String[]{lower, lower}, null, null, getDefaultOrderColumns());
        try {
            List<Hero> entities = new ArrayList<Hero>(cursor.getCount());
            if (cursor.moveToFirst()) {
                do {
                    Hero entity = cursorToEntity(cursor, 0);
                    entities.add(entity);
                } while (cursor.moveToNext());
            }
            return entities;
        } finally {
            cursor.close();
        }
    }
}
