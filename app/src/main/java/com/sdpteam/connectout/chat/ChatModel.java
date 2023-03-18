package com.sdpteam.connectout.chat;

import static com.sdpteam.connectout.profile.EditProfileActivity.NULL_USER;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.lifecycle.LiveData;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.sdpteam.connectout.R;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class ChatModel implements ChatDirectory{

    private final DatabaseReference firebaseRef;
    public final static String CHATS_PATH_STRING = "Chats";

    public final static int NUM_IMPORTED_MESSAGES = 50;
    private FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    private String userName = (currentUser == null)? NULL_USER : currentUser.getDisplayName();

    private FirebaseListAdapter<ChatMessage> adapter;

    public ChatModel() {
        firebaseRef = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public LiveData<List<ChatMessage>> getMessages(int n, String chatId) {
        return null;
        //TODO fetch a list with the first n values from Database
        //TODO pretty sure that there is no need for this function, because FirebaseListAdapter
        // deals with the fetching
    }

    @Override
    public void saveMessage(ChatMessage message) {
        firebaseRef.child(CHATS_PATH_STRING).child(message.getChatId()).push().setValue(message);
    }

    @Override
    public String getProfileUserName() {
        return userName;
    }

    @Override
    public void setUpListAdapter(Function<FirebaseListOptions.Builder, FirebaseListOptions.Builder> setLayout,
                                 Function<FirebaseListOptions.Builder, FirebaseListOptions.Builder> setLifecycleOwner,
                                 BiConsumer<View, ChatMessage> populateView,
                                 Consumer<ListAdapter> setAdapter,
                                 String chatId){

        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child(CHATS_PATH_STRING)
                .child(chatId)
                .limitToLast(NUM_IMPORTED_MESSAGES);

        FirebaseListOptions<ChatMessage> options = setLifecycleOwner.apply(
                setLayout.apply(new FirebaseListOptions.Builder<ChatMessage>())
                        .setQuery(query, ChatMessage.class))
                .build();

        adapter = new FirebaseListAdapter<ChatMessage>(options){
            @Override
            protected void populateView(@NonNull View v, @NonNull ChatMessage chatMessage, int position) {
                populateView.accept(v, chatMessage);
            }
        };

        setAdapter.accept(adapter);
    }
}
