package com.mrprona.brawlassistant.base.youtube;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.youtube.YouTube;
import com.mrprona.brawlassistant.R;
import com.mrprona.brawlassistant.base.configs.ScreenIDs;
import com.mrprona.brawlassistant.base.fragment.SCBaseFragment;
import com.util.responses.ChannelInfo;

import java.util.ArrayList;
import java.util.List;

import static com.mrprona.brawlassistant.base.youtube.YouTubeRecyclerViewFragment.ARG_PLAYLIST_IDS;

/**
 * User: ABadretdinov
 * Date: 16.01.14
 * Time: 15:57
 */
public class YoutubeFragment extends SCBaseFragment implements ChannelAdapter.OnClickImageChannel {


    private List<ChannelInfo> mListChannel = new ArrayList<>();

    private ChannelAdapter mChannelAdapter;

    private RecyclerView mRecycleView;

    private static final String[] YOUTUBE_PLAYLISTS = {
            "UCooVYzDxdwTtGYAkcPmOgOw"
    };

    private ArrayList<String[]> listChannel = new ArrayList<>();

    private YouTube mYoutubeDataApi;
    private final GsonFactory mJsonFactory = new GsonFactory();
    private final HttpTransport mTransport = AndroidHttp.newCompatibleTransport();


    public static YoutubeFragment newInstance() {
        YoutubeFragment fragment = new YoutubeFragment();
        return fragment;
    }

    @Override
    public int getToolbarTitle() {
        return -99;
    }

    @Override
    public String getToolbarTitleString() {
        return "CHANNEL";
    }

    @Override
    protected int getViewContent() {
        return R.layout.recycler_content_youtube;
    }

    @Override
    protected void initUI() {
        mRecycleView = (RecyclerView) findViewById(R.id.list);
        mChannelAdapter = new ChannelAdapter(getContext());
        mChannelAdapter.setOnItemClickListener(this);
        mRecycleView.setItemAnimator(new DefaultItemAnimator());
        mRecycleView.setAdapter(mChannelAdapter);
        mRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        String[] empty = {""};
        String[] powerGaming = {"PLuWkXi1TKRndniIyrUHWstVLEKk3yDGjw", "PLuWkXi1TKRne_mbjsUuceEQQta4CmsFF6"};
        listChannel.add(empty);
        listChannel.add(powerGaming);
        prepareChannels();
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


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onStart() {

        super.onStart();
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }


    public RecyclerView.LayoutManager getLayoutManager(Context context) {
        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        return manager;
    }


    private void prepareChannels() {
        int[] covers = new int[]{
                R.drawable.channel_introduce,
        };
        ChannelInfo a = new ChannelInfo("INTRODUCE", "INTRODUCE", covers[0], "ABC");
        ChannelInfo b = new ChannelInfo("POWERGAMING", "POWERGAMING", covers[0], "ABC");
        mListChannel.add(a);
        mListChannel.add(b);
        mChannelAdapter.setListAdapter(mListChannel);
    }


    @Override
    public void OnClickImage(View v, int position) {
        if (position == 0) {
            Bundle mBundle = new Bundle();
            mBundle.putString(ARG_PLAYLIST_IDS, YOUTUBE_PLAYLISTS[0]);
            openScreen(ScreenIDs.ScreenTab.LIVE, YouTubeRecyclerViewFragment.class, mBundle, true, true);
        } else {
            Bundle mBundle = new Bundle();
            mBundle.putStringArray(ARG_PLAYLIST_IDS, listChannel.get(position));
            openScreen(ScreenIDs.ScreenTab.LIVE, YouTubeRecyclerPlaylistFragment.class, mBundle, true, true);
        }
    }
}
