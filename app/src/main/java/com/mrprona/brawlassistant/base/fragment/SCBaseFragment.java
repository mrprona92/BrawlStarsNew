package com.mrprona.brawlassistant.base.fragment;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mrprona.brawlassistant.base.activity.BaseActivity;
import com.mrprona.brawlassistant.base.activity.HorizontalNtbActivity;
import com.mrprona.brawlassistant.base.configs.ScreenIDs;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Project : TRS Social
 * Author :DANGLV
 * Date : 31/05/2016
 * Time : 13:17
 * Description :
 */
public abstract class SCBaseFragment extends Fragment {
    protected final String TAG = getClass().getSimpleName();


    protected BaseActivity mBaseActivity;
    protected HorizontalNtbActivity mActivity;

    protected boolean mIsViewInitialized = false;
    protected View mView;
    protected Unbinder mUnbinder;
    protected Context mAppContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof HorizontalNtbActivity) {
            mActivity = (HorizontalNtbActivity) context;
        } else {
            mBaseActivity = (BaseActivity) context;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mActivity != null) {
            mAppContext = mActivity.getApplicationContext();
        } else {
            mAppContext = mBaseActivity.getApplicationContext();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (getActivity() instanceof HorizontalNtbActivity) {
            mActivity = (HorizontalNtbActivity) getActivity();
        } else {
            mBaseActivity = (BaseActivity) getActivity();
        }

        if (mView == null) {
            mView = inflater.inflate(getViewContent(), container, false);
            mUnbinder = ButterKnife.bind(this, mView);
            mIsViewInitialized = false;
        } else {
            mIsViewInitialized = true;
            if (mView.getParent() != null) {
                ((ViewGroup) mView.getParent()).removeView(mView);
            }
        }
        if (!mIsViewInitialized) {
            onInitializeView();
            mIsViewInitialized = true;
        }

        onConnected();

        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        // mActivity.updateUI();
    }

    @Override
    public void onPause() {
        unregisterListener();
        super.onPause();
    }

    public abstract int getToolbarTitle();

    public abstract String getToolbarTitleString();

    //Main layout Id for Fragment
    protected abstract int getViewContent();

    // Finish inflate view
    public void onInitializeView() {
        initUI();
        initControls();
    }

    // Init UI, setup toolbar
    protected abstract void initUI();

    // Setting Event for UI Elements
    protected abstract void initControls();

    // Load data
    protected abstract void initData();

    // Hide information
    // Load data
    public abstract void hideInformation();


    protected abstract void registerListeners();

    protected abstract void unregisterListener();

    public void onConnected() {
        registerListeners();
        initData();
    }

    // Common functions
    protected View findViewById(int id) {
        if (mView != null) return mView.findViewById(id);
        return null;
    }

    public void onComeBackFragment(Bundle resultBundle) {
    }


    protected void hideProgressDialog() {
        hideProgressDialog(true);
    }

    protected void hideProgressDialog(final boolean isWait) {
        if (mActivity != null) mActivity.hideProgressDialog(isWait);
    }

    public void showProgressDialog() {
        if (mActivity != null) mActivity.showProgressDialog(null, true); //"Now Loading..."
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivity = null;
        mBaseActivity = null;
    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void hideSystemUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mActivity.getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                            | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }


    protected void openScreen(final ScreenIDs.ScreenTab tab, final Class<? extends SCBaseFragment> fragmentClass, final Bundle bundles, final boolean isAnimate,
                              final boolean shouldAddToBackstack) {
        if (mActivity != null)
            mActivity.openScreen(tab, fragmentClass, bundles, isAnimate, shouldAddToBackstack);
    }
}
