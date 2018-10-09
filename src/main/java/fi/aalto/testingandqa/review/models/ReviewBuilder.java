package fi.aalto.testingandqa.review.models;

import java.util.ArrayList;
import java.util.List;

public final class ReviewBuilder {
    private Long id;
    private String title;
    private String body;
    private List<Comment> comments = new ArrayList<>();

    private ReviewBuilder() {
    }

    public static ReviewBuilder aReview() {
        return new ReviewBuilder();
    }

    public ReviewBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public ReviewBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public ReviewBuilder withBody(String body) {
        this.body = body;
        return this;
    }

    public ReviewBuilder withComments(List<Comment> comments) {
        this.comments = comments;
        return this;
    }

    public Review build() {
        Review review = new Review();
        review.setId(id);
        review.setTitle(title);
        review.setBody(body);
        review.setComments(comments);
        return review;
    }
}
