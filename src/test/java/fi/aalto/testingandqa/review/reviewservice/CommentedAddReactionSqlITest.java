package fi.aalto.testingandqa.review.reviewservice;

import fi.aalto.testingandqa.review.CommentRepository;
import fi.aalto.testingandqa.review.ReviewException;
import fi.aalto.testingandqa.review.ReviewService;
import fi.aalto.testingandqa.review.models.Comment;
import fi.aalto.testingandqa.review.models.Reaction;
import fi.aalto.testingandqa.review.models.ReactionType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@SpringBootTest
@RunWith(SpringRunner.class)
//With sql annotation, we can run any kind of sql to database in tests. This test case is an example why its a bad idea to create test data with scripts.
//Some valid uses for sql-scripts in tests might be for example schema creating or having table truncations happen with script before each test method
@Sql(scripts = "classpath:add-reaction.sql", executionPhase = BEFORE_TEST_METHOD)
public class CommentedAddReactionSqlITest {

    @Autowired
    ReviewService reviewService;
    @Autowired
    CommentRepository commentRepository;

    @Test
    @Transactional
    public void testAddReactionForComment_withCommentThatHasExistingReactionForTheGivenType_shouldAppendReactionCount() throws ReviewException {
        ReactionType reactionToGive = ReactionType.LIKE;

        //why do we insert 1L here? what does it mean
        reviewService.addReactionForComment(1L, reactionToGive);

        //again, why 1L?
        //what kind of comments are there in the db?
        Comment reactedComment = commentRepository.findById(1L).get();
        Reaction reactionThatWasChanged = reactedComment.getReactions().stream()
                .filter( reaction -> reaction.getType() == reactionToGive )
                .findFirst().get();
        //why do we have 2 + 1 on the other side of assertion?
        assertEquals(Long.valueOf(reactionThatWasChanged.getAmount()), Long.valueOf(2 + 1));
    }

}
