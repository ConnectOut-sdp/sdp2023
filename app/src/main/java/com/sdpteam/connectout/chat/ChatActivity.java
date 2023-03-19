package com.sdpteam.connectout.chat;

import static com.sdpteam.connectout.profile.EditProfileActivity.NULL_USER;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.DrawableCompat;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sdpteam.connectout.R;
import com.sdpteam.connectout.authentication.AuthenticatedUser;
import com.sdpteam.connectout.authentication.GoogleAuth;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class ChatActivity extends AppCompatActivity {

    private final String YOU = "You";
    public ChatViewModel viewModel = new ChatViewModel(new ChatModel());
    public AuthenticatedUser au = new GoogleAuth().loggedUser();

    public String uid = (au == null) ? NULL_USER : au.uid;

    public final String userName = viewModel.getProfileUserName();

    public String chatId;

    public static final String NULL_CHAT = "null_chat";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        chatId = getIntent().getStringExtra("chatId");
        chatId = (chatId == null)? NULL_CHAT : chatId;

        //we enable the return button in the ActionBar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        EditText input = findViewById(R.id.chat_input);
        FloatingActionButton chatFab = findViewById(R.id.chat_fab);

        chatFab.setOnClickListener((view) ->{
            viewModel.saveMessage(new ChatMessage(userName, uid, input.getText().toString(), chatId));
            input.setText("");
        });

        //set up the ListView
        setUpListAdapter();
    }

    /**
     * Returns to the previous activity if the return button in the Actionbar is pressed
     * */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setUpListAdapter() {
        ListView listOfMessages = findViewById(R.id.list_of_messages);
        listOfMessages.setStackFromBottom(true);

        Function<FirebaseListOptions.Builder, FirebaseListOptions.Builder> setLayout = a -> a.setLayout(R.layout.message);
        Function<FirebaseListOptions.Builder, FirebaseListOptions.Builder> setLifecycleOwner = a -> a.setLifecycleOwner(this);
        BiConsumer<View, ChatMessage> populateView = populateViewBiConsumer();
        Consumer<ListAdapter> setAdapter = adapter -> listOfMessages.setAdapter(adapter);

        viewModel.setUpListAdapter(setLayout, setLifecycleOwner, populateView, setAdapter, chatId);
    }

    private BiConsumer<View, ChatMessage> populateViewBiConsumer(){
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
            if (chatMessage.getUserId().equals(uid)){//your messages are to the right of the screen
                messageText.setGravity(Gravity.END);
                messageUser.setText(YOU);
                messageUser.setGravity(Gravity.CENTER);
                Drawable backgroundDrawable = DrawableCompat.wrap(v.getBackground()).mutate();
                backgroundDrawable.setColorFilter( 0x58F8F800, PorterDuff.Mode.ADD);
            }
        };
    }
}