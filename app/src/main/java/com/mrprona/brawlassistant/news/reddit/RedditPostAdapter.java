package com.mrprona.brawlassistant.news.reddit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mrprona.brawlassistant.R;
import com.mrprona.brawlassistant.base.adapter.OnRedditClickListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by bradl on 10/8/2016.
 */

public class RedditPostAdapter extends ArrayAdapter<RedditPost> {
    public RedditPostAdapter(Context context, ArrayList<RedditPost> posts) {
        super(context, 0, posts);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final RedditPost post = getItem(position);

        // Check if the existing view is being reused, otherwise infalte the view
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_reddit_post, parent, false);
        }

        // Lookup views within the layout
        TextView tvUpvotes = (TextView) convertView.findViewById(R.id.tvUpvotes);
        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
        TextView tvDomain = (TextView) convertView.findViewById(R.id.tvDomain);
        TextView tvComments = (TextView) convertView.findViewById(R.id.tvComments);
        TextView tvSubreddit = (TextView) convertView.findViewById(R.id.tvSubreddit);
        ImageView ivThumbnail = (ImageView) convertView.findViewById(R.id.ivThumbnail);

        // Populate the data
        tvUpvotes.setText("" + post.getUpvotes());
        tvTitle.setText(post.getTitle());
        tvDomain.setText("(" + post.getDomain() + ")");
        tvComments.setText(post.getNumComments() + " comments");
        tvSubreddit.setText(post.getSubreddit());

        // Display the thumbnail if there is one
        String thumbnailUrl = post.getThumbnail();
        if (thumbnailUrl.length() < 1 || thumbnailUrl.equals("self")) {
            ivThumbnail.setImageResource(R.drawable.ic_reddie);
        } else {
            Picasso.with(getContext()).load(post.getThumbnail()).into(ivThumbnail);
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v, post.getContent());
            }
        });

        return convertView;
    }

    private OnRedditClickListener listener;

    public void setOnItemClickListener(OnRedditClickListener listener) {
        this.listener = listener;
    }
}
