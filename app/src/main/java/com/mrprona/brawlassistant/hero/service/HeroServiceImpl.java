package com.mrprona.brawlassistant.hero.service;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.mrprona.brawlassistant.BeanContainer;
import com.mrprona.brawlassistant.base.dao.DatabaseManager;
import com.mrprona.brawlassistant.hero.api.CarouselHero;
import com.mrprona.brawlassistant.hero.api.Hero;
import com.mrprona.brawlassistant.hero.api.HeroStats;
import com.mrprona.brawlassistant.hero.api.TalentTree;
import com.mrprona.brawlassistant.hero.api.abilities.Ability;
import com.mrprona.brawlassistant.hero.dao.AbilityDao;
import com.mrprona.brawlassistant.hero.dao.HeroDao;
import com.mrprona.brawlassistant.hero.dao.HeroStatsDao;
import com.mrprona.brawlassistant.hero.dao.TalentDao;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import roboguice.util.temp.Strings;

/**
 * Created by ABadretdinov
 * 25.12.2014
 * 14:35
 */
public class HeroServiceImpl implements HeroService {
    private HeroDao heroDao;
    private HeroStatsDao heroStatsDao;
    private AbilityDao abilityDao;
    private TalentDao talentDao;

    @Override
    public void initialize() {
        BeanContainer beanContainer = BeanContainer.getInstance();
        heroDao = beanContainer.getHeroDao();
        heroStatsDao = beanContainer.getHeroStatsDao();
        abilityDao = beanContainer.getAbilityDao();
        talentDao = beanContainer.getTalentDao();
    }

    @Override
    public List<Hero> getAllHeroes(Context context) {
        DatabaseManager manager = DatabaseManager.getInstance(context);
        SQLiteDatabase database = manager.openDatabase();
        try {
            return heroDao.getAllEntities(database);
        } finally {
            manager.closeDatabase();
        }
    }

    @Override
    public TalentTree getTalentTreeByID(Context context, int id) {
        DatabaseManager manager = DatabaseManager.getInstance(context);
        SQLiteDatabase database = manager.openDatabase();
        try {
            return talentDao.getShortTalentsTree(database, id);
        } finally {
            manager.closeDatabase();
        }
    }

    @Override
    public Hero.List getFilteredHeroes(Context context, String filter) {
        DatabaseManager manager = DatabaseManager.getInstance(context);
        SQLiteDatabase database = manager.openDatabase();
        try {
            List<Hero> heroes = heroDao.getAllEntities(database);
            Iterator<Hero> iterator = heroes.iterator();
            if (!StringUtils.isEmpty(filter)) {
                while (iterator.hasNext()) {
                    Hero hero = iterator.next();
                    if (hero.getTier() != null) {
                        boolean found = false;
                        if (hero.getTier().equalsIgnoreCase(filter)) {
                            found = true;
                        }
                        if (!found) {
                            iterator.remove();
                        }
                    } else {
                        iterator.remove();
                    }
                }
            }
            return new Hero.List(heroes);
        } finally {
            manager.closeDatabase();
        }
    }

    @Override
    public List<Hero> getHeroesByName(Context context, String name) {
        DatabaseManager manager = DatabaseManager.getInstance(context);
        SQLiteDatabase database = manager.openDatabase();
        try {
            return heroDao.getEntities(database, name);
        } finally {
            manager.closeDatabase();
        }
    }

    @Override
    public Hero getExactHeroByName(Context context, String name) {
        DatabaseManager manager = DatabaseManager.getInstance(context);
        SQLiteDatabase database = manager.openDatabase();
        try {
            return heroDao.getExactEntity(database, name);
        } finally {
            manager.closeDatabase();
        }
    }

    @Override
    public List<CarouselHero> getCarouselHeroes(Context context, String filter) {
        DatabaseManager manager = DatabaseManager.getInstance(context);
        SQLiteDatabase database = manager.openDatabase();
        try {
            List<CarouselHero> carouselHeroes = new ArrayList<>();
            List<Hero> heroes = heroDao.getAllEntities(database);
            for (Hero hero : heroes) {
                CarouselHero carouselHero = new CarouselHero(hero);
                if (!Strings.isEmpty(hero)) {
                    if (hero.getTier().equalsIgnoreCase(filter)) {
                        carouselHeroes.add(carouselHero);
                    }
                } else {
                    carouselHeroes.add(carouselHero);
                }
            }
            return carouselHeroes;
        } finally {
            manager.closeDatabase();
        }
    }

