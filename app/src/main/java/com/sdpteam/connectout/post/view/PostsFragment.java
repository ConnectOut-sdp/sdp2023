package com.sdpteam.connectout.post.view;

import static com.sdpteam.connectout.profile.EditProfileActivity.NULL_USER;

import com.sdpteam.connectout.R;
import com.sdpteam.connectout.authentication.AuthenticatedUser;
import com.sdpteam.connectout.authentication.GoogleAuth;
import com.sdpteam.connectout.drawer.DrawerFragment;
import com.sdpteam.connectout.post.model.Post;
import com.sdpteam.connectout.post.model.PostFirebaseDataSource;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * A fragment representing a list of Posts.
 * The viewModel that is passed to this fragment is the one that dictates which posts to display.
 */
public class PostsFragment extends DrawerFragment {
    private final String eventId;
    private final String authorId;
    private boolean seeEventsVisible;
    private ArrayAdapter<Post> adapter;
    private boolean scrollable;

    public PostsFragment(String eventId, String authorId, boolean openEventButtonsVisible, boolean scrollable) {
        this.eventId = eventId;
        this.authorId = authorId;
        this.seeEventsVisible = openEventButtonsVisible;
        this.scrollable = scrollable;
    }

    public PostsFragment() {
        this(null, null, true, true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;
        if (scrollable) {
            view = inflater.inflate(R.layout.fragment_post_list, container, false);
        } else {
            view = inflater.inflate(R.layout.fragment_post_list_not_scrollable, container, false);
        }

        AuthenticatedUser user = new GoogleAuth().loggedUser();
        String currentUserId = user == null ? NULL_USER : user.uid;

        PostsViewModel postsViewModel = new PostsViewModel(new PostFirebaseDataSource());
        postsViewModel.getPosts(currentUserId, eventId, authorId);

        if (adapter == null) {
            adapter = new PostsAdapter(container.getContext(), R.layout.post_list_item_view, seeEventsVisible, postsViewModel::likePost);
        }

        ListView listView = view.findViewById(R.id.posts_list_view);
        listView.setAdapter(adapter);

        postsViewModel.refreshPosts();
        postsViewModel.getPostsLiveData().observeForever(posts -> {
            adapter.clear();
            adapter.addAll(posts);
            adapter.notifyDataSetChanged();
        });

        return view;
    }
}