package com.sdpteam.connectout.post.model;

import java.util.List;

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

    private final String title;

    private final String description;

    public Post(String id, String profileId, String eventId, String commentsChatId, List<String> imagesUrls, int nbrLikes, PostVisibility visibility, String title, String description) {
        this.id = id;
        this.profileId = profileId;
        this.eventId = eventId;
        this.commentsChatId = commentsChatId;
        this.imagesUrls = imagesUrls;
        this.nbrLikes = nbrLikes;
        this.visibility = visibility;
        this.title = title;
        this.description = description;
    }

    /**
     * PUBLIC means anyone in the app can view this post
     * SEMIPRIVATE means only users that are taking part (joined) to this event (eventId)
     */
    public enum PostVisibility {
        PUBLIC("Everyone in the app can see this post"), SEMIPRIVATE("People who joined this event can see this post");

        private final String desc;

        PostVisibility(String desc) {
            this.desc = desc;
        }

        public String getDesc() {
            return desc;
        }
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

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
