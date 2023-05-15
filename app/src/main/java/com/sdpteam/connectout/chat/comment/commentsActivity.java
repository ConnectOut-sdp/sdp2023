package com.sdpteam.connectout.chat.comment;

import static com.sdpteam.connectout.profile.EditProfileActivity.NULL_USER;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.sdpteam.connectout.R;
import com.sdpteam.connectout.authentication.AuthenticatedUser;
import com.sdpteam.connectout.authentication.GoogleAuth;

public class commentsActivity extends AppCompatActivity {

    public final static String PASSED_COMMENTS_KEY = "commentsId";
    public static final String NULL_COMMENTS = "null_comments";
    public AuthenticatedUser au = new GoogleAuth().loggedUser();
    public String uid = (au == null) ? NULL_USER : au.uid;
    public String commentsId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        commentsId = getIntent().getStringExtra(PASSED_COMMENTS_KEY);
        commentsId = (commentsId == null) ? NULL_COMMENTS : commentsId;

        //we enable the return button in the ActionBar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}