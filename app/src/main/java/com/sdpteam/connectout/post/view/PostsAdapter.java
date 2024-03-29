package com.sdpteam.connectout.post.view;

import static com.sdpteam.connectout.post.model.Post.PostVisibility.PUBLIC;

import java.util.concurrent.CompletableFuture;

import com.sdpteam.connectout.R;
import com.sdpteam.connectout.chat.comment.CommentsActivity;
import com.sdpteam.connectout.event.viewer.EventActivity;
import com.sdpteam.connectout.post.model.Post;
import com.sdpteam.connectout.profile.Profile;
import com.sdpteam.connectout.profile.ProfileActivity;
import com.sdpteam.connectout.profile.ProfileFirebaseDataSource;
import com.sdpteam.connectout.profile.ProfileViewModel;
import com.sdpteam.connectout.utils.Result;
import com.squareup.picasso.Picasso;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.viewpager.widget.ViewPager;

/**
 * ArrayAdapter for the ListView that inflates each posts ui.
 * It is responsible for creating and managing the views for a list of posts.
 * In fact it knows how to create a view for a single post that will be displayed in the list.
 * If the event id is null or empty then the button that opens the event will be hidden.
 */
public class PostsAdapter extends ArrayAdapter<Post> {

    private final int postItemResource;
    private final boolean seeEventsVisible;

    private final OnPostLiked onPostLiked;

    public PostsAdapter(@NonNull Context context, int resource, boolean seeEventsVisible, OnPostLiked onPostLiked) {
        super(context, resource);
        this.postItemResource = resource;
        this.seeEventsVisible = seeEventsVisible;
        this.onPostLiked = onPostLiked;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(postItemResource, parent, false);
        }
        final Post post = getItem(position);

        final ViewPager viewPager = view.findViewById(R.id.post_images_view_pager);
        viewPager.setAdapter(new PagedImagesAdapter(getContext(), post.getImagesUrls()));

        final DotsIndicator dotsIndicator = view.findViewById(R.id.post_images_dots_indicator);
        dotsIndicator.attachTo(viewPager);

        final TextView titleTextView = view.findViewById(R.id.post_title);
        titleTextView.setText(post.getTitle());
        final TextView descTextView = view.findViewById(R.id.post_description);
        descTextView.setText(post.getDescription());

        setupLikeButton(view, post);

        final Button commentsButton = view.findViewById(R.id.post_comments_button);
        commentsButton.setOnClickListener(v -> CommentsActivity.openComments(getContext(), post.getCommentsChatId()));

        final Button eventButton = view.findViewById(R.id.post_event_button);
        eventButton.setVisibility(seeEventsVisible ? View.VISIBLE : View.GONE);
        eventButton.setOnClickListener(v -> EventActivity.openEvent(getContext(), post.getEventId()));

        final ImageView visibilityIcon = view.findViewById(R.id.post_visibility_icon);
        visibilityIcon.setImageResource(post.getVisibility().equals(PUBLIC) ? R.drawable.public_icon : R.drawable.not_public_icon);
        visibilityIcon.setOnClickListener(v -> {
            String text = "Post is " + post.getVisibility().toString() + " (" + post.getVisibility().getDesc() + ")";
            Toast.makeText(getContext(), text, Toast.LENGTH_LONG).show();
        });

        displayAuthorInfo(view, post);

        return view;
    }

    @SuppressLint("SetTextI18n")
    private void setupLikeButton(View view, Post post) {
        final String LIKES = getContext().getString(R.string.likes);

        final TextView countView = view.findViewById(R.id.post_like_count_text);
        countView.setText(post.getNbrLikes() + LIKES);

        final ImageButton likeButton = view.findViewById(R.id.post_like_button);
        final ProgressBar likeProgressBar = view.findViewById(R.id.post_like_progress_bar);
        likeButton.setOnClickListener(v -> {
            likeButton.setVisibility(View.INVISIBLE);
            likeProgressBar.setVisibility(View.VISIBLE);
            onPostLiked.postLiked(post).thenAccept(res -> {
                likeButton.setVisibility(View.VISIBLE);
                likeButton.setEnabled(false);
                likeProgressBar.setVisibility(View.GONE);
                if (res.isSuccess()) {
                    countView.setText(post.getNbrLikes() + 1 + LIKES);
                } else {
                    Toast.makeText(getContext(), res.msg(), Toast.LENGTH_LONG).show();
                }
            });
        });
    }

    private void displayAuthorInfo(View view, Post post) {
        final String profileId = post.getProfileId();

        final ImageButton profileImageBtn = view.findViewById(R.id.post_profile_image);
        final TextView profileNameText = view.findViewById(R.id.post_profile_name);

        View.OnClickListener openProfile = v -> ProfileActivity.openProfile(getContext(), profileId);
        profileImageBtn.setOnClickListener(openProfile);
        profileNameText.setOnClickListener(openProfile);

        profileNameText.setText(profileId);
        ProfileViewModel profileViewModel = new ProfileViewModel(new ProfileFirebaseDataSource());
        profileViewModel.fetchProfile(profileId);
        Observer<Profile> profileObserver = profile -> {
            profileNameText.setText(profile.getName());
            final String imageUrl = profile.getProfileImageUrl();
            if (!imageUrl.isEmpty()) {
                Picasso.get().load(imageUrl).into(profileImageBtn);
                profileImageBtn.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }
        };
        profileViewModel.getProfileLiveData().observeForever(profileObserver);
    }

    interface OnPostLiked {
        /**
         * @param post the post that was liked
         * @return a future that will be completed when the post is liked, the string is just the post id.
         */
        CompletableFuture<Result<String>> postLiked(Post post);
    }
}
