package com.sdpteam.connectout.chat.comment;

import static com.sdpteam.connectout.profile.EditProfileActivity.NULL_USER;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.database.FirebaseListOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sdpteam.connectout.R;
import com.sdpteam.connectout.authentication.AuthenticatedUser;
import com.sdpteam.connectout.authentication.GoogleAuth;
import com.sdpteam.connectout.chat.ChatFirebaseDataSource;
import com.sdpteam.connectout.chat.ChatMessage;
import com.sdpteam.connectout.chat.ChatViewModel;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class CommentsActivity extends AppCompatActivity {

    public final static String PASSED_COMMENTS_KEY = "commentsId";
    public static final String NULL_COMMENTS = "null_comments";

    public ChatViewModel viewModel = new ChatViewModel(new ChatFirebaseDataSource());
    public final String userName = viewModel.getProfileUserName();
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
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        EditText input = findViewById(R.id.comment_input);
        FloatingActionButton commentPost = findViewById(R.id.comment_post);
        commentPost.setOnClickListener((view) -> {
            viewModel.saveMessage(new ChatMessage(userName, uid, input.getText().toString(), commentsId));
            input.setText("");
        });

        setUpListAdapter();
    }

    /**
     * Helper method to launch a event activity from the source context
     * (made it to avoid code duplication)
     *
     * @param fromContext from where we are starting the intent
     * @param postId     event Id to open with.
     */
    public static void openComments(Context fromContext, String postId) {
        Intent intent = new Intent(fromContext, CommentsActivity.class);
        intent.putExtra(PASSED_COMMENTS_KEY, postId);
        fromContext.startActivity(intent);
    }

    /**
     * Returns to the previous activity if the return button in the Actionbar is pressed
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * The view must not interact directly with Firebase, as such the FirebaseListAdapter is created
     * by the model.
     * However the adapter needs indirect access to the view elements as such we pass lambdas so that
     * the Model has no direct access.
     */
    private void setUpListAdapter() {
        ListView listOfComments = findViewById(R.id.list_of_comments);
        listOfComments.setStackFromBottom(true);
        listOfComments.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);

        Function<FirebaseListOptions.Builder<ChatMessage>, FirebaseListOptions.Builder<ChatMessage>> setLayout = a -> a.setLayout(R.layout.message);
        Function<FirebaseListOptions.Builder<ChatMessage>, FirebaseListOptions.Builder<ChatMessage>> setLifecycleOwner = a -> a.setLifecycleOwner(this);
        BiConsumer<View, ChatMessage> populateView = populateMessageViewBiConsumer();
        Consumer<ListAdapter> setAdapter = listOfComments::setAdapter;

        viewModel.setUpListAdapter(setLayout, setLifecycleOwner, populateView, setAdapter, commentsId);
    }

    /**
     * Creates the view for a single message
     * Your messages are to the right and others' are to the left
     */
    private BiConsumer<View, ChatMessage> populateMessageViewBiConsumer() {
        return (v, chatMessage) -> {
            TextView messageText = v.findViewById(R.id.message_text);
            TextView messageUser = v.findViewById(R.id.message_user);
            TextView messageTime = v.findViewById(R.id.message_time);
            // Set the text of the message's view
            messageText.setText(chatMessage.getMessageText());
            messageUser.setText(chatMessage.getUserName());
            // Format the date before showing it
            messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
                    chatMessage.getMessageTime()));
            messageTime.setGravity(Gravity.END);
            // Hide the image view
            v.findViewById(R.id.message_image).setVisibility(View.GONE);
        };
    }
}