package fi.aalto.testingandqa.review;

import fi.aalto.testingandqa.review.models.*;
import fi.aalto.testingandqa.review.models.Error;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.lookup.DataSourceLookupFailureException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
public class ReviewService {

    private ReviewRepository reviewRepository;
    private ErrorRepository errorRepository;
    private CommentRepository commentRepository;


    @Autowired
    public ReviewService(ReviewRepository reviewRepository,
                         ErrorRepository errorRepository,
                         CommentRepository commentRepository) {
        this.reviewRepository = reviewRepository;
        this.errorRepository = errorRepository;
        this.commentRepository = commentRepository;
    }

    public void addComment(Long reviewId, String author, String comment) throws ReviewException {
        Optional<Review> reviewToAddComment = reviewRepository.findById(reviewId);

        if (reviewToAddComment.isPresent()) {
            Review reviewToComment = reviewToAddComment.get();

            if (StringUtils.isEmpty(comment)) {
                throw new ReviewException("Comment can't be blank");
            }

            Comment commentToAdd = new Comment();
            commentToAdd.setAuthor(author);
            commentToAdd.setBody(comment);

            reviewToComment.getComments().add(commentToAdd);

            try {
                reviewRepository.save(reviewToComment);
            } catch (DataSourceLookupFailureException ex) {
                Error error = new Error();
                error.setReason("Connection error");
                errorRepository.save(error);
                throw new ReviewException("Commenting is disabled because DBA has broken our connection", ex);
            }
        } else {
            throw new ReviewException(String.format("Review with id %d not found", reviewId));
        }
    }

    public void addReactionForComment(Long commentId, ReactionType type) throws ReviewException {
        if (type == null)
            throw new ReviewException("No reaction type given for comment");

        Optional<Comment> commentForReaction = commentRepository.findById(commentId);
        if (commentForReaction.isPresent()) {
            Comment reactionComment = commentForReaction.get();
            reactionComment.addReaction(type);
            commentRepository.save(reactionComment);
        } else {
            throw new ReviewException(String.format("Comment with id %d not found", commentId));
        }
    }

}
