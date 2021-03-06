package fi.aalto.testingandqa.review.reviewservice;

import fi.aalto.testingandqa.review.*;
import fi.aalto.testingandqa.review.models.Error;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.jdbc.datasource.lookup.DataSourceLookupFailureException;

import java.util.Optional;

public class AddCommentTest extends ReviewServiceBase {

    ReviewService reviewService;
    ReviewRepository reviewRepository;
    ErrorRepository errorRepository;
    CommentRepository commentRepository;

    @Before
    public void setupReviewServiceWithMockRepository() {
        reviewRepository = Mockito.mock(ReviewRepository.class);
        errorRepository = Mockito.mock(ErrorRepository.class);
        commentRepository = Mockito.mock(CommentRepository.class);
        reviewService = new ReviewService(reviewRepository, errorRepository, commentRepository);
    }

    @Test
    public void test_addComment_whenDatasourceIsUnavailable_throwsReviewException_andCreatesNewErrorRow() throws ReviewException {
        expectReviewExceptionWithMessage("Commenting is disabled because DBA has broken our connection");

        Mockito
                .when(reviewRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(buildDefaultReview()));
        Mockito
                .when(reviewRepository.save(Mockito.any()))
                .thenThrow(new DataSourceLookupFailureException("Datasource not available"));
        String author = "Antti";
        String comment = "That review is very good!!!!";

        reviewService.addComment(1L, author, comment);

        Mockito.verify(errorRepository, Mockito.times(1)).save(Mockito.any(Error.class));
    }

}
