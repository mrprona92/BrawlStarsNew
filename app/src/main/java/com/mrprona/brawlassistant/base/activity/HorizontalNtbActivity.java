package com.mrprona.brawlassistant.base.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.chartboost.sdk.Chartboost;
import com.mrprona.brawlassistant.R;
import com.mrprona.brawlassistant.base.configs.ScreenIDs;
import com.mrprona.brawlassistant.base.fragment.ConfirmDialog;
import com.mrprona.brawlassistant.base.fragment.SCAlertDialog;
import com.mrprona.brawlassistant.base.fragment.SCBaseFragment;
import com.mrprona.brawlassistant.base.fragment.SearchableFragment;
import com.mrprona.brawlassistant.base.menu.fragment.MenuFragment;
import com.mrprona.brawlassistant.base.util.AppRater;
import com.mrprona.brawlassistant.base.util.UpdateUtils;
import com.mrprona.brawlassistant.base.youtube.YoutubeFragment;
import com.mrprona.brawlassistant.hero.fragment.HeroesList;
import com.mrprona.brawlassistant.item.fragment.ItemsList;
import com.mrprona.brawlassistant.news.fragment.NewsList;
import com.mrprona.brawlassistant.news.reddit.PostsFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import devlight.io.library.ntb.NavigationTabBar;

/**
 * Created by GIGAMOLE on 28.03.2016.
 */
public class HorizontalNtbActivity extends BaseActivity implements SearchView.OnQueryTextListener, AdapterView.OnItemSelectedListener {


    @Nullable
    @BindView(R.id.btnBack)
    ImageView btnBack;

    @Nullable
    @BindView(R.id.lblToolbarTitle)
    TextView lblToolbarTitle;


    private FragmentManager mFragmentManager;

    private static Context appContext;

    private SCBaseFragment mCurrentFragment;

    protected final String TAG = getClass().getSimpleName();

    int lastSelected = -1;


    @Override
    protected void onStart() {
        ButterKnife.bind(this);
        Chartboost.onStart(this);
        super.onStart();
    }


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        setTheme(R.style.Infodota);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_horizontal_ntb);
        MenuFragment.updateActivity(this);
        initUI();

        //UpdateUtils.checkNewVersion(this, false);

        appContext = this;

        AppRater.showRate(appContext);
        initControl();

        openScreen(ScreenIDs.ScreenTab.HERO, HeroesList.class, null, true, false);
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
    }


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


    private void initUI() {
        ActionBar bar = getSupportActionBar();
        bar.setDisplayShowTitleEnabled(false);
        bar.setDisplayShowHomeEnabled(false);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.edit().remove("mainMenuLastSelected").commit();

        final String[] colors = getResources().getStringArray(R.array.default_preview);

        final NavigationTabBar navigationTabBar = (NavigationTabBar) findViewById(R.id.ntb_horizontal);
        final ArrayList<NavigationTabBar.Model> models = new ArrayList<>();
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_eighth),
                        Color.parseColor(colors[4]))
                        .selectedIcon(getResources().getDrawable(R.drawable.ic_eighth))
                        .title("Brawler")
                        .badgeTitle("777")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_menu_news),
                        Color.parseColor(colors[0]))
                        .selectedIcon(getResources().getDrawable(R.drawable.ic_menu_news))
                        .title("Reddit")
                        .badgeTitle("NTB")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_menu_twitch),
                        Color.parseColor(colors[1]))
