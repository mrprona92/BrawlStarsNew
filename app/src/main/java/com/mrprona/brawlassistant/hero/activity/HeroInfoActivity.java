package com.mrprona.brawlassistant.hero.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.chartboost.sdk.Chartboost;
import com.mrprona.brawlassistant.BeanContainer;
import com.mrprona.brawlassistant.R;
import com.mrprona.brawlassistant.base.activity.BaseActivity;
import com.mrprona.brawlassistant.base.util.FileUtils;
import com.mrprona.brawlassistant.hero.adapter.pager.HeroPagerAdapter;
import com.mrprona.brawlassistant.hero.api.Hero;
import com.mrprona.brawlassistant.hero.service.HeroService;
import com.mrprona.brawlassistant.hero.task.HeroInfoLoadRequest;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.UncachedSpiceService;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.util.responses.HeroInfo;

import butterknife.ButterKnife;

/**
 * User: ABadretdinov
 * Date: 02.09.13
 * Time: 13:24
 */
public class HeroInfoActivity extends BaseActivity implements RequestListener<HeroInfo> {
    private Hero hero;
    private HeroInfo mHeroInfo;
    private SpiceManager mSpiceManager = new SpiceManager(UncachedSpiceService.class);


    @Override
    public void onStart() {
        ButterKnife.bind(this);
        if (!mSpiceManager.isStarted()) {
            mSpiceManager.start(this);
            loadHeroInfoData(this);
        }
        super.onStart();
    }


    @Override
    public void onDestroy() {
        if (mSpiceManager.isStarted()) {
            mSpiceManager.shouldStop();
        }
        Chartboost.onDestroy(this);
        super.onDestroy();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    /*    MenuItem guides = menu.add(1, 1001, 1, R.string.guides);
        MenuItemCompat.setShowAsAction(guides, MenuItemCompat.SHOW_AS_ACTION_ALWAYS | MenuItemCompat.SHOW_AS_ACTION_WITH_TEXT);*/
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case 1001:
                Intent intent = new Intent(this, GuideActivity.class);
                intent.putExtra("id", hero.getId());
                startActivity(intent);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hero_info);


        Bundle intent = getIntent().getExtras();
        if (intent != null && intent.containsKey("id")) {
            HeroService heroService = BeanContainer.getInstance().getHeroService();
            hero = heroService.getHeroWithStatsById(this, intent.getLong("id"));

            final TypedArray styledAttributes = getTheme()
                    .obtainStyledAttributes(new int[]{R.attr.actionBarSize});
            int mActionBarSize = (int) styledAttributes.getDimension(0, 40) / 2;
            styledAttributes.recycle();
            Bitmap icon = FileUtils.getBitmapFromAsset(this, "heroes/" + hero.getDotaId() + "/mini.png");
            if (icon != null) {
                icon = Bitmap.createScaledBitmap(icon, mActionBarSize, mActionBarSize, false);
                Drawable iconDrawable = new BitmapDrawable(getResources(), icon);
                mToolbar.setNavigationIcon(iconDrawable);
            }
            getSupportActionBar().setTitle(hero.getLocalizedName());
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        Chartboost.onPause(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        Chartboost.onResume(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        Chartboost.onStop(this);
    }


    @Override
    public void onRequestFailure(SpiceException spiceException) {
        //do nothing
    }

    @Override
    public void onRequestSuccess(HeroInfo mHeroInfo) {
        this.mHeroInfo = mHeroInfo;
        FragmentStatePagerAdapter adapter = new HeroPagerAdapter(getSupportFragmentManager(), this, hero, mHeroInfo);

        final ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);
        pager.setOffscreenPageLimit(1);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(pager);
    }


    private void loadHeroInfoData(Context context) {
        mSpiceManager.execute(new HeroInfoLoadRequest(context.getApplicationContext(), hero.getLocalizedName()), this);
    }

}
