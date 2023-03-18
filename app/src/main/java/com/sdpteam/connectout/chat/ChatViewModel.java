package com.sdpteam.connectout.chat;

import android.view.View;
import android.widget.ListAdapter;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.firebase.ui.database.FirebaseListOptions;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class ChatViewModel extends ViewModel {
    public ChatModel chatModel;

    public ChatViewModel(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    /**
     * Get the n last ChatMessages from the chat with the given id
     */
    public LiveData<List<ChatMessage>> getMessages(int n, String chatId) {
        return chatModel.getMessages(n, chatId);
    }

    /**
     * Save your new ChatMessage
     */
    public void saveMessage(ChatMessage message) {
        chatModel.saveMessage(message);
    }

    public String getProfileUserName(){
        return chatModel.getProfileUserName();
    }

    public void setUpListAdapter(Function<FirebaseListOptions.Builder, FirebaseListOptions.Builder> setLayout,
                                 Function<FirebaseListOptions.Builder, FirebaseListOptions.Builder> setLifecycleOwner,
                                 BiConsumer<View, ChatMessage> populateView,
                                 Consumer<ListAdapter> setAdapter,
                                 String chatId){
        chatModel.setUpListAdapter(setLayout, setLifecycleOwner, populateView, setAdapter, chatId);
    }
}




