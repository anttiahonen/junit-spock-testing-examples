package fi.aalto.testingandqa.review.models;

import java.util.HashSet;
import java.util.Set;

public final class CommentBuilder {
    private Long id;
    private String author;
    private String body;
    private Set<Reaction> reactions = new HashSet<>();

    private CommentBuilder() {
    }

    public static CommentBuilder aComment() {
        return new CommentBuilder();
    }

    public CommentBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public CommentBuilder withAuthor(String author) {
        this.author = author;
        return this;
    }

    public CommentBuilder withBody(String body) {
        this.body = body;
        return this;
    }

    public CommentBuilder withReactions(Set<Reaction> reactions) {
        this.reactions = reactions;
        return this;
    }

    public Comment build() {
        Comment comment = new Comment();
        comment.setId(id);
        comment.setAuthor(author);
        comment.setBody(body);
        comment.setReactions(reactions);
        return comment;
    }
}
