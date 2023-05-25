package com.sdpteam.connectout.chat;

import static com.sdpteam.connectout.chat.ChatActivity.NULL_CHAT;
import static com.sdpteam.connectout.profile.Profile.NULL_USER;

import java.util.Date;

public class ChatMessage {

    private final String userName;
    private final String userId;
    private final String messageText;
    private final long messageTime;

    private final String chatId;

    private final String imageUrl;

    public ChatMessage(String userName, String userId, String messageText, long messageTime, String chatId, String imageUrl) {
        this.userName = userName;
        this.userId = userId;
        this.messageText = messageText;
        this.messageTime = messageTime;
        this.chatId = chatId;
        this.imageUrl = imageUrl;
    }

    /**
     * if no time is specified, then the time that the constructor is called is used
     */
    public ChatMessage(String userName, String userId, String messageText, String chatId) {
        this(userName, userId, messageText, new Date().getTime(), chatId, "");
    }

    public ChatMessage(String userName, String userId, String messageText, String chatId, String imageUrl) {
        this(userName, userId, messageText, new Date().getTime(), chatId, imageUrl);
    }

    public ChatMessage() {
        this(NULL_USER, NULL_USER, "", 0, NULL_CHAT, "");
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

    public String getImageUrl() {
        return imageUrl;
    }
}
