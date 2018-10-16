package fi.aalto.testingandqa.review.reviewservice;

import fi.aalto.testingandqa.review.CommentRepository;
import fi.aalto.testingandqa.review.ReviewException;
import fi.aalto.testingandqa.review.ReviewRepository;
import fi.aalto.testingandqa.review.ReviewService;
import fi.aalto.testingandqa.review.models.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;


@SpringBootTest
@RunWith(SpringRunner.class)
public class CommentedAddReactionITest {

    @Autowired
    ReviewService reviewService;
    @Autowired
    ReviewRepository reviewRepository;
    @Autowired
    CommentRepository commentRepository;

    @Test
    @Transactional
    public void testAddReactionForComment_withCommentThatHasExistingReactionForTheGivenType_shouldAppendReactionCount() throws ReviewException  {
        ReactionType reactionToGive = ReactionType.LIKE;
        int existingReactionTypeCount = 2;
        //here we build the whole object and its childs with a grap-notation. It is easy to see, how different objects relate
        //to each other.
        Review reviewWithCommentsAndReactions = ReviewBuilder.aReview()
                .withTitle("Great movie!")
                .withBody("Absolute classic of a movie!")
                .withComments(Arrays.asList(
                        CommentBuilder.aComment()
                                .withAuthor("Antti A")
                                .withBody("Great review!")
                                .withReactions(new HashSet<>(Arrays.asList(
                                        ReactionBuilder.aReaction() //we can see that here is the existing reaction build for a certain comment
                                                                    //with type and reaction amount in place
                                                .withType(reactionToGive)
                                                .withAmount(existingReactionTypeCount)
                                                .build(),
                                        ReactionBuilder.aReaction()
                                                .withType(ReactionType.ANGRY)
                                                .withAmount(1)
                                                .build()
                                )))
                                .build(),
                        CommentBuilder.aComment()
                                .withAuthor("")
                                .withBody("Your review is not valid")
                                .build()
                ))
                .build();
        reviewWithCommentsAndReactions = reviewRepository.save(reviewWithCommentsAndReactions); //here we save the object & childs with repository
        Comment commentForReaction = reviewWithCommentsAndReactions.getComments().get(0);

        //in here its visible from the test that we are reacting to the first comment with a certain reaction
        reviewService.addReactionForComment(commentForReaction.getId(), reactionToGive);


        Comment reactedComment = commentRepository.findById(commentForReaction.getId()).get();
        Reaction reactionThatWasChanged = reactedComment.getReactions().stream()
                .filter( reaction -> reaction.getType() == reactionToGive )
                .findFirst().get();
        //in here, its clear from the test that the reaction amount for a certain has increased with one after the action
        assertEquals(Long.valueOf(reactionThatWasChanged.getAmount()), Long.valueOf(existingReactionTypeCount + 1));
    }

}