//                        .selectedIcon(getResources().getDrawable(R.drawable.ic_eighth))
                        .title("Channel")
                        .badgeTitle("with")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_tab4_menu),
                        Color.parseColor(colors[2]))
                        .selectedIcon(getResources().getDrawable(R.drawable.ic_tab4_menu))
                        .title("Menu")
                        .badgeTitle("state")
                        .build()
        );
        navigationTabBar.setModels(models);
        navigationTabBar.setOnTabBarSelectedIndexListener(new NavigationTabBar.OnTabBarSelectedIndexListener() {
            @Override
            public void onStartTabSelected(NavigationTabBar.Model model, int index) {
                clearBackStack();
                if (index == 0) {
                    openScreen(ScreenIDs.ScreenTab.HERO, HeroesList.class, null, true, false);
                } else if (index == 1) {
                    openScreen(ScreenIDs.ScreenTab.NEWS, PostsFragment.class, null, true, false);
                } else if (index == 2) {
                    openScreen(ScreenIDs.ScreenTab.LIVE, YoutubeFragment.class, null, true, false);
                } else if (index == 3) {
                    openScreen(ScreenIDs.ScreenTab.MENU, MenuFragment.class, null, true, false);
                }
            }

            @Override
            public void onEndTabSelected(NavigationTabBar.Model model, int index) {

            }
        });


        navigationTabBar.postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < navigationTabBar.getModels().size(); i++) {
                    final NavigationTabBar.Model model = navigationTabBar.getModels().get(i);
                    navigationTabBar.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            model.showBadge();
                        }
                    }, i * 100);
                }
            }
        }, 500);


        /*navSpinner = (Spinner) mToolbar.findViewById(R.id.nav_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.spinner_item, getResources().getStringArray(R.array.main_menu));
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        navSpinner.setAdapter(adapter);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        int selected = prefs.getInt("mainMenuLastSelected", 0);

        navSpinner.setOnItemSelectedListener(this);
        navSpinner.setSelection(Math.min(selected, adapter.getCount() - 1));*/
        // navSpinner.setVisibility(View.GONE);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        return true;
    }

    //public boolean isSearchViewVisible=false;
    public boolean onQueryTextChange(String textNew) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.details);
        if (fragment instanceof SearchableFragment) {
            ((SearchableFragment) fragment).onTextSearching(textNew);
        }
        return true;
    }


    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

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
            super.onBackPressed();
            /*clearBackStack();
            openScreen(ScreenIDs.ScreenTab.MENU, MenuFragment.class, null, false, false);*/
            return;
        } else {
            Log.d(TAG, "onBackPressed() called. Finish()");
            ConfirmDialog confirmDialog = new ConfirmDialog(HorizontalNtbActivity.this);
            confirmDialog.setMessageId(R.string.ask_quit);
            confirmDialog.setTitleId(R.string.app_name);

            confirmDialog.setOnConfirmDialogListener(new ConfirmDialog.ConfirmDialogListener() {
                @Override
                public void onSelect(int indexButton) {
                    if (indexButton == 1) {
                        HorizontalNtbActivity.super.onBackPressed();
                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(1);
                    }
                }
            });
            confirmDialog.show();
        }
    }


    public void clearBackStack() {
        Log.e(TAG, "clearBackStack() called:" + mFragmentManager.getBackStackEntryCount());
        if (mFragmentManager.getBackStackEntryCount() > 0) {
            FragmentManager.BackStackEntry first = mFragmentManager.getBackStackEntryAt(0);
            boolean didPop = mFragmentManager.popBackStackImmediate(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
            Log.d(TAG, "clearBackStack: didPop " + didPop);
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

    private ScreenIDs.ScreenTab mCurrentTab;

    private void openScreen2(ScreenIDs.ScreenTab tab, Class<? extends SCBaseFragment> fragmentClass, Bundle bundles, boolean isAnimate,
                             boolean shouldAddToBackStack) {

        //if (tab != ScreenIDs.ScreenTab.NOT_HIGHLIGHT) clearBackStack();
        if (getBaseContext() == null) return;

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


    public void onChangeFragment(int position) {
        switch (position) {
            default:
            case 0:
                //When want open link  to display
                /*Bundle bundle = new Bundle();
                bundle.putString(AgreementFragment.ARG_URL, "http://www.gosugamers.net/dota2/rankings");
                openScreen(ScreenIDs.ScreenTab.MENU, AgreementFragment.class, bundle, true, true);
                */
                openScreen(ScreenIDs.ScreenTab.HERO, HeroesList.class, null, true, true);
                break;
            case 1:
                openScreen(ScreenIDs.ScreenTab.NEWS, NewsList.class, null, true, true);
                break;
            case 2:
                //mFragmentDetails = new ItemsList();
                openScreen(ScreenIDs.ScreenTab.LIVE, YoutubeFragment.class, null, true, true);
                break;
            case 3:
                openScreen(ScreenIDs.ScreenTab.MENU, MenuFragment.class, null, true, true);
                break;
            case 7:
                UpdateUtils.checkNewVersion(HorizontalNtbActivity.this, true);
                break;
            case 8:
                startActivity(new Intent(HorizontalNtbActivity.this, AboutActivity.class));
                break;
            case 9:
                //TODO quit app
                AlertDialog alertDialog = new AlertDialog.Builder(HorizontalNtbActivity.this).create();
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


    public void replaceFragment(Fragment details) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.details, details);
        ft.commitAllowingStateLoss();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        /*if (mCurrentFragment instanceof MenuFragment) {
            navSpinner.setVisibility(View.VISIBLE);
        } else {*/
        //}
        if (mCurrentFragment instanceof HeroesList) {
            getMenuInflater().inflate(R.menu.search, menu);
            MenuItem searchItem = menu.findItem(R.id.action_search);
            SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
            searchView.setQueryHint(getString(android.R.string.search_go));
            searchView.setOnQueryTextListener(this);
        }
        return super.onCreateOptionsMenu(menu);
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
                mAlertDialog = new SCAlertDialog(HorizontalNtbActivity.this, message, R.string.cmn_ok);
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
                mAlertDialog = new SCAlertDialog(HorizontalNtbActivity.this, message, R.string.cmn_ok);
                mAlertDialog.show();
            }
        });
    }

    public static Context getAppContext() {
        return appContext;
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


}
