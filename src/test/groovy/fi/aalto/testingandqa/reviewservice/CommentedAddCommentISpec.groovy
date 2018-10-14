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
class CommentedAddCommentISpec extends Specification {

    @Autowired @Subject ReviewService reviewService //subject can be used for highlighting the class under test

    @Autowired ReviewRepository reviewRepository //autowired is used for fetching dependency injection context created classes

    def COMMENT_AUTHOR = "Antti A"
    def COMMENT = "Your review2 sucked big time!"

    @Transactional
    //In groovy, method names can be defined with GString objects (strings). This is a powerful feature for testing.
    //The naming format is the same as with examples seen earlier with xUnit testing frameworks
    //   action      | context                   | expected outcome
    def "addComment() with valid comment that has persists the comment to given review"() {
        given: "a persisted review" //given is used to setup test context
            Review persistedReview = createDefaultReview()

        expect: "no comments exists for the created review" //expect can be used to assert state before the tested action has been called
            persistedReview.comments.isEmpty()

        when: "adding a comment for the review" //when is reserved for tested action. This should be as concise as possible!
            reviewService.addComment(persistedReview.id, COMMENT_AUTHOR, COMMENT)

        then: "a new comment is added for review" //then is used to do the assertions against the action results
            reviewRepository.findById(persistedReview.id).get().comments.size() == 1
    }

    @Transactional
    def "addComment() with valid comment that has author sets the author and body for comment"() {
        given: "a persisted review" //given is used to setup test context
            Review persistedReview = createDefaultReview()

        when: "adding a comment for the review" //when is reserved for tested action. This should be as concise as possible!
            reviewService.addComment(persistedReview.id, COMMENT_AUTHOR, COMMENT)

        then: "author and body are set for comment" //then is used to do the assertions against the action results
            with(reviewRepository.findById(persistedReview.id).get().comments.first()) {
                author == COMMENT_AUTHOR
                body == COMMENT
            }
    }

    def "addComment() for non existing review throws review exception"() {
        when: "adding comment to non existing review" //when is reserved for tested action. This should be as concise as possible!
            reviewService.addComment(1L, COMMENT_AUTHOR, COMMENT)

        then: "a review exception is thrown" //then is used to do the assertions against the action results
            def ex = thrown ReviewException //here we are expecting that a ReviewException is thrown as a result
                                            //if we are only interested about exception type, use 'thrown ReviewException'
            ex.message == "Review with id 1 not found" //and here we check the message in the exception
    }

    @Transactional
    def "addComment() with null comment throws review exception"() {
        given: "a persisted review" //given is used to setup test context
            Review persistedReview = createDefaultReview()
        and: "a null comment to try to add for the review" //some more context here chained with and-block
            String nullComment = null

        when: "trying to add the null comment for the review" //when is reserved for tested action. This should be as concise as possible!
            reviewService.addComment(persistedReview.id, COMMENT_AUTHOR, nullComment)

        then: "a review exception is thrown"
            def ex = thrown ReviewException
            ex.message == "Comment can't be blank"
    }

    @Transactional
    def "addComment() with empty comment throws review exception"() {
        given: "a persisted review" //given is used to setup test context
            Review persistedReview = createDefaultReview()
        and: "an empty comment to try to add for the review" //some more context here chained with and-block
            String emptyComment = ""

        when: "trying to add the null comment for the review" //when is reserved for tested action. This should be as concise as possible!
            reviewService.addComment(persistedReview.id, COMMENT_AUTHOR, emptyComment)

        then: "a review exception is thrown"
            def ex = thrown ReviewException
            ex.message == "Comment can't be blank"
    }

    private Review createDefaultReview() {
        return reviewRepository.save(buildDefaultReview_groovy25())
    }

}