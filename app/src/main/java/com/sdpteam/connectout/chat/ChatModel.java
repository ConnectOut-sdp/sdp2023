package com.sdpteam.connectout.chat;

import static com.sdpteam.connectout.profile.EditProfileActivity.NULL_USER;

import androidx.lifecycle.LiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ChatModel implements ChatDirectory{

    private final DatabaseReference firebaseRef;
    public final static String chatsPathString = "Chats";
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    String userName = (currentUser == null)? NULL_USER : currentUser.getDisplayName();

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
        firebaseRef.child(chatsPathString).child(message.getChatId()).push().setValue(message);
    }

    @Override
    public String getProfileUserName() {
        return userName;
    }
}
