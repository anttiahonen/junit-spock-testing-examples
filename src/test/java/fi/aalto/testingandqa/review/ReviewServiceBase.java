package fi.aalto.testingandqa.review;

import fi.aalto.testingandqa.review.models.Review;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

//common inhereted base class for sharing helper methods or setup between test classes
public class ReviewServiceBase {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    //helper method for always checking the same type of exception with message,
    //-> more readability & maintainability
    protected void expectReviewExceptionWithMessage(String msg) {
        thrown.expect(ReviewException.class);
        thrown.expectMessage(msg);
    }

    //helper factory-method for building certain type of object
    protected Review buildDefaultReview() {
        Review review = new Review();
        review.setTitle("Movie of the century");
        review.setBody("So good movie!");
        return review;
    }

}
