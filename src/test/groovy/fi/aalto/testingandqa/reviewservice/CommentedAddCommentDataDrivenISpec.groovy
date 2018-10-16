package fi.aalto.testingandqa.reviewservice

import fi.aalto.testingandqa.review.ReviewException
import fi.aalto.testingandqa.review.ReviewRepository
import fi.aalto.testingandqa.review.ReviewService
import fi.aalto.testingandqa.review.models.Review
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

import javax.transaction.Transactional

import static fi.aalto.testingandqa.reviewservice.ReviewServiceSpecUtils.buildDefaultReview_groovy25

@SpringBootTest
class CommentedAddCommentDataDrivenISpec extends Specification {

    @Autowired @Subject ReviewService reviewService

    @Autowired ReviewRepository reviewRepository

    Review persistedReview
    def COMMENT_AUTHOR = "Antti A"
    def COMMENT = "Your review2 sucked big time!"

    //here we have refactored the creation of review out from tests, as it is needed in almost every test
    def setup() {
        persistedReview = createDefaultReview()
    }

    @Transactional
    def "addComment() with valid comment that has persists the comment to given review"() {
        given: "a persisted review" //we still want to leave a block-with comment, so that it is directly visible from feature method what the context is

        expect: "no comments exists for the created review"
            persistedReview.comments.isEmpty()

        when: "adding a comment for the review"
            reviewService.addComment(persistedReview.id, COMMENT_AUTHOR, COMMENT)

        then: "a new comment is added for review"
            reviewRepository.findById(persistedReview.id).get().comments.size() == 1
    }

    @Transactional
    def "addComment() with valid comment that has author sets the author and body for comment"() {
        given: "a persisted review" //we still want to leave a block-with comment, so that it is directly visible from feature method what the context is

        when: "adding a comment for the review"
            reviewService.addComment(persistedReview.id, COMMENT_AUTHOR, COMMENT)

        then: "author and body are set for comment"
            with(reviewRepository.findById(persistedReview.id).get().comments.first()) {
                author == COMMENT_AUTHOR
                body == COMMENT
            }
    }

    def "addComment() for non existing review throws review exception"() {
        given: "nonexisting review id" //with this block and variable assign, we make immediately visible what the 666L means
            def nonExistingReviewId = 666L
        when: "adding comment to non existing review"
            reviewService.addComment(nonExistingReviewId, COMMENT_AUTHOR, COMMENT)

        then: "a review exception is thrown"
            def ex = thrown ReviewException
            ex.message == "Review with id $nonExistingReviewId not found"
    }

    @Transactional
    //Unroll is used for individual runs of each of the row in data-driven where-block
    @Unroll
    //in here, #commentDescription is used for injecting info to the feature method output
    def "addComment() with #commentDescription comment throws review exception"() {
        given: "a persisted review"

        when: "trying to add the #commentDescription comment for the review"
            reviewService.addComment(persistedReview.id, COMMENT_AUTHOR, comment) //comments is param from where-block

        then: "a review exception is thrown"
            def ex = thrown ReviewException
            ex.message == "Comment can't be blank"

        where: //data driven params and tests with them
        //column is a parameter, first is the param name and then comes the values
        //here we have two params, comment and commentDescription
        comment | commentDescription
        ""      | "blank" //each row is a separate test with the given params
        null    | "null"
    }


    private Review createDefaultReview() {
        return reviewRepository.save(buildDefaultReview_groovy25())
    }

}