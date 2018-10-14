package fi.aalto.testingandqa.chucknorris;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ChuckJokeTransformer {

    private static final List<String> OTHER_STARS = Arrays.asList(
            "Sylvester Stallone",
            "Arnold Schwarzenegger",
            "Steven Seagal"
    );

    private boolean previousIndexContainsName;
    private String previousIndexWord;

    public ChuckJokeTransformer() {
        previousIndexWord = null;
        previousIndexContainsName = false;
    }

    public String replaceOtherNamesThanChuckWith80sMovieStars(String chuckNorrisJoke) {
        String[] wordsInJoke = chuckNorrisJoke.split(" ");
        List<String> newJokeWords = new ArrayList<>();

        for (String word : wordsInJoke) {
            boolean wordFirstCharactersIsUpperCase = Character.isUpperCase(word.codePointAt(0));
            if (wordFirstCharactersIsUpperCase && previousIndexContainsName) {
                if (previousIndexWord.contains("Chuck") && word.contains("Norris")) {
                    addActualPreviousWordAndCurrentWord(newJokeWords, word);
                } else {
                    replaceOtherNameWith80sMovieStar(newJokeWords);
                }
            } else if (!wordFirstCharactersIsUpperCase && previousIndexContainsName) {
                addActualPreviousWordAndCurrentWord(newJokeWords, word);
            } else if (wordFirstCharactersIsUpperCase) {
                startRecordingWords(word);
            } else
                newJokeWords.add(word);
        }

        String newJoke = String.join(" ", newJokeWords);

        return newJoke;
    }

    private void startRecordingWords(String word) {
        previousIndexContainsName = true;
        previousIndexWord = word;
    }

    private void replaceOtherNameWith80sMovieStar(List<String> newJokeWords) {
        Random random = new Random();
        int index = random.nextInt(OTHER_STARS.size());
        String otherStar = OTHER_STARS.get(index);
        newJokeWords.add(otherStar);
    }

    private void addActualPreviousWordAndCurrentWord(List<String> newJokeWords, String word) {
        newJokeWords.add(previousIndexWord);
        newJokeWords.add(word);
        previousIndexWord = null;
        previousIndexContainsName = false;
    }


}
