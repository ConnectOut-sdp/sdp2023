package com.sdpteam.connectout.chat;

import static com.sdpteam.connectout.chat.ChatActivity.NULL_CHAT;
import static com.sdpteam.connectout.profile.Profile.NULL_USER;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import org.junit.Test;

public class ChatMessageTest {

    @Test
    public void gettersAndSettersTest() {
        ChatMessage m = new ChatMessage(NULL_USER, NULL_USER, "test message", NULL_CHAT);
        assertThat(m.getUserName(), is(NULL_USER));
        assertThat(m.getUserId(), is(NULL_USER));
        assertThat(m.getMessageText(), is("test message"));
        assertThat(m.getChatId(), is(NULL_CHAT));

        m = new ChatMessage();

        assertThat(m.getUserName(), is(NULL_USER));
        assertThat(m.getUserId(), is(NULL_USER));
        assertThat(m.getMessageText(), is(""));
        assertThat(m.getChatId(), is(NULL_CHAT));
        assertThat(m.getMessageTime(), is(0L));
    }

    @Test
    public void constructorWorks() {
        ChatMessage chatMessage = new ChatMessage(NULL_USER, NULL_USER, "test message", NULL_CHAT, "imageUrl");
        assertThat(chatMessage.getUserName(), is(NULL_USER));
        assertThat(chatMessage.getUserId(), is(NULL_USER));
        assertThat(chatMessage.getMessageText(), is("test message"));
        assertThat(chatMessage.getChatId(), is(NULL_CHAT));
        assertThat(chatMessage.getImageUrl(), is("imageUrl"));
    }
}
