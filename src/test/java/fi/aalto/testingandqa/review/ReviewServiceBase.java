package fi.aalto.testingandqa.review;

import fi.aalto.testingandqa.review.models.Comment;
import fi.aalto.testingandqa.review.models.Reaction;
import fi.aalto.testingandqa.review.models.ReactionType;
import fi.aalto.testingandqa.review.models.Review;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;

public class ReviewServiceBase {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    protected void expectReviewExceptionWithMessage(String msg) {
        thrown.expect(ReviewException.class);
        thrown.expectMessage(msg);
    }

    protected Review buildDefaultReview() {
        Review review = new Review();
        review.setTitle("Movie of the century");
        review.setBody("So good movie!");
        return review;
    }

}
