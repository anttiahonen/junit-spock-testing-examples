package fi.aalto.testingandqa.review;

import fi.aalto.testingandqa.review.models.Comment;
import org.springframework.data.repository.CrudRepository;

public interface CommentRepository extends CrudRepository<Comment, Long> {

}
