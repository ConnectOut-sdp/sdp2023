package com.sdpteam.connectout.post.view;

import com.sdpteam.connectout.R;
import com.sdpteam.connectout.post.model.Post;
import com.sdpteam.connectout.profile.ProfileActivity;
import com.sdpteam.connectout.profile.ProfileFirebaseDataSource;
import com.squareup.picasso.Picasso;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

/**
 * ArrayAdapter for the ListView that inflates each posts ui.
 * It is responsible for creating and managing the views for a list of posts.
 * In fact it knows how to create a view for a single post that will be displayed in the list.
 */
public class PostsAdapter extends ArrayAdapter<Post> {

    public PostsAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.post_list_item_view, parent, false);
        }
        final Post post = getItem(position);

        ViewPager viewPager = view.findViewById(R.id.post_images_view_pager);
        viewPager.setAdapter(new PagedImagesAdapter(getContext(), post.getImagesUrls()));

        DotsIndicator dotsIndicator = view.findViewById(R.id.post_images_dots_indicator);
        dotsIndicator.attachTo(viewPager);

        TextView titleTextView = view.findViewById(R.id.post_title);
        titleTextView.setText(post.getTitle());
        TextView descTextView = view.findViewById(R.id.post_description);
        descTextView.setText(post.getDescription());

        TextView countView = view.findViewById(R.id.post_like_count_text);
        countView.setText(post.getNbrLikes() + " likes");

        ImageButton likeButton = view.findViewById(R.id.post_like_button);
        likeButton.setOnClickListener(v -> {
            // TODO like this post
        });

        Button commentsButton = view.findViewById(R.id.post_comments_button);
        commentsButton.setOnClickListener(v -> {
            // TODO open chat for this post
        });

        displayAuthorInfo(view, post);

        return view;
    }

    private void displayAuthorInfo(View view, Post post) {
        // We could maybe create a fragment for that with its own view model that load the profile image and name but I don't know how to do it and it is not a big deal.
        // The complexity comes from the fact that I was having issues adding a Fragment with its ViewModel to an ArrayAdapter.
        // This is a complex problem in Android because ArrayAdapter's getView() method is called multiple times, making it difficult to ensure that each instance of the Fragment has its own ViewModel and
        // that they are properly initialized before being added to the views.
        // But I am sure it should be possible if you want to try, do it ! :D

        String profileId = post.getProfileId();

        View profileImageAndNameContainer = view.findViewById(R.id.post_profile_bar);
        profileImageAndNameContainer.setOnClickListener(v -> ProfileActivity.openProfile(getContext(), profileId));

        ImageButton profileImageBtn = view.findViewById(R.id.post_profile_image);

        TextView profileNameText = view.findViewById(R.id.post_profile_name);
        profileNameText.setText(profileId);
        new ProfileFirebaseDataSource().fetchProfile(profileId).thenAccept(profile -> {
            profileNameText.setText(profile.getName());
            final String imageUrl = profile.getProfileImageUrl();
            if (!imageUrl.isEmpty()) {
                Picasso.get().load(imageUrl).into(profileImageBtn);
                profileImageBtn.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }
        });
    }
}
