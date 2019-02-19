package fi.aalto.testingandqa.reviewservice

import fi.aalto.testingandqa.review.ReviewException
import fi.aalto.testingandqa.review.ReviewRepository
import fi.aalto.testingandqa.review.ReviewService
import fi.aalto.testingandqa.review.models.Review
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification
import spock.lang.Subject

import javax.transaction.Transactional

import static fi.aalto.testingandqa.reviewservice.ReviewServiceSpecUtils.buildDefaultReview_groovy25

@SpringBootTest
class AddCommentISpec extends Specification {

    @Autowired @Subject ReviewService reviewService

    @Autowired ReviewRepository reviewRepository

    def COMMENT_AUTHOR = "Antti A"
    def COMMENT = "Your review2 sucked big time!"

    @Transactional
    def "addComment() with valid comment that has persists the comment to given review"() {
        given: "a persisted review"
            Review persistedReview = createDefaultReview()

        expect: "no comments exists for the created review"
            persistedReview.comments.isEmpty()

        when: "adding a comment for the review"
            reviewService.addComment(persistedReview.id, COMMENT_AUTHOR, COMMENT)

        then: "a new comment is added for review"
            reviewRepository.findById(persistedReview.id).get().comments.size() == 1
    }

    @Transactional
    def "addComment() with valid comment that has author sets the author and body for comment"() {
        given: "a persisted review"
            Review persistedReview = createDefaultReview()

        when: "adding a comment for the review"
            reviewService.addComment(persistedReview.id, COMMENT_AUTHOR, COMMENT)

        then: "author and body are set for comment"
            with(reviewRepository.findById(persistedReview.id).get().comments.first()) {
                author == COMMENT_AUTHOR
                body == COMMENT
            }
    }

    def "addComment() for non existing review throws review exception"() {
        when: "adding comment to non existing review"
            reviewService.addComment(1L, COMMENT_AUTHOR, COMMENT)

        then: "a review exception is thrown"
            thrown ReviewException
    }

    @Transactional
    def "addComment() with null comment throws review exception"() {
        given: "a persisted review"
            Review persistedReview = createDefaultReview()
        and: "a null comment to try to add for the review"
            String nullComment = null

        when: "trying to add the null comment for the review"
            reviewService.addComment(persistedReview.id, COMMENT_AUTHOR, nullComment)

        then: "a review exception is thrown"
            def ex = thrown ReviewException
            ex.message == "Comment can't be blank"
    }

    @Transactional
    def "addComment() with empty comment throws review exception"() {
        given: "a persisted review"
            Review persistedReview = createDefaultReview()
        and: "an empty comment to try to add for the review"
            String emptyComment = ""

        when: "trying to add the null comment for the review"
            reviewService.addComment(persistedReview.id, COMMENT_AUTHOR, emptyComment)

        then: "a review exception is thrown"
            def ex = thrown ReviewException
            ex.message == "Comment can't be blank"
    }

    private Review createDefaultReview() {
        return reviewRepository.save(buildDefaultReview_groovy25())
    }

}