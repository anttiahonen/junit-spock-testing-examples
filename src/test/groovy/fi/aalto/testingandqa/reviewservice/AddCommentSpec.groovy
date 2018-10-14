package fi.aalto.testingandqa.reviewservice

import spock.lang.Subject

import static fi.aalto.testingandqa.reviewservice.ReviewServiceSpecUtils.buildDefaultReview_groovy25

import fi.aalto.testingandqa.review.ReviewException
import fi.aalto.testingandqa.review.models.Error
import fi.aalto.testingandqa.review.models.Review
import fi.aalto.testingandqa.review.CommentRepository
import fi.aalto.testingandqa.review.ErrorRepository
import fi.aalto.testingandqa.review.ReviewRepository
import fi.aalto.testingandqa.review.ReviewService
import org.springframework.jdbc.datasource.lookup.DataSourceLookupFailureException
import spock.lang.Specification

class AddCommentSpec extends Specification {

    @Subject ReviewService reviewService
    ReviewRepository reviewRepository
    ErrorRepository errorRepository
    CommentRepository commentRepository

    def setup() {
        reviewRepository = Mock()
        errorRepository = Mock(ErrorRepository)
        commentRepository = Mock(CommentRepository.class)
        reviewService = new ReviewService(reviewRepository, errorRepository, commentRepository)
    }

    def "addComment() when data source is unavailable throws review exception and creates new error row"() {
        given: "a review for commenting"
            def reviewId = 1L
        and: "author and body for comment"
            def author = "Antti"
            def comment = "That review is very good!!!!"

        when: "trying to create the given comment for author"
            reviewService.addComment(reviewId, author, comment)

        then: "review is found from db"
            1 * reviewRepository.findById(reviewId) >> Optional.of(buildDefaultReview_groovy25())
        and: "while trying to save comment for review the datasource is not available"
            1 * reviewRepository.save(_ as Review) >> { throw new DataSourceLookupFailureException("Datasource not available") }
        and: "an error-report should be generated to db"
            1 * errorRepository.save(_ as Error)
        and: "review exception is thrown"
            def ex = thrown ReviewException
            ex.message == "Commenting is disabled because DBA has broken our connection"
    }

}