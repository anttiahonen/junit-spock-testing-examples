package fi.aalto.testingandqa.review;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.Assert.assertEquals;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ReviewServiceITest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Autowired
    ReviewService reviewService;

    @Autowired
    ReviewRepository reviewRepository;
    
    static String COMMENT_AUTHOR = "Antti A";
    static String COMMENT = "Your review sucked big time!";

    @Test
    @Transactional
    public void test_addComment_withValidCommentThatHasAuthor_persistsTheCommentToGivenReview() throws ReviewException {
        Review persistedReview = createDefaultReview();
        
        reviewService.addComment(persistedReview.getId(), COMMENT_AUTHOR, COMMENT);
        
        List<Comment> reviewComments = reviewRepository.findById(persistedReview.getId()).get().getComments();
        assertEquals(reviewComments.size(), 1);
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
    public void test_addComment_withNullComment_throwsReviewException() throws ReviewException  {
        expectReviewExceptionWithMessage("Comment can't be blank");

        Review persistedReview = createDefaultReview();
        String nullComment = null;

        reviewService.addComment(persistedReview.getId(), COMMENT_AUTHOR, nullComment);
    }

    @Test
    public void test_addComment_withEmptyComment_throwsReviewException() throws ReviewException  {
        expectReviewExceptionWithMessage("Comment can't be blank");

        Review persistedReview = createDefaultReview();
        String emptyComment = "";

        reviewService.addComment(persistedReview.getId(), COMMENT_AUTHOR, emptyComment);
    }

    private void expectReviewExceptionWithMessage(String msg) {
        thrown.expect(ReviewException.class);
        thrown.expectMessage(msg);
    }
    
    private Review createDefaultReview() {
        Review persistedReview = new Review();
        persistedReview.setTitle("Movie of the century");
        persistedReview.setBody("So good movie!");
        return reviewRepository.save(persistedReview);
    }

}