    @Override
    public CarouselHero.List getCarouselHeroes(Context context, String filter, String name) {
        DatabaseManager manager = DatabaseManager.getInstance(context);
        SQLiteDatabase database = manager.openDatabase();
        try {
            CarouselHero.List carouselHeroes = new CarouselHero.List();
            List<Hero> heroes = heroDao.getEntities(database, name);
            for (Hero hero : heroes) {
                CarouselHero carouselHero = new CarouselHero(hero);
                HeroStats heroStats = heroStatsDao.getShortHeroStats(database, hero.getId());
                if (heroStats != null) {
                    carouselHero.setPrimaryStat(heroStats.getPrimaryStat());
                    if (TextUtils.isEmpty(filter)) {
                        carouselHero.setSkills(getStringAbilities(context, carouselHero.getId()));
                    }
                    if (heroStats.getRoles() != null) {
                        boolean found = filter == null;
                        for (int i = 0, size = heroStats.getRoles().length; !found && i < size; i++) {
                            String role = heroStats.getRoles()[i];
                            if (role.equals(filter)) {
                                found = true;
                            }
                        }
                        if (found) {
                            List<String> stringAbilities = getStringAbilitiesByHero(context, hero.getId());
                            if (stringAbilities != null && stringAbilities.size() > 0) {
                                carouselHero.setSkills(stringAbilities.toArray(new String[stringAbilities.size()]));
                            }
                            carouselHeroes.add(carouselHero);
                        }
                    } else if (filter == null) {
                        List<String> stringAbilities = getStringAbilitiesByHero(context, hero.getId());
                        if (stringAbilities != null && stringAbilities.size() > 0) {
                            carouselHero.setSkills(stringAbilities.toArray(new String[stringAbilities.size()]));
                        }
                        carouselHeroes.add(carouselHero);
                    }
                }
            }
            return carouselHeroes;
        } finally {
            manager.closeDatabase();
        }
    }

    private String[] getStringAbilities(Context context, long heroId) {
        List<String> stringAbilities = getStringAbilitiesByHero(context, heroId);
        if (stringAbilities != null && stringAbilities.size() > 0) {
            return stringAbilities.toArray(new String[stringAbilities.size()]);
        }
        return null;
    }

    private List<String> getStringAbilitiesByHero(Context context, long heroId) {
        DatabaseManager manager = DatabaseManager.getInstance(context);
        SQLiteDatabase database = manager.openDatabase();
        try {
            return abilityDao.getStringAbilities(database, heroId);
        } finally {
            manager.closeDatabase();
        }
    }

    @Override
    public Hero getHeroById(Context context, long id) {
        DatabaseManager manager = DatabaseManager.getInstance(context);
        SQLiteDatabase database = manager.openDatabase();
        try {
            return heroDao.getById(database, id);
        } finally {
            manager.closeDatabase();
        }
    }

    @Override
    public Hero getHeroWithStatsById(Context context, long id) {
        DatabaseManager manager = DatabaseManager.getInstance(context);
        SQLiteDatabase database = manager.openDatabase();
        try {
            Hero hero = heroDao.getById(database, id);
            if (hero != null) {
            }
            return hero;
        } finally {
            manager.closeDatabase();
        }
    }

    @Override
    public void saveHero(Context context, Hero hero) {
        DatabaseManager manager = DatabaseManager.getInstance(context);
        SQLiteDatabase database = manager.openDatabase();
        try {
            heroDao.saveOrUpdate(database, hero);
        } finally {
            manager.closeDatabase();
        }
    }

    @Override
    public List<Ability> getHeroAbilities(Context context, long heroId) {
        DatabaseManager manager = DatabaseManager.getInstance(context);
        SQLiteDatabase database = manager.openDatabase();
        try {
            List<Ability> abilities = abilityDao.getEntities(database, heroId);

            Ability ability = new Ability();
            ability.setId(5002);
            ability.setName("attribute_bonus");
            abilities.add(ability);
            return abilities;
        } finally {
            manager.closeDatabase();
        }
    }

    @Override
    public List<Ability> getNotThisHeroAbilities(Context context, long heroId) {
        DatabaseManager manager = DatabaseManager.getInstance(context);
        SQLiteDatabase database = manager.openDatabase();
        try {
            return abilityDao.getNotThisHeroEntities(database, heroId);
        } finally {
            manager.closeDatabase();
        }
    }

    @Override
    public List<Ability> getAbilitiesByList(Context context, List<Long> inGameList) {
        DatabaseManager manager = DatabaseManager.getInstance(context);
        SQLiteDatabase database = manager.openDatabase();
        try {
            List<Ability> abilities = abilityDao.getEntitiesByList(database, inGameList);

            Ability ability = new Ability();
            ability.setId(5002);
            ability.setName("attribute_bonus");
            if (!abilities.contains(ability)) {
                abilities.add(ability);
            }
            return abilities;
        } finally {
            manager.closeDatabase();
        }
    }

    @Override
    public String getAbilityPath(Context context, long id) {
        DatabaseManager manager = DatabaseManager.getInstance(context);
        SQLiteDatabase database = manager.openDatabase();
        try {
            Ability ability = abilityDao.getById(database, id);
            if (ability != null) {
                return "skills/" + ability.getName() + ".png";
            }
            return null;
        } finally {
            manager.closeDatabase();
        }
    }

    @Override
    public void saveAbility(Context context, Ability ability) {
        DatabaseManager manager = DatabaseManager.getInstance(context);
        SQLiteDatabase database = manager.openDatabase();
        try {
            abilityDao.saveOrUpdate(database, ability);
        } finally {
            manager.closeDatabase();
        }
    }

    @Override
    public void saveTalentsTree(Context context, List<TalentTree> talentTrees) {
        DatabaseManager manager = DatabaseManager.getInstance(context);
        for (TalentTree talentTree : talentTrees) {
            SQLiteDatabase database = manager.openDatabase();
            try {
            } finally {
                manager.closeDatabase();
            }
        }
    }
}