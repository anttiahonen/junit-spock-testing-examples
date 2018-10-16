package fi.aalto.testingandqa.chucknorris

import fi.aalto.testingandqa.IntegrationSpecRestBase
import io.restassured.RestAssured
import org.spockframework.spring.SpringBean
import org.springframework.http.HttpStatus

import static org.hamcrest.Matchers.containsString

class CommentedChuckNorrisControllerWithSpyRestSpec extends IntegrationSpecRestBase {

    def ENDPOINT = "/chucknorris/randomjoke_refactored"

    @SpringBean //in spock 1.2 we can mock a spring context bean easily with @SpringBean annotation
    //Spy objects can be used for partial mocking & stubbing. Some method calls with the Spy-object can be done as the actual ones, and some can be stubbed.
    ChuckApiClient chuckApiClient = Spy() //any kind of mock object (mock,stub,spy) can be injected to spring context with @SpringBean

    //In this test, we are actually calling the chuck norris api. What if the api is down momentarily and our builds were failing with this test?
    def "getting random chuck joke with working external api should return a Chuck Norris Joke"() {
        given: "endpoint for getting random chuck joke with other people replaced with movie stars"
        when: "calling the endpoint"
            def response = RestAssured.when().get(ENDPOINT).then() //rest assured is a bdd-style fluent api for rest testing
                                                                   //here we make define a get-method call to given endpoint - > RestAssured.when().get(ENDPOINT)
                                                                   //and then actually call it with -> .then()

        then: "should return 200 OK"
            //checking response status code with Rest-assured
            //in Groovy, paranthesis can be omitted in many situations, line below is the same as response.statusCode(HttpStatus.OK.value())
            response.statusCode HttpStatus.OK.value()

        and: "joke should contain Chuck Norris"
            //with a plain text response body, the response (joke) is directly at the root of body
            response.body containsString("Chuck")
    }

    def "getting random chuck joke with non-working external api should return status INTERNAL SERVER ERROR"() {
        given: "endpoint for getting random chuck joke with other people replaced with movie stars"
        when: "calling the endpoint"
            def response = RestAssured.when().get(ENDPOINT).then()

        then: "connection to Chuck-api is not working"
            //here we stub the chuck joke getting with an Exception (simulating not available connection)
            1 * chuckApiClient.getRandomChuckNorrisJoke() >> { throw new ChuckException("Could not obtain a new chuck joke") }
        and: "should return 500 INTERNAL SERVER ERROR"
            response.statusCode HttpStatus.INTERNAL_SERVER_ERROR.value()
        and: "Connection error message is returned"
            //when checking an json-object with rest-assured, we can give the first param json-path for body-method. Second param is a hamcrest-matcher
            response.body "message", containsString("Connection not working")
    }

}