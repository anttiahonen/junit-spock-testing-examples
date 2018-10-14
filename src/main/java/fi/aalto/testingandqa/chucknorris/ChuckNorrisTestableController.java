package fi.aalto.testingandqa.chucknorris;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chucknorris")
public class ChuckNorrisTestableController {

    private ChuckApiClient chuckApiClient;
    private ChuckJokeTransformer chuckJokeTransformer;

    @Autowired
    public ChuckNorrisTestableController(ChuckApiClient chuckApiClient, ChuckJokeTransformer chuckJokeTransformer) {
        this.chuckApiClient = chuckApiClient;
        this.chuckJokeTransformer = chuckJokeTransformer;
    }

    @RequestMapping("/randomjoke_refactored")
    public ResponseEntity<String> getRandomJokeWithChuckAndOtherStar() throws ChuckException {
        String chuckNorrisJoke = chuckApiClient.getRandomChuckNorrisJoke();

        String newJokeWithPossiblyNamesReplaced = chuckJokeTransformer.
                replaceOtherNamesThanChuckWith80sMovieStars(chuckNorrisJoke);

        String htmlContent = String.format("New joke: %s <br> Old joke: %s", newJokeWithPossiblyNamesReplaced, chuckNorrisJoke);

        return new ResponseEntity<>(htmlContent, HttpStatus.OK);
    }

}
