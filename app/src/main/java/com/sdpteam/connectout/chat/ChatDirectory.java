package com.sdpteam.connectout.chat;

import android.view.View;
import android.widget.ListAdapter;

import androidx.lifecycle.LiveData;

import com.firebase.ui.database.FirebaseListOptions;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;


public interface ChatDirectory {
    /**
     * Get the n last ChatMessages from the chat with the given id
     */
    LiveData<List<ChatMessage>> getMessages(int n, String chatId);

    /**
     * Save your new ChatMessage
     */
    void saveMessage(ChatMessage message);

    String getProfileUserName();

    void setUpListAdapter(Function<FirebaseListOptions.Builder, FirebaseListOptions.Builder> setLayout,
                          Function<FirebaseListOptions.Builder, FirebaseListOptions.Builder> setLifecycleOwner,
                          BiConsumer<View, ChatMessage> populateView,
                          Consumer<ListAdapter> setAdapter,
                          String chatId);
}
