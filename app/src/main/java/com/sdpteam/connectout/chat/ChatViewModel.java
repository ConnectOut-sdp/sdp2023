package com.sdpteam.connectout.chat;

import android.view.View;
import android.widget.ListAdapter;

import androidx.lifecycle.ViewModel;

import com.firebase.ui.database.FirebaseListOptions;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class ChatViewModel extends ViewModel {
    public ChatFirebaseDataSource chatFirebaseDataSource;

    public ChatViewModel(ChatFirebaseDataSource chatFirebaseDataSource) {
        this.chatFirebaseDataSource = chatFirebaseDataSource;
    }

    /**
     * Save your new ChatMessage
     */
    public void saveMessage(ChatMessage message) {
        chatFirebaseDataSource.saveMessage(message);
    }

    /**
     * Called by the ChatView
     */
    public String getProfileUserName() {
        return chatFirebaseDataSource.getProfileUserName();
    }

    /**
     * sets up the FirebaseListAdapter for the chat view
     */
    public void setUpListAdapter(Function<FirebaseListOptions.Builder<ChatMessage>, FirebaseListOptions.Builder<ChatMessage>> setLayout,
                                 Function<FirebaseListOptions.Builder<ChatMessage>, FirebaseListOptions.Builder<ChatMessage>> setLifecycleOwner,
                                 BiConsumer<View, ChatMessage> populateView,
                                 Consumer<ListAdapter> setAdapter,
                                 String chatId) {
        chatFirebaseDataSource.setUpListAdapter(setLayout, setLifecycleOwner, populateView, setAdapter, chatId);
    }
}




