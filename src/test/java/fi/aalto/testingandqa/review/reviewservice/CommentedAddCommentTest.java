package fi.aalto.testingandqa.review.reviewservice;

import fi.aalto.testingandqa.review.*;
import fi.aalto.testingandqa.review.models.Error;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.jdbc.datasource.lookup.DataSourceLookupFailureException;

import java.util.Optional;

//no context loading annotations inside a unit test.
//in here we extend a base class providing some functionality for multiple test cases.
//do not over eagerly use inheritance, it can lead into hard to follow tests
public class CommentedAddCommentTest extends ReviewServiceBase {

    //class under test
    ReviewService reviewService;
    //the dependencies of ReviewService
    ReviewRepository reviewRepository;
    ErrorRepository errorRepository;
    CommentRepository commentRepository;

    @Before // a test fixture method for providing fresh set of mocks for each test method
    public void setupReviewServiceWithMockRepository() {
        reviewRepository = Mockito.mock(ReviewRepository.class); // mock object for substituting real dependency & its actions
        errorRepository = Mockito.mock(ErrorRepository.class); //mock
        commentRepository = Mockito.mock(CommentRepository.class); //mock
        //class under test created with mocked dependencies
        reviewService = new ReviewService(reviewRepository, errorRepository, commentRepository);
    }

    @Test
    public void test_addComment_whenDatasourceIsUnavailable_throwsReviewException_andCreatesNewErrorRow() throws ReviewException {
        //assert: checking exceptions must be done before act-part
        expectReviewExceptionWithMessage("Commenting is disabled because DBA has broken our connection");

        //arrange: wiring the mock object interactions
        Mockito
                //when given mock object, reviewRepository and its method findById is called with any long-type parameter
                .when(reviewRepository.findById(Mockito.anyLong()))
                //then we stub the return value with value that we desire, instead of actually hitting the database
                .thenReturn(Optional.of(buildDefaultReview()));
        Mockito  //when given mock object, reviewRepository and its method save is called with parameter of any type
                .when(reviewRepository.save(Mockito.any()))
                //we throw a new DataSourceLookupFailureException, which would be hard to encounter inside an integration-test
                .thenThrow(new DataSourceLookupFailureException("Datasource not available"));
        String author = "Antti";
        String comment = "That review is very good!!!!";

        //act
        reviewService.addComment(1L, author, comment);

        //assert: verifying mock object actions, we are only interested in checking that something is called inside the execution path
        //This one reads: verify that errorRepository.save is called exactly 1 time with a parameter of our projects own Error-class type.
        Mockito.verify(errorRepository, Mockito.times(1)).save(Mockito.any(Error.class));
    }

}
