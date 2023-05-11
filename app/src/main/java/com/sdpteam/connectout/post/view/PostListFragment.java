package com.sdpteam.connectout.post.view;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.sdpteam.connectout.R;
import com.sdpteam.connectout.post.model.Post;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.fragment.app.Fragment;

/**
 * A fragment representing a list of Posts.
 * The viewModel that is passed to this fragment is the one that dictates which posts to display.
 */
public class PostListFragment extends Fragment {

    //    private final PostsViewModel postsViewModel; // TODO viewModel that feeds this list

    public PostListFragment() {
        // TODO uncomment this when the viewModel is ready
//        this.postsViewModel = postsViewModel;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_post_list, container, false);

        final ArrayAdapter<Post> adapter = new PostsAdapter(container.getContext(), R.layout.post_list_item_view);

        adapter.addAll(hardCodedPosts());

        // TODO uncomment this when the viewModel is ready
//        postsViewModel.getPostsToDisplayLiveData().observeForever(posts -> {
//            adapter.clear();
//            adapter.addAll(posts);
//            adapter.notifyDataSetChanged();
//        });

        ListView listView = view.findViewById(R.id.posts_list_view);
        listView.setAdapter(adapter);
        return view;
    }

    // TODO remove this function when the viewModel is ready
    // Just sample data
    private static List<Post> hardCodedPosts() {
        List<Post> posts = new ArrayList<>();
        ArrayList<String> imagesUrls = new ArrayList<>();
        imagesUrls.add("https://firebasestorage.googleapis.com/v0/b/my-project-test-a847f.appspot.com/o/1683269814271_40078ef6-3886-4c15-b013-940b8d3f1614" +
                ".jpg?alt=media&token=c5abae50-d2af-4361-bb75-d975f8460a6d");
        imagesUrls.add("https://firebasestorage.googleapis.com/v0/b/my-project-test-a847f.appspot.com/o/1683269290710_5e3659ce-9aea-4f90-aea2-64eb941d6a6c" +
                ".jpg?alt=media&token=590a4df0-12c1-491a-9b89-f621c8f2bbe3");
        posts.add(new Post("idid", "eAzCDL8zdZaJu635KhtUnh2foFo2", "", "okko", imagesUrls, 6, Post.PostVisibility.PUBLIC, "Awesome title", "My awesome post description here that is very long " +
                "because why not"));
        posts.add(new Post("idid2", "T60S7sst1sQ4sPCwcOC13JawOgD3", "eid", "chatId", imagesUrls.stream().sorted().collect(Collectors.toList()), 24, Post.PostVisibility.SEMIPRIVATE, "Like my post!!",
                "Let's go an watch the movie Shrek again, it's so funny and nostalgic about my childhood!"));
        return posts;
    }
}