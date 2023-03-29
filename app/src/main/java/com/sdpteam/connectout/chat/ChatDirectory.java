package com.sdpteam.connectout.chat;

import android.view.View;
import android.widget.ListAdapter;

import com.firebase.ui.database.FirebaseListOptions;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;


public interface ChatDirectory {

    /**
     * Save your new ChatMessage
     */
    void saveMessage(ChatMessage message);

    String getProfileUserName();

    void setUpListAdapter(Function<FirebaseListOptions.Builder<ChatMessage>, FirebaseListOptions.Builder<ChatMessage>> setLayout,
                          Function<FirebaseListOptions.Builder<ChatMessage>, FirebaseListOptions.Builder<ChatMessage>> setLifecycleOwner,
                          BiConsumer<View, ChatMessage> populateView,
                          Consumer<ListAdapter> setAdapter,
                          String chatId);
}
