package fi.aalto.testingandqa.review.reviewservice;

import fi.aalto.testingandqa.review.CommentRepository;
import fi.aalto.testingandqa.review.ReviewException;
import fi.aalto.testingandqa.review.ReviewService;
import fi.aalto.testingandqa.review.models.*;
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
@Sql(scripts = "classpath:add-reaction.sql", executionPhase = BEFORE_TEST_METHOD)
public class AddReactionSqlITest {

    @Autowired
    ReviewService reviewService;
    @Autowired
    CommentRepository commentRepository;

    @Test
    @Transactional
    public void testAddReactionForComment_withCommentThatHasExistingReactionForTheGivenType_shouldAppendReactionCount() throws ReviewException {
        ReactionType reactionToGive = ReactionType.LIKE;

        reviewService.addReactionForComment(1L, reactionToGive);

        Comment reactedComment = commentRepository.findById(1L).get();
        Reaction reactionThatWasChanged = reactedComment.getReactions().stream()
                .filter( reaction -> reaction.getType() == reactionToGive )
                .findFirst().get();
        assertEquals(Long.valueOf(reactionThatWasChanged.getAmount()), Long.valueOf(2 + 1));
    }

}
