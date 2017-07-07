package com.codepath.apps.twitterApp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.twitterApp.models.User;

import java.util.List;

/**
 * Created by kazhang on 7/7/17.
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private final TwitterClient client = TwitterApplication.getRestClient();;
    private List<User> mUsers;
    Context context;
    // pass in Tweets array in constructor
    public UserAdapter(List<User> users) {
        mUsers = users;
    }

    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View userView = inflater.inflate(R.layout.item_user, parent, false);
        UserAdapter.ViewHolder viewHolder = new UserAdapter.ViewHolder(userView);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(UserAdapter.ViewHolder holder, int position) {
        final User user = mUsers.get(position);

        // populate views
        holder.tvUserName.setText(user.name);
        holder.tvBody.setText(user.tagLine);
        holder.tvAtName.setText("@" + user.screenName);

        if (user.verified) {
            holder.ivVerified.setVisibility(View.VISIBLE);
        } else {
            holder.ivVerified.setVisibility(View.GONE);
        }

        // loading profile image
        Glide.with(context)
                .load(user.profileImageUrl)
                .into(holder.ivProfileImage);

    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView ivProfileImage;
        public TextView tvUserName;
        public TextView tvBody;
        public TextView tvAtName;
        public ImageView ivVerified;

        public ViewHolder(View itemView) {
            super(itemView);

            // perform findViewById lookups

            ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
            tvUserName = (TextView) itemView.findViewById(R.id.tvUserName);
            tvBody = (TextView) itemView.findViewById(R.id.tvBody);
            tvAtName = (TextView) itemView.findViewById(R.id.tvAtName);
            ivVerified = (ImageView) itemView.findViewById(R.id.ivVerified);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();

            // get tweet at that position in the list
            if (position != RecyclerView.NO_POSITION) {
                User user = mUsers.get(position);
                Intent intent = new Intent(context, ProfileActivity.class);
                intent.putExtra("screen_name", user.screenName);

                (context).startActivity(intent);
            }

        }
    }

    public void clear() {
        mUsers.clear();
        notifyDataSetChanged();
    }


}
