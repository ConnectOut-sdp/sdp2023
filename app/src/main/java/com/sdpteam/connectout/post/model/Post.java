package com.sdpteam.connectout.post.model;

import static com.sdpteam.connectout.post.model.Post.PostVisibility.SEMIPRIVATE;

import java.util.ArrayList;
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

    private final String title;

    private final String description;

    //Used for firebase class cast
    public Post() {
        this(null, null, null, null, new ArrayList<>(), 0, null, null, null);
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Post post = (Post) o;
        return nbrLikes == post.nbrLikes && Objects.equals(id, post.id) && Objects.equals(profileId, post.profileId) && Objects.equals(eventId, post.eventId) && Objects.equals(commentsChatId,
                post.commentsChatId) && Objects.equals(imagesUrls, post.imagesUrls) && visibility == post.visibility && Objects.equals(title, post.title) && Objects.equals(description,
                post.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, profileId, eventId, commentsChatId, imagesUrls, nbrLikes, visibility, title, description);
    }
}
