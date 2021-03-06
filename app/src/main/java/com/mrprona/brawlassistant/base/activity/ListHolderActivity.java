package com.mrprona.brawlassistant.base.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.chartboost.sdk.Chartboost;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.mrprona.brawlassistant.BeanContainer;
import com.mrprona.brawlassistant.R;
import com.mrprona.brawlassistant.base.configs.ScreenIDs;
import com.mrprona.brawlassistant.base.dao.DatabaseManager;
import com.mrprona.brawlassistant.base.fragment.ConfirmDialog;
import com.mrprona.brawlassistant.base.fragment.SCAlertDialog;
import com.mrprona.brawlassistant.base.fragment.SCBaseFragment;
import com.mrprona.brawlassistant.base.fragment.SearchableFragment;
import com.mrprona.brawlassistant.base.menu.fragment.MenuFragment;
import com.mrprona.brawlassistant.base.service.LocalSpiceService;
import com.mrprona.brawlassistant.base.util.AppRater;
import com.mrprona.brawlassistant.base.util.UpdateUtils;
import com.mrprona.brawlassistant.hero.fragment.HeroesList;
import com.mrprona.brawlassistant.hero.service.HeroService;
import com.mrprona.brawlassistant.item.fragment.ItemsList;
import com.mrprona.brawlassistant.news.fragment.NewsList;
import com.octo.android.robospice.SpiceManager;

import java.util.Map;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

//import android.support.v7.widget.ActionMenuPresenter;

/**
 * User: ABadretdinov
 * Date: 15.01.14
 * Time: 14:27
 */
public class ListHolderActivity extends BaseActivity implements SearchView.OnQueryTextListener, AdapterView.OnItemSelectedListener{
    int lastSelected = -1;

    HeroService heroService = BeanContainer.getInstance().getHeroService();
    private SpiceManager mSpiceManager = new SpiceManager(LocalSpiceService.class);
    private boolean doubleBackToExitPressedOnce = false;
    protected final String TAG = getClass().getSimpleName();
    private Spinner navSpinner;

    public static int CODE_WRITE_SETTINGS_PERMISSION = 1001;

    @BindView(R.id.tabHero)
    LinearLayout tabHero;

    @BindView(R.id.tabConterPick)
    LinearLayout tabCounterPick;

    @BindView(R.id.tabQuiz)
    LinearLayout tabQuiz;

    @BindView(R.id.tabMenu)
    LinearLayout tabMenu;

    @BindView(R.id.lblTabHero)
    TextView lblTabHero;

    @BindView(R.id.lblTabCounterPick)
    TextView lblTabCounter;

    @BindView(R.id.lblTabQuiz)
    TextView lblTabQuiz;

    @BindView(R.id.lblTabMenu)
    TextView lblTabMenu;

    @Nullable
    @BindView(R.id.btnBack)
    ImageView btnBack;

    @Nullable
    @BindView(R.id.lblToolbarTitle)
    TextView lblToolbarTitle;


    private ScreenIDs.ScreenTab mCurrentTab;

    private SCBaseFragment mCurrentFragment;

    @BindColor(R.color.cmn_white)
    int tabHighLightTextColor;


    @BindColor(R.color.ranking_bgr_row)
    int tabNormalTextColor;

    private FragmentManager mFragmentManager;

    private static Context appContext;


