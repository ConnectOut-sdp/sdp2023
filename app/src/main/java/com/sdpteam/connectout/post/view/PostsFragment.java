package com.sdpteam.connectout.post.view;

import static com.sdpteam.connectout.profile.EditProfileActivity.NULL_USER;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.sdpteam.connectout.R;
import com.sdpteam.connectout.authentication.AuthenticatedUser;
import com.sdpteam.connectout.authentication.GoogleAuth;
import com.sdpteam.connectout.drawer.DrawerFragment;
import com.sdpteam.connectout.post.model.Post;
import com.sdpteam.connectout.post.model.PostFirebaseDataSource;

/**
 * A fragment representing a list of Posts.
 * The viewModel that is passed to this fragment is the one that dictates which posts to display.
 */
public class PostsFragment extends DrawerFragment {
    private final String eventId;
    private final String authorId;
    private ArrayAdapter<Post> adapter;

    public PostsFragment(String eventId, String authorId) {
        this.eventId = eventId;
        this.authorId = authorId;

    }

    public PostsFragment() {
        this(null, null);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_post_list, container, false);

        if (adapter == null) {
            adapter = new PostsAdapter(container.getContext(), R.layout.post_list_item_view);
        }
        AuthenticatedUser user = new GoogleAuth().loggedUser();
        String currentUserId = user == null ? NULL_USER : user.uid;

        PostsViewModel postsViewModel = new PostsViewModel(new PostFirebaseDataSource());
        postsViewModel.getPosts(currentUserId, eventId, authorId);

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