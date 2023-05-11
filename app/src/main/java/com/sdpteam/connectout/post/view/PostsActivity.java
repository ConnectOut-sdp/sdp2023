package com.sdpteam.connectout.post.view;

import com.sdpteam.connectout.R;
import com.sdpteam.connectout.utils.WithFragmentActivity;

import android.os.Bundle;

/**
 * Activity that contains the fragment for the list of posts
 */
public class PostsActivity extends WithFragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);
        replaceFragment(new PostListFragment(), R.id.fragment_container);
    }
}