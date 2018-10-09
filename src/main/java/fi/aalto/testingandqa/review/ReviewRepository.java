package fi.aalto.testingandqa.review;

import fi.aalto.testingandqa.review.models.Review;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends CrudRepository<Review, Long> {

}
