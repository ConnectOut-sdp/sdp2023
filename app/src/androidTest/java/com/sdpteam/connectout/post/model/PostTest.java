package com.sdpteam.connectout.post.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import org.junit.Test;

import java.util.ArrayList;

public class PostTest {

    @Test
    public void gettersAndSettersTest() {
        Post m = new Post("id", "pid", "eventId", "commentsChatId", new ArrayList<>(), 1, Post.PostVisibility.PUBLIC, "title", "description");
        assertThat(m.getId(), is("id"));
        assertThat(m.getProfileId(), is("pid"));
        assertThat(m.getEventId(), is("eventId"));
        assertThat(m.getCommentsChatId(), is("commentsChatId"));
        assertThat(m.getImagesUrls(), is(new ArrayList<>()));
        assertThat(m.getNbrLikes(), is(1));
        assertThat(m.getVisibility(), is(Post.PostVisibility.PUBLIC));
        assertThat(m.getVisibility().getDesc(), is("Everyone in the app can see this post"));
        assertThat(m.getTitle(), is("title"));
        assertThat(m.getDescription(), is("description"));
    }
}
