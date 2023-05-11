package com.sdpteam.connectout.post.model;

import java.util.List;
import java.util.Objects;

/**
 * A post is identified by it's id.
 * A Post is a publication made by some user (profileId) about an Event (eventId)
 * There is a counter of likes and a list of images that this person wants people to look at.
 * A comment section chat (commentsChatId) allows people to chat about this particular post.
 * The user can choose a visibility mode (PUBLIC mean anyone in the app can see this post, SEMIPRIVATE are only those that have joined this particular event)
 */
public class Post {
    private final String id;
    private final String profileId;
    private final String eventId;
    private final String commentsChatId;
    private final List<String> imagesUrls;
    private final int nbrLikes;
    private final PostVisibility visibility;

    public Post() {
        this.id = null;
        this.profileId = null;
        this.eventId = null;
        this.commentsChatId = null;
        this.imagesUrls = null;
        this.nbrLikes = 0;
        this.visibility = null;
    }

    public Post(String id, String profileId, String eventId, String commentsChatId, List<String> imagesUrls, int nbrLikes, PostVisibility visibility) {
        this.id = id;
        this.profileId = profileId;
        this.eventId = eventId;
        this.commentsChatId = commentsChatId;
        this.imagesUrls = imagesUrls;
        this.nbrLikes = nbrLikes;
        this.visibility = visibility;
    }

    /**
     * PUBLIC means anyone in the app can view this post
     * SEMIPRIVATE means only users that are taking part (joined) to this event (eventId)
     */
    public enum PostVisibility {
        PUBLIC, SEMIPRIVATE
    }

    public String getId() {
        return id;
    }

    public String getProfileId() {
        return profileId;
    }

    public String getEventId() {
        return eventId;
    }

    public String getCommentsChatId() {
        return commentsChatId;
    }

    public List<String> getImagesUrls() {
        return imagesUrls;
    }

    public int getNbrLikes() {
        return nbrLikes;
    }

    public PostVisibility getVisibility() {
        return visibility;
    }
}
