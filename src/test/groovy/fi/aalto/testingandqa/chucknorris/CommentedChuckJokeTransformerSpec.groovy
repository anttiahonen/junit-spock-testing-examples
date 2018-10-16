package fi.aalto.testingandqa.chucknorris

import spock.lang.Specification
import spock.lang.Unroll

import static fi.aalto.testingandqa.chucknorris.ChuckJokeTransformer.OTHER_STARS

class CommentedChuckJokeTransformerSpec extends Specification {

    def static REPLACE_PATTERN = 'REPLACEWITHSTAR'

    //Unroll is used for individual runs of each of the row in data-driven where-block
    @Unroll
    //test output is injected to feature method with contextDesc and outcome params
    def "replaceOtherNamesThanChuckWith80sMovieStars() with #contextDesc #outcome replace other names"() {
        given: "a chuck norris joke transformer"
            ChuckJokeTransformer jokeTransformer = new ChuckJokeTransformer()
        //having params injected to the labels can produce bdd-reports
        and: "a joke of '#joke' to transform"

        //having params injected to the labels can produce bdd-reports
        //expect is used here to combine simple when&then -blocks
        expect: "replaced joke #outcome have replaced other names"
            newPossibleJokes.contains jokeTransformer.replaceOtherNamesThanChuckWith80sMovieStars(joke)

        where: //there are a total of 4 params (joke, newPossibleJokes, contextDesc, outocme) in each test
        joke                                                                                                                                | _ //here we split the long table with | _
        "Chuck Norris once started a fire with only what was around him. He was on an iceberg."                                             | _
        "When Chuck Norris was a bartender he invented a drink called the \"Colon Slammer\". It was made with Vodka and Prune juice."       | _
        "Lance Armstrong finally admitted to his steroid use simply because Chuck Norris warned him that he would bite the other one off."  | _
        "Chuck Norris is So Badass, he went to the island of Coradie to kick Ganon's Ass."                                                  | _
        "the manny pacquiao-floyd mayweather, jr. fight will only push through if CHUCK NORRIS says so..."                                  | _

        newPossibleJokes << [
                ["Chuck Norris once started a fire with only what was around him. He was on an iceberg."],
                //we can use a static method, closure or variable (or @Shared closure, variable) inside the where block
                getPossibleJokes("When Chuck Norris was a bartender he invented a drink called the \"$REPLACE_PATTERN\". It was made with Vodka and Prune juice."),
                getPossibleJokes("$REPLACE_PATTERN finally admitted to his steroid use simply because Chuck Norris warned him that he would bite the other one off."),
                getPossibleJokes("Chuck Norris is $REPLACE_PATTERN, he went to the island of Coradie to kick $REPLACE_PATTERN."),
                ["the manny pacquiao-floyd mayweather, jr. fight will only push through if CHUCK NORRIS says so..."]

        ]
        contextDesc << ["with a joke containing only name Chuck Norris", "with Chuck Norris after sentence start and another name inside quotes",
                        "with a joke starting with other star", "with joke containing multiple 'names' and special chars attached to names",
                        "with a joke containing Chuck Norris in all caps"]
        outcome << ["should not", "should", "should", "should", "should not replace CHUCK NORRIS or"]
    }


    private static List<String> getPossibleJokes(String jokeTemplate) {
        List<String> possibleJokes = []
        boolean containsMultipleNamesToReplace =
                jokeTemplate.replaceAll(REPLACE_PATTERN, "").size() < jokeTemplate.replaceFirst(REPLACE_PATTERN, "").size()
        if (containsMultipleNamesToReplace) {
            possibleJokes = OTHER_STARS.collect { jokeTemplate.replaceFirst(REPLACE_PATTERN, it) }
            while (possibleJokes.any { it.contains(REPLACE_PATTERN)} )
                possibleJokes = OTHER_STARS
                        .collectMany { star -> possibleJokes.collect { joke -> joke.replaceFirst(REPLACE_PATTERN, star) } }
        }
        else
            possibleJokes = OTHER_STARS.collect { jokeTemplate.replace(REPLACE_PATTERN, it) }

        possibleJokes
    }

}