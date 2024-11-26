package tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utils.SchemaValidator;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class ReqresApiTest {

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "https://reqres.in";
    }

    @Test
    public void testGetSingleUser() {
        Response response = given()
                .when()
                .get("/api/users/2")
                .then()
                .statusCode(200)
                .extract().response();

        SchemaValidator.validate(response.asString(), "src/test/resources/schemas/singleUserSchema.json");
        response.then().body("data.id", equalTo(2));
    }

    @Test
    public void testCreateUser() {
        String payload = "{ \"name\": \"morpheus\", \"job\": \"leader\" }";

        Response response = given()
                .header("Content-Type", "application/json")
                .body(payload)
                .when()
                .post("/api/users")
                .then()
                .statusCode(201)
                .extract().response();

        SchemaValidator.validate(response.asString(), "src/test/resources/schemas/createUserSchema.json");
        response.then().body("name", equalTo("morpheus"));
    }

    @Test
    public void testUpdateUser() {
        String payload = "{ \"name\": \"morpheus\", \"job\": \"zion resident\" }";

        Response response = given()
                .header("Content-Type", "application/json")
                .body(payload)
                .when()
                .put("/api/users/2")
                .then()
                .statusCode(200)
                .extract().response();

        SchemaValidator.validate(response.asString(), "src/test/resources/schemas/updateUserSchema.json");
        response.then().body("job", equalTo("zion resident"));
    }

    @Test
    public void testDeleteUser() {
        given()
                .when()
                .delete("/api/users/2")
                .then()
                .statusCode(204);
    }

    @Test
    public void testUserNotFoundReturns404() {
        given()
                .when()
                .get("/api/users/23")
                .then()
                .statusCode(404);
    }

    @Test
    public void testCreateUserInvalidJsonReturns400() {
        String payload = "{ \"name\": \"morpheus\", ";

        given()
                .header("Content-Type", "application/json")
                .body(payload)
                .when()
                .post("/api/users")
                .then()
                .statusCode(400);
    }

}
