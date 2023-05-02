package com.sdpteam.connectout.chat;

import static com.sdpteam.connectout.profile.EditProfileActivity.NULL_USER;

import android.view.View;
import android.widget.ListAdapter;

import androidx.annotation.NonNull;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class ChatFirebaseDataSource implements ChatDirectory {

    private final static int NUM_IMPORTED_MESSAGES = 50;
    private final DatabaseReference firebaseRef;
    private final String CHATS_PATH_STRING = "Chats";
    private final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    private final String userName = (currentUser == null) ? NULL_USER : currentUser.getDisplayName();

    private FirebaseListAdapter<ChatMessage> adapter;

    public ChatFirebaseDataSource() {
        firebaseRef = FirebaseDatabase.getInstance().getReference();
    }

    /**
     * Saves a given chatMessage to firebase
     */
    @Override
    public void saveMessage(ChatMessage message) {
        firebaseRef.child(CHATS_PATH_STRING).child(message.getChatId()).push().setValue(message);
    }

    /**
     * Called by the ChatView
     */
    @Override
    public String getProfileUserName() {
        return userName;
    }

    /**
     * sets up the FirebaseListAdapter for the chat view
     */
    public void setUpListAdapter(ChatAdapterFirebaseConfig config, String chatId) {

        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child(CHATS_PATH_STRING)
                .child(chatId)
                .limitToLast(NUM_IMPORTED_MESSAGES);

        FirebaseListOptions<ChatMessage> options = config.setLifecycleOwner.apply(config.setLayout.apply(new FirebaseListOptions.Builder<>())
                .setQuery(query, ChatMessage.class)).build();

        adapter = new FirebaseListAdapter<ChatMessage>(options) {
            @Override
            protected void populateView(@NonNull View v, @NonNull ChatMessage chatMessage, int position) {
                config.populateView.accept(v, chatMessage);
            }
        };

        config.setAdapter.accept(adapter);
    }

    public static class ChatAdapterFirebaseConfig {
        Function<FirebaseListOptions.Builder<ChatMessage>, FirebaseListOptions.Builder<ChatMessage>> setLayout;
        Function<FirebaseListOptions.Builder<ChatMessage>, FirebaseListOptions.Builder<ChatMessage>> setLifecycleOwner;
        BiConsumer<View, ChatMessage> populateView;
        Consumer<ListAdapter> setAdapter;

        public ChatAdapterFirebaseConfig(Function<FirebaseListOptions.Builder<ChatMessage>, FirebaseListOptions.Builder<ChatMessage>> setLayout,
                                         Function<FirebaseListOptions.Builder<ChatMessage>, FirebaseListOptions.Builder<ChatMessage>> setLifecycleOwner,
                                         BiConsumer<View, ChatMessage> populateView,
                                         Consumer<ListAdapter> setAdapter) {
            this.setLayout = setLayout;
            this.setLifecycleOwner = setLifecycleOwner;
            this.populateView = populateView;
            this.setAdapter = setAdapter;
        }
    }

    /**
     * Testing is done in the chat called TestChat, but it is important to delete the chat messages
     * generated during the test which is what this method does
     */
    public void emptyTestMode() {
        //hardcoded value to avoid unwanted errors
        firebaseRef.child("Chats/TestChat").removeValue();
    }
}