    private void initControl() {
        mFragmentManager = getSupportFragmentManager();
        mFragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                Log.e(TAG, "onBackStackChanged() called");
                Fragment fr = mFragmentManager.findFragmentById(R.id.details);
                if (fr != null) {
                    mCurrentFragment = (SCBaseFragment) fr;
                    updateUI();
                    Log.e(TAG, "Current fragment = " + fr.getClass().getSimpleName());
                }
            }
        });
    }


    public static Map<Long, Integer> mMapTypeHero;


    public static Map<Long, Integer> getMapTypeHero() {
        return mMapTypeHero;
    }

    @Override
    protected void onStart() {
        ButterKnife.bind(this);
        Log.d("BINH", "onStart() called");

        mMapTypeHero = DatabaseManager.getInstance(this).mHelper.getAllStatsHero();
        /*
        if (!mSpiceManager.isStarted()) {
            mSpiceManager.start(getApplicationContext());
            final int currentVersion = localUpdateService.getVersion(this);
            if (currentVersion != Helper.DATABASE_VERSION) {
                mSpiceManager.execute(new UpdateLoadRequest(getApplicationContext()), this);
            }
        }*/

    Chartboost.onStart(this);
    super.onStart();
}


    @Override
    protected void onResume() {
        super.onResume();
        Chartboost.onResume(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Chartboost.onStop(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Chartboost.onDestroy(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate() called with: " + "savedInstanceState = [" + this.getDatabasePath("dota2.db") + "]");
        setTheme(R.style.Infodota);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_fragment_holder);

        //mActionMenuView.setPresenter(new ActionMenuPresenter(this));

        FacebookSdk.sdkInitialize(getApplicationContext());

        // Initialize the Mobile Ads SDK.§

        //MobileAds.initialize(this, getString(R.string.banner_ad_unit_id));


        Chartboost.startWithAppId(this, getString(R.string.appIDChartboost), getString(R.string.appSignature));
        Chartboost.onCreate(this);

        AppEventsLogger.activateApp(this);

       // MenuFragment.updateActivity(this);

        Log.d(TAG, "onCreate() called with: " + "savedInstanceState = [" + savedInstanceState + "]");

        ActionBar bar = getSupportActionBar();
        bar.setDisplayShowTitleEnabled(false);
        bar.setDisplayShowHomeEnabled(false);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.edit().remove("mainMenuLastSelected").commit();

        // BinhTran comment 23/03 for calling redundant
        // UpdateUtils.checkNewVersion(this, false);


        navSpinner = (Spinner) mToolbar.findViewById(R.id.nav_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.spinner_item, getResources().getStringArray(R.array.main_menu));
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        navSpinner.setAdapter(adapter);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        int selected = prefs.getInt("mainMenuLastSelected", 0);

        navSpinner.setOnItemSelectedListener(this);
        navSpinner.setSelection(Math.min(selected, adapter.getCount() - 1));
        // navSpinner.setVisibility(View.GONE);

        initControl();

        UpdateUtils.checkNewVersion(this, false);

        appContext = this;

        AppRater.showRate(appContext);

       /* NativeExpressAdView adView = (NativeExpressAdView)findViewById(R.id.adView);

        AdRequest request = new AdRequest.Builder()
                .build();
        adView.loadAd(request);*/

        //не нужен AppRater.onAppLaunched(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        /*if (mCurrentFragment instanceof MenuFragment) {
            navSpinner.setVisibility(View.VISIBLE);
        } else {*/
        navSpinner.setVisibility(View.GONE);
        //}

        getMenuInflater().inflate(R.menu.search, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setQueryHint(getString(android.R.string.search_go));
        searchView.setOnQueryTextListener(this);

        return super.onCreateOptionsMenu(menu);
    }

    //public boolean isSearchViewVisible=false;
    public boolean onQueryTextChange(String textNew) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.details);
        if (fragment instanceof SearchableFragment) {
            ((SearchableFragment) fragment).onTextSearching(textNew);
        }

        return true;
    }

    public boolean onQueryTextSubmit(String text) {

        return true;
    }


    @Override
    public void onBackPressed() {
        // If an interstitial is on screen, close it.
        if (Chartboost.onBackPressed())
            return;

        /*if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        doubleBackToExitPressedOnce = true;
        Toast.makeText(this, getString(R.string.back_toast), Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, Constants.MILLIS_FOR_EXIT);*/
        if (mFragmentManager.getBackStackEntryCount() >= 1) {
            Log.d(TAG, "onBackPressed() called. More than 0 fragment in back stack");
            clearBackStack();
            openScreen(ScreenIDs.ScreenTab.MENU, MenuFragment.class, null, false, false);
            return;
        } else {
            Log.d(TAG, "onBackPressed() called. Finish()");
            ConfirmDialog confirmDialog = new ConfirmDialog(ListHolderActivity.this);
            confirmDialog.setMessageId(R.string.ask_quit);
            confirmDialog.setTitleId(R.string.app_name);

            confirmDialog.setOnConfirmDialogListener(new ConfirmDialog.ConfirmDialogListener() {
                @Override
                public void onSelect(int indexButton) {
                    if (indexButton == 1) {
                        ListHolderActivity.super.onBackPressed();
                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(1);
                    }
                }
            });
            confirmDialog.show();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (!getResources().getBoolean(R.bool.is_tablet)) {
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                ((ViewGroup) mActionMenuView.getParent()).removeView(mActionMenuView);
                ((ViewGroup) findViewById(R.id.top_container)).addView(mActionMenuView);
                ((LinearLayout.LayoutParams) mActionMenuView.getLayoutParams()).weight = 1;
                ((LinearLayout.LayoutParams) mToolbar.getLayoutParams()).weight = 1;
            } else {
                ((ViewGroup) mActionMenuView.getParent()).removeView(mActionMenuView);
                ((ViewGroup) findViewById(R.id.main_view)).addView(mActionMenuView);
                ((LinearLayout.LayoutParams) mActionMenuView.getLayoutParams()).weight = 0;
                ((LinearLayout.LayoutParams) mToolbar.getLayoutParams()).weight = 0;
            }
        }
        mActionMenuView.onConfigurationChanged(newConfig);
        super.onConfigurationChanged(newConfig);
    }

    public void replaceFragment(Fragment details) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.details, details);
        ft.commitAllowingStateLoss();
    }


       @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            default:
            case 0:
                mCurrentFragment = new MenuFragment();
                break;
            case 1:
                mCurrentFragment = new ItemsList();
                break;

            case 7:
                // mCurrentFragment = new NewsList();
                break;
                              /*
                case 9:
					details=new LeaguesGamesList.newInstance("&c2=7057&c1=2390");
                    break;*/
        }
        replaceFragment(mCurrentFragment);
        lastSelected = position;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.edit().putInt("mainMenuLastSelected", lastSelected).commit();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    //Tab Change
    @OnClick(R.id.tabHero)
    public void onClickTabHero() {
        clearBackStack();
        openScreen(ScreenIDs.ScreenTab.LIVE, MenuFragment.class, null, true, false);
    }

    @OnClick(R.id.tabConterPick)
    public void onClickTabCounterPick() {
        clearBackStack();
        openScreen(ScreenIDs.ScreenTab.HERO, MenuFragment.class, null, true, false);
    }

    @OnClick(R.id.tabQuiz)
    public void onClickTabQuiz() {
        clearBackStack();
        openScreen(ScreenIDs.ScreenTab.HERO, MenuFragment.class, null, true, false);
    }

    @OnClick(R.id.tabMenu)
    public void onClickTabMenu() {
        clearBackStack();
        openScreen(ScreenIDs.ScreenTab.MENU, MenuFragment.class, null, true, false);
    }


    private void setHighLightTab(ScreenIDs.ScreenTab tab) {
        android.util.Log.d(TAG, "setHighLightTab() called with: tab = [" + tab + "]");
        tabHero.setBackgroundResource(R.color.tabbar_normal);
        tabMenu.setBackgroundResource(R.color.tabbar_normal);
        tabCounterPick.setBackgroundResource(R.color.tabbar_normal);
        tabQuiz.setBackgroundResource(R.color.tabbar_normal);

       /* lblTabRanking.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_tab_ranking, 0, 0);
        lblTabMenu.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_tab_4, 0, 0);
        lblTabTradeFeed.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_tab_2, 0, 0);
        imgTradeSetting.setImageResource(R.drawable.ic_tab_3);*/

        lblTabHero.setTextColor(tabNormalTextColor);
        lblTabCounter.setTextColor(tabNormalTextColor);
        lblTabQuiz.setTextColor(tabNormalTextColor);
        lblTabMenu.setTextColor(tabNormalTextColor);

        switch (tab) {
            case LIVE:
                tabHero.setBackgroundResource(R.color.tabbar_active);
                lblTabHero.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_tab1_live, 0, 0);
                lblTabHero.setTextColor(tabHighLightTextColor);
                break;
            case NEWS:
                tabCounterPick.setBackgroundResource(R.color.tabbar_active);
                lblTabCounter.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_tab2_counterpick, 0, 0);
                lblTabCounter.setTextColor(tabHighLightTextColor);
                break;
            case HERO:
                tabQuiz.setBackgroundResource(R.color.tabbar_active);
                lblTabQuiz.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_tab3_quiz, 0, 0);
                lblTabQuiz.setTextColor(tabHighLightTextColor);
                break;
            case MENU:
                tabMenu.setBackgroundResource(R.color.tabbar_active);
                lblTabMenu.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_tab4_menu, 0, 0);
                lblTabMenu.setTextColor(tabHighLightTextColor);
                break;
            default:
                tabHero.setBackgroundResource(R.color.tabbar_normal);
                tabCounterPick.setBackgroundResource(R.color.tabbar_normal);
                tabQuiz.setBackgroundResource(R.color.tabbar_normal);
                tabMenu.setBackgroundResource(R.color.tabbar_normal);
                break;
        }
    }

    public void onChangeFragment(int position) {
        switch (position) {
            default:
            case 0:
                //When want open link  to display
                /*Bundle bundle = new Bundle();
                bundle.putString(AgreementFragment.ARG_URL, "http://www.gosugamers.net/dota2/rankings");
                openScreen(ScreenIDs.ScreenTab.MENU, AgreementFragment.class, bundle, true, true);
                */
                openScreen(ScreenIDs.ScreenTab.MENU, HeroesList.class, null, true, true);
                break;
            case 1:
                openScreen(ScreenIDs.ScreenTab.MENU, ItemsList.class, null, true, true);
                break;
            case 2:
                //mFragmentDetails = new ItemsList();
                openScreen(ScreenIDs.ScreenTab.MENU, ItemsList.class, null, true, true);
                break;
            case 3:
                openScreen(ScreenIDs.ScreenTab.MENU, NewsList.class, null, true, true);
                break;
            case 7:
                UpdateUtils.checkNewVersion(ListHolderActivity.this, true);
                break;
            case 8:
                startActivity(new Intent(ListHolderActivity.this, AboutActivity.class));
                break;
            case 9:
                //TODO quit app
                AlertDialog alertDialog = new AlertDialog.Builder(ListHolderActivity.this).create();
                alertDialog.setTitle("Dota Assitant");
                alertDialog.setMessage("Do you want quit application?");
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(1);
                    }
                });
                alertDialog.show();
                break;
        }
        replaceFragment(mCurrentFragment);
    }


    /**
     * Returns the shared progress dialog.
     *
     * @author Binh.TH
     */
    @SuppressLint("InflateParams")
    private Dialog getProgressDialog() {
        if (mProgressDialog == null) {         // Create if null
            mProgressDialog = new Dialog(this, android.R.style.Theme_Black);
            View view = LayoutInflater.from(this).inflate(R.layout.cmn_process, null);
            mProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            mProgressDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
            mProgressDialog.setContentView(view);
        }
        return mProgressDialog;
    }


    private Dialog mProgressDialog;

    public void showProgressDialog(final String message) {
        showProgressDialog(message, false);
    }

    private int sDialogCount = 0;

    /**
     * Show progress dialog.
     *
     * @param message
     * @param cancelable
     */

    public void showProgressDialog(final String message, final boolean cancelable) {
        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Dialog dlg = getProgressDialog();
                        sDialogCount++;
                        if (!dlg.isShowing()) {
                            dlg.setCancelable(cancelable);
                            dlg.show();
                        }
                    } catch (Exception ex) {
                        // Do nothing
                    }
                }
            });
        } catch (Exception e) {
        }
    }


    /**
     * Hide progress dialog.
     */
    public void hideProgressDialog() {
        hideProgressDialog(true);
    }

    public void hideProgressDialog(final boolean isWait) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    Dialog dlg = getProgressDialog();
                    sDialogCount--;
                    if (dlg != null && dlg.isShowing()) {
                        if (!isWait) {
                            sDialogCount = 0;
                            dlg.dismiss();
                        } else {
                            if (sDialogCount <= 0) dlg.dismiss();
                        }
                    }
                } catch (Exception ex) {
                    // Do nothing
                }
            }
        });
    }


    private SCAlertDialog mAlertDialog;

    public void showAlertDialog(final int message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mAlertDialog != null && mAlertDialog.isShowing()) {
                    mAlertDialog.hide();
                }
                mAlertDialog = new SCAlertDialog(ListHolderActivity.this, message, R.string.cmn_ok);
                mAlertDialog.show();
            }
        });
    }

    public void showAlertDialog(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mAlertDialog != null && mAlertDialog.isShowing()) {
                    mAlertDialog.hide();
                }
                mAlertDialog = new SCAlertDialog(ListHolderActivity.this, message, R.string.cmn_ok);
                mAlertDialog.show();
            }
        });
    }


    public void updateUI() {
        Log.d(TAG, "updateUI: called");

        if (mCurrentFragment instanceof HeroesList) {
            lblToolbarTitle.setVisibility(View.GONE);
            mActionMenuView.setVisibility(View.GONE);
        } else {
            lblToolbarTitle.setVisibility(View.VISIBLE);
        }

        if (mCurrentFragment != null) {
            if (mCurrentFragment.getToolbarTitle() != -99) {
                if (lblToolbarTitle != null && mCurrentFragment.getToolbarTitle() > 0) {
                    lblToolbarTitle.setText(mCurrentFragment.getToolbarTitle());
                }
            } else {
                lblToolbarTitle.setText(mCurrentFragment.getToolbarTitleString());
            }
        }

        //btnBack.setVisibility(mFragmentManager.getBackStackEntryCount() >= 1 ? View.VISIBLE : GONE);

        /* if (mCurrentFragment instanceof Coun)
            setHighLightTab(ScreenIDs.ScreenTab.RANKING);
        if (mCurrentFragment instanceof TradeSettingFragment)
            setHighLightTab(ScreenIDs.ScreenTab.TRADE_SETTING);
        if (mCurrentFragment instanceof TradeStatusFragment)
            setHighLightTab(ScreenIDs.ScreenTab.TRADE_SETTING);
        if (mCurrentFragment instanceof MenuFragment) setHighLightTab(ScreenIDs.ScreenTab.MENU);*/
    }


    private void openScreen2(ScreenIDs.ScreenTab tab, Class<? extends SCBaseFragment> fragmentClass, Bundle bundles, boolean isAnimate,
                             boolean shouldAddToBackStack) {

        //if (tab != ScreenIDs.ScreenTab.NOT_HIGHLIGHT) clearBackStack();
        if (getBaseContext() == null) return;

        setHighLightTab(tab);
        this.mCurrentTab = tab;

        FragmentManager manager = getSupportFragmentManager();
        String tag = fragmentClass.getName();
        try {
            FragmentTransaction transaction = manager.beginTransaction();
            if (isAnimate)
                transaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
            //if (!manager.popBackStackImmediate(tag, 0) && manager.findFragmentByTag(tag) == null) {
            Log.e(TAG, "openScreen: fragment " + fragmentClass.getSimpleName() + " is NOT from back stack");
            mCurrentFragment = fragmentClass.newInstance();
            mCurrentFragment.setRetainInstance(true);
            if (bundles == null) bundles = new Bundle();
            mCurrentFragment.setArguments(bundles);
            if (shouldAddToBackStack) {
                transaction.addToBackStack(tag);
                Log.e(TAG, "openScreen: add " + tag + " to back stack");
            }
            transaction.replace(R.id.details, mCurrentFragment, tag);
            transaction.commitAllowingStateLoss();
//            } else {
//                Log.e(TAG, "openScreen: popped " + tag + " from back stack");
//                mCurrentFragment = (SCBaseFragment) manager.findFragmentByTag(tag);
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openScreen(final ScreenIDs.ScreenTab tab, final Class<? extends SCBaseFragment> fragmentClass, final Bundle bundles, final boolean isAnimate,
                           final boolean shouldAddToBackStack) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                openScreen2(tab, fragmentClass, bundles, isAnimate, shouldAddToBackStack);
            }
        });
    }

    public void clearBackStack() {
        Log.e(TAG, "clearBackStack() called:" + mFragmentManager.getBackStackEntryCount());
        if (mFragmentManager.getBackStackEntryCount() > 0) {
            FragmentManager.BackStackEntry first = mFragmentManager.getBackStackEntryAt(0);
            boolean didPop = mFragmentManager.popBackStackImmediate(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
            Log.d(TAG, "clearBackStack: didPop " + didPop);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public static Context getAppContext() {
        return appContext;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB) // API 11
    public void startMyTask(AsyncTask asyncTask) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        else
            asyncTask.execute();
    }

}
