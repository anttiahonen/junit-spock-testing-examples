package fi.aalto.testingandqa.chucknorris

import fi.aalto.testingandqa.IntegrationSpecRestBase
import io.restassured.RestAssured
import org.spockframework.spring.SpringBean
import org.springframework.http.HttpStatus

import static org.hamcrest.Matchers.containsString

class ChuckNorrisControllerWithMocksRestSpec extends IntegrationSpecRestBase {

    def ENDPOINT = "/chucknorris/randomjoke_refactored"

    @SpringBean
    ChuckApiClient chuckApiClient = Mock()

    def "getting random chuck joke with working external api should return a Chuck Norris Joke"() {
        given: "endpoint for getting random chuck joke with other people replaced with movie stars"
        when: "calling the endpoint"
            def response = RestAssured.when().get(ENDPOINT).then()

        then: "a random Chuck-joke is retrieved with api"
            1 * chuckApiClient.getRandomChuckNorrisJoke() >> new ChuckJoke().tap {
                category = []
                iconUrl = "iconurl"
                url = "url"
                value = "Chuck Norris once started a fire with only what was around him. He was on an iceberg."
            }
        then: "should return 200 OK"
            response.statusCode HttpStatus.OK.value()

        and: "joke should contain Chuck Norris"
            response.body containsString("Chuck")
    }

    def "getting random chuck joke with non-working external api should return status INTERNAL SERVER ERROR"() {
        given: "endpoint for getting random chuck joke with other people replaced with movie stars"
        when: "calling the endpoint"
            def response = RestAssured.when().get(ENDPOINT).then()

        then: "connection to Chuck-api is not working"
            1 * chuckApiClient.getRandomChuckNorrisJoke() >> { throw new ChuckException("Could not obtain a new chuck joke") }
        and: "should return 500 INTERNAL SERVER ERROR"
            response.statusCode HttpStatus.INTERNAL_SERVER_ERROR.value()
        and: "Connection error message is returned"
            response.body "message", containsString("Connection not working")
    }

}