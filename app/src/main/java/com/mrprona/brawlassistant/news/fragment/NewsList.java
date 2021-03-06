package com.mrprona.brawlassistant.news.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.ActionMenuView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Toast;

import com.mrprona.brawlassistant.base.activity.HorizontalNtbActivity;
import com.mrprona.brawlassistant.base.fragment.UpdatableRecyclerFragment;
import com.mrprona.brawlassistant.base.view.EndlessRecycleScrollListener;
import com.mrprona.brawlassistant.news.activity.NewsItemActivity;
import com.mrprona.brawlassistant.news.adapter.NewsAdapter;
import com.mrprona.brawlassistant.news.adapter.NewsItemViewHolder;
import com.mrprona.brawlassistant.news.api.AppNews;
import com.mrprona.brawlassistant.news.api.NewsItem;
import com.mrprona.brawlassistant.news.task.NewsLoadRequest;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.UncachedSpiceService;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import com.mrprona.brawlassistant.R;

/**
 * User: ABadretdinov
 * Date: 21.04.14
 * Time: 18:29
 */
public class NewsList extends UpdatableRecyclerFragment<NewsItem, NewsItemViewHolder> implements RequestListener<AppNews> {

    private SpiceManager mSpiceManager = new SpiceManager(UncachedSpiceService.class);
    private boolean mReloading = false;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        ActionMenuView actionMenuView = ((HorizontalNtbActivity) getActivity()).getActionMenuView();
        Menu actionMenu = actionMenuView.getMenu();
        actionMenu.clear();
        actionMenuView.setVisibility(View.GONE);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        View root = getView();
        if (root == null) {
            return;
        }
        getRecyclerView().addOnScrollListener(new EndlessRecycleScrollListener() {
            @Override
            public void onLoadMore() {
                mReloading = false;
                loadNews();
            }
        });
    }

    private void loadNews() {
        final HorizontalNtbActivity activity = (HorizontalNtbActivity) getActivity();
        if (activity != null) {
            setRefreshing(true);
            Long fromDate = null;
            if (!mReloading) {
                NewsItem lastItem = getAdapter().getItem(getAdapter().getItemCount() - 1);
                fromDate = lastItem.getDate();
            }
            mSpiceManager.execute(new NewsLoadRequest(activity.getApplicationContext(), fromDate), this);
        }
    }

    @Override
    public void onRefresh() {
        mReloading = true;
        loadNews();
    }

    @Override
    public void onStart() {
        if (!mSpiceManager.isStarted()) {
            mSpiceManager.start(getActivity());
            onRefresh();
        }
        super.onStart();
    }

    @Override
    public void onDestroy() {
        if (mSpiceManager.isStarted()) {
            mSpiceManager.shouldStop();
        }
        super.onDestroy();
    }

    @Override
    public void onRequestFailure(SpiceException spiceException) {
        Toast.makeText(getActivity(), spiceException.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        setRefreshing(false);
    }

    @Override
    public void onRequestSuccess(AppNews newsItems) {
        if (!mReloading && getAdapter() != null) {
            ((NewsAdapter) getAdapter()).addData(newsItems.getNewsItems());
        } else {
            setAdapter(new NewsAdapter(newsItems.getNewsItems()));
        }
        setRefreshing(false);
    }

    @Override
    public void onItemClick(View view, int position) {
        NewsItem item = getAdapter().getItem(position);
        Intent intent = new Intent(getActivity(), NewsItemActivity.class);
        intent.putExtra("newsItem", item);
        startActivity(intent);
    }

    @Override
    public int getToolbarTitle() {
        return R.string.menu_news_dota;
    }

    @Override
    public String getToolbarTitleString() {
        return null;
    }

    @Override
    protected int getViewContent() {
        return 0;
    }

    @Override
    protected void initUI() {

    }

    @Override
    protected void initControls() {

    }

    @Override
    protected void initData() {

    }

    @Override
    public void hideInformation() {

    }

    @Override
    protected void registerListeners() {

    }

    @Override
    protected void unregisterListener() {

    }
}
