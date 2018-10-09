package fi.aalto.testingandqa.review;

import fi.aalto.testingandqa.review.models.Error;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ErrorRepository extends CrudRepository<Error, Long> {

}
