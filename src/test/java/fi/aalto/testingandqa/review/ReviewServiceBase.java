package fi.aalto.testingandqa.review;

import org.junit.Rule;
import org.junit.rules.ExpectedException;

public class ReviewServiceBase {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    void expectReviewExceptionWithMessage(String msg) {
        thrown.expect(ReviewException.class);
        thrown.expectMessage(msg);
    }

    Review buildDefaultReview() {
        Review review = new Review();
        review.setTitle("Movie of the century");
        review.setBody("So good movie!");
        return review;
    }

}
