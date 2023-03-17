package com.sdpteam.connectout.chat;

import static com.sdpteam.connectout.chat.ChatActivity.NULL_CHAT;
import static com.sdpteam.connectout.profile.EditProfileActivity.NULL_USER;

import java.util.Date;

public class ChatMessage {

    private final String userName;
    private final String userId;
    private final String messageText;
    private final long messageTime;

    private final String chatId;

    public ChatMessage(String userName, String userId, String messageText, long messageTime, String chatId){
        this.userName = userName;
        this.userId = userId;
        this.messageText = messageText;
        this.messageTime = messageTime;
        this.chatId = chatId;
    }

    /**if no time is specified, then the time that the constructor is called is used*/
    public ChatMessage(String userName, String userId, String messageText, String chatId) {
        this.userName = userName;
        this.userId = userId;
        this.messageText = messageText;
        messageTime = new Date().getTime();
        this.chatId = chatId;
    }
    public ChatMessage(){
        this(NULL_USER, NULL_USER, "", 0, NULL_CHAT);
    }

    public String getUserName() {
        return userName;
    }

    public String getUserId() {
        return userId;
    }

    public String getMessageText() {
        return messageText;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public String getChatId() {
        return chatId;
    }
}
