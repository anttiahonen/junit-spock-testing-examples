package fi.aalto.testingandqa

import io.restassured.RestAssured
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import spock.lang.Specification

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class IntegrationSpecRestBase extends Specification {

    int port

    @LocalServerPort
    setLocalServerPort(int port) {
        this.port = port
    }

    def setup() {
        RestAssured.port = port
    }

}