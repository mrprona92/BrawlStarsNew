package com.mrprona.brawlassistant.news.reddit;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.mrprona.brawlassistant.R;
import com.mrprona.brawlassistant.base.adapter.OnRedditClickListener;
import com.mrprona.brawlassistant.base.configs.ScreenIDs;
import com.mrprona.brawlassistant.base.fragment.AgreementFragment;
import com.mrprona.brawlassistant.base.fragment.SCBaseFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.mrprona.brawlassistant.base.fragment.AgreementFragment.ARG_URL;

public class PostsFragment extends SCBaseFragment implements OnRedditClickListener {
    private ListView lvPosts;
    private RedditPostAdapter postsAdapter;
    RedditClient client;

    @Override
    public int getToolbarTitle() {
        return -99;
    }

    @Override
    public String getToolbarTitleString() {
        return "NEWS";
    }

    @Override
    protected int getViewContent() {
        return R.layout.activity_posts;
    }

    @Override
    protected void initUI() {
        lvPosts = (ListView) findViewById(R.id.lvPosts);
        ArrayList<RedditPost> aPosts = new ArrayList<RedditPost>();
        postsAdapter = new RedditPostAdapter(mActivity, aPosts);
        postsAdapter.setOnItemClickListener(this);
        lvPosts.setAdapter(postsAdapter);
        client = new RedditClient();
        fetchPosts();
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

    public void fetchPosts() {
        showProgressDialog();
        client.getPosts(new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, org.apache.http.Header[] headers, JSONObject responseBody) {
                JSONArray items = null;
                try {
                    // Get the posts json array
                    JSONObject data = responseBody.getJSONObject("data");
                    items = data.getJSONArray("children");
                    // Parse the json array into array of model objects
                    ArrayList<RedditPost> posts = RedditPost.fromJson(items);
                    // Load the model objects into the adapter
                    for (RedditPost post : posts) {
                        postsAdapter.add(post);
                    }
                    postsAdapter.notifyDataSetChanged();
                    hideProgressDialog();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onItemClick(View view, String content) {
        Bundle mBundle = new Bundle();
        mBundle.putString(ARG_URL, content);
        openScreen(ScreenIDs.ScreenTab.NEWS, AgreementFragment.class, mBundle, true, true);
    }
}
