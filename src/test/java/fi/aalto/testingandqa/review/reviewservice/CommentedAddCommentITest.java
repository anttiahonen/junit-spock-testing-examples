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

//with these two annotations, the spring context is initialized for test case.
//configuring test context is a beast of a task in itself, luckily this is not a part of this course.
@SpringBootTest
@RunWith(SpringRunner.class)
//in here we extend a base class providing some functionality for multiple test cases.
//do not over eagerly use inheritance, it can lead into hard to follow tests
public class CommentedAddCommentITest extends ReviewServiceBase {

    //here we get ReviewService initialized by the Spring dependency injection container
    //this service has actual dependencies in place.
    @Autowired
    ReviewService reviewService;

    //here we get ReviewRepository initialized by the Spring dependency injection container
    @Autowired
    ReviewRepository reviewRepository;
    
    static String COMMENT_AUTHOR = "Antti A";
    static String COMMENT = "Your review2 sucked big time!";

    @Test
    //Transactional annotation is used here to guarantee an isolated db-context for test method
    @Transactional
    public void test_addComment_withValidCommentThatHasAuthor_persistsTheCommentToGivenReview() throws ReviewException {
        //arrange
        Review persistedReview = createDefaultReview();

        //act
        reviewService.addComment(persistedReview.getId(), COMMENT_AUTHOR, COMMENT);


        //assert
        //here we fetch from actual in-memory db the changed review and its comments to do assertions against
        List<Comment> reviewComments = reviewRepository.findById(persistedReview.getId()).get().getComments();
        assertEquals(reviewComments.size(), 1);
        Comment firstComment = reviewComments.get(0);
        assertEquals(firstComment.getAuthor(), COMMENT_AUTHOR);
        assertEquals(firstComment.getBody(), COMMENT);
    }

    @Test
    //Transactional annotation is used here to guarantee an isolated db-context for test method
    @Transactional
    public void test_addComment_withValidCommentWithoutAuthor_persistsTheCommentToGivenReview() throws ReviewException {
        //arrange
        Review persistedReview = createDefaultReview();
        //java doesn't have named parameters, its good to add null values to well named variables to help to understand what certai null means
        String noAuthor = null;

        //act --> eg here we can see that null means noAuthor
        reviewService.addComment(persistedReview.getId(), noAuthor, COMMENT);
        //compare: reviewService.addComment(persistedReview.getId(), null, COMMENT);
        //we need to check the code under test to understand what is null there without the variable assign

        //assert
        List<Comment> reviewComments = reviewRepository.findById(persistedReview.getId()).get().getComments();
        assertEquals(reviewComments.size(), 1);
    }

    @Test
    public void test_addComment_forNonexistingReview_throwsReviewException() throws ReviewException {
        //assert: when testing exception message in JUnit, it needs to be defined before the act
        expectReviewExceptionWithMessage("Review with id 1 not found");

        //act
        reviewService.addComment(1L, COMMENT_AUTHOR, COMMENT);
    }

    @Test
    //Transactional annotation is used here to guarantee an isolated db-context for test method
    @Transactional
    public void test_addComment_withNullComment_throwsReviewException() throws ReviewException  {
        //assert
        expectReviewExceptionWithMessage("Comment can't be blank");

        //arrange
        Review persistedReview = createDefaultReview();
        //once again null is assigned to variable, helps to understand what is given as null in the act part.
        String nullComment = null;

        //act
        reviewService.addComment(persistedReview.getId(), COMMENT_AUTHOR, nullComment);
    }

    @Test
    //Transactional annotation is used here to guarantee an isolated db-context for test method
    @Transactional
    public void test_addComment_withEmptyComment_throwsReviewException() throws ReviewException  {
        //assert
        expectReviewExceptionWithMessage("Comment can't be blank");

        //arrange
        Review persistedReview = createDefaultReview();
        String emptyComment = "";

        //act
        reviewService.addComment(persistedReview.getId(), COMMENT_AUTHOR, emptyComment);
    }

    //helper factory method for persisting object to database
    private Review createDefaultReview() {
        return reviewRepository.save(buildDefaultReview());
    }

}
