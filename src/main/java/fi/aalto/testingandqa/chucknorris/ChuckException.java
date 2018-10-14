package fi.aalto.testingandqa.chucknorris;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Connection not working")
    public class ChuckException extends Exception {

    public ChuckException(String message) {
        super(message);
    }

}
