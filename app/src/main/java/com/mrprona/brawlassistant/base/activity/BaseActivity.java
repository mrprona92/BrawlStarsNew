package com.mrprona.brawlassistant.base.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.mrprona.brawlassistant.R;

/**
 * User: ABadretdinov
 * Date: 29.01.14
 * Time: 12:41
 */
public  abstract  class BaseActivity extends AppCompatActivity {
    protected ActionMenuView mActionMenuView;
    protected Toolbar mToolbar;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        initActionBar();
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        initActionBar();
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        initActionBar();
    }

    private void initActionBar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mActionMenuView = (ActionMenuView) findViewById(R.id.actionMenuView);
        if (mActionMenuView != null) {
            mActionMenuView.setVisibility(View.GONE);
        }
        Log.d("BINH", "initActionBar() called with: " + "");
    }
    public ActionMenuView getActionMenuView() {
        return mActionMenuView;
    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void hideSystemUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            this.getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                            | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
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




}
