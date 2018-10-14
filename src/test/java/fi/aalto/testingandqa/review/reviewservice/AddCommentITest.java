package fi.aalto.testingandqa.review.reviewservice;

import fi.aalto.testingandqa.review.ReviewException;
import fi.aalto.testingandqa.review.ReviewRepository;
import fi.aalto.testingandqa.review.ReviewService;
import fi.aalto.testingandqa.review.ReviewServiceBase;
import fi.aalto.testingandqa.review.models.Comment;
import fi.aalto.testingandqa.review.models.Review;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.Assert.assertEquals;

@SpringBootTest
@RunWith(SpringRunner.class)
public class AddCommentITest extends ReviewServiceBase {

    @Autowired
    ReviewService reviewService;

    @Autowired
    ReviewRepository reviewRepository;
    
    static String COMMENT_AUTHOR = "Antti A";
    static String COMMENT = "Your review2 sucked big time!";

    @Test
    @Transactional
    public void test_addComment_withValidCommentThatHasAuthor_setsTheAuthorAndBodyForComment() throws ReviewException {
        Review persistedReview = createDefaultReview();

        reviewService.addComment(persistedReview.getId(), COMMENT_AUTHOR, COMMENT);

        List<Comment> reviewComments = reviewRepository.findById(persistedReview.getId()).get().getComments();
        Comment firstComment = reviewComments.get(0);
        assertEquals(firstComment.getAuthor(), COMMENT_AUTHOR);
        assertEquals(firstComment.getBody(), COMMENT);
    }

    @Test
    @Transactional
    public void test_addComment_withValidCommentWithoutAuthor_persistsTheCommentToGivenReview() throws ReviewException {
        Review persistedReview = createDefaultReview();
        String noAuthor = null;

        reviewService.addComment(persistedReview.getId(), noAuthor, COMMENT);

        List<Comment> reviewComments = reviewRepository.findById(persistedReview.getId()).get().getComments();
        assertEquals(reviewComments.size(), 1);
    }

    @Test
    public void test_addComment_forNonexistingReview_throwsReviewException() throws ReviewException {
        expectReviewExceptionWithMessage("Review with id 1 not found");

        reviewService.addComment(1L, COMMENT_AUTHOR, COMMENT);
    }

    @Test
    @Transactional
    public void test_addComment_withNullComment_throwsReviewException() throws ReviewException  {
        expectReviewExceptionWithMessage("Comment can't be blank");

        Review persistedReview = createDefaultReview();
        String nullComment = null;

        reviewService.addComment(persistedReview.getId(), COMMENT_AUTHOR, nullComment);
    }

    @Test
    @Transactional
    public void test_addComment_withEmptyComment_throwsReviewException() throws ReviewException  {
        expectReviewExceptionWithMessage("Comment can't be blank");

        Review persistedReview = createDefaultReview();
        String emptyComment = "";

        reviewService.addComment(persistedReview.getId(), COMMENT_AUTHOR, emptyComment);
    }

    private Review createDefaultReview() {
        return reviewRepository.save(buildDefaultReview());
    }

}
