import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class PatientIntegrationTest {
    @BeforeAll
    static void setUp(){
        RestAssured.baseURI = "http://localhost:4004";
    }

    @Test
    public void shouldReturnPatientsWithValidToken(){
        String loginPayload = """
                {
                    "email" : "testuser@test.com",
                    "password" : "password123"
                }
            """;

        String token = given()     //Setting up the body of the response
                .contentType("application/json")
                .body(loginPayload)
                .when()                             //Acting
                .post("/auth/login")
                .then()                             //Asserting
                .statusCode(200)
                .extract()
                .jsonPath()
                .get("token");

        System.out.println("Token: " + token);

        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/api/patients")
                .then()
                .statusCode(200)
                .body("patients", notNullValue());
    }

}
