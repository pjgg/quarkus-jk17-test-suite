package io.quarkus.ts.jdk17.resources;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;

import java.util.Arrays;

import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import io.quarkus.test.bootstrap.RestService;
import io.quarkus.test.scenarios.QuarkusScenario;
import io.quarkus.test.services.DevModeQuarkusApplication;
import io.quarkus.ts.jdk17.model.Fruit;

@QuarkusScenario
public class FruitsResourceIT {

    private static final String[] DEFAULT_FRUITS_NAMES = { "Apple", "Pineapple" };
    private static final String[] DEFAULT_FRUITS_DESCRIPTIONS = { "Winter fruit", "Tropical fruit" };

    private static final String EXPECTED_TEXT_BLOCK = """
            JEP 378: Text Blocks
            Summary
            Add text blocks to the Java language. A text block is a multi-line string literal that avoids the need for
            most escape sequences, automatically formats the string in a predictable way, and gives the developer
            control over the format when desired.

            History
            Text blocks were proposed by JEP 355 in early 2019 as a follow-on to explorations begun in JEP 326
            (Raw String Literals), which was initially targeted to JDK 12 but eventually withdrawn and did not appear
             in that release. JEP 355 was targeted to JDK 13 in June 2019 as a preview feature. Feedback on JDK 13
             suggested that text blocks should be previewed again in JDK 14, with the addition of two new escape
             sequences. Consequently, JEP 368 was targeted to JDK 14 in November 2019 as a preview feature. Feedback
             on JDK 14 suggested that text blocks were ready to become final and permanent in JDK 15 with
             no further changes.
            """;

    @DevModeQuarkusApplication
    static final RestService app = new RestService();

    @Test
    public void listFruitTest() {
        given()
                .when().get("/fruits")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("$.size()", is(2),
                        "name", containsInAnyOrder(DEFAULT_FRUITS_NAMES),
                        "description", containsInAnyOrder(DEFAULT_FRUITS_DESCRIPTIONS));
    }

    @Test
    public void addFruitTest() {
        given()
                .body("{\"name\": \"Pear\", \"description\": \"Winter fruit\"}")
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .when()
                .post("/fruits")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("$.size()", is(3),
                        "name", containsInAnyOrder(ArrayUtils.add(DEFAULT_FRUITS_NAMES, "Pear")),
                        "description", containsInAnyOrder(ArrayUtils.add(DEFAULT_FRUITS_DESCRIPTIONS, "Winter fruit")));
    }

    @Test
    public void addWrongFruitTest() {
        var longInvalidName = "tooooolooooongToBeValid";
        given()
                .body("{\"name\": \"" + longInvalidName + "\", \"description\": \"Winter fruit\"}")
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .when()
                .post("/fruits")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    public void deleteFruitTest() {
        var fruit = given()
                .when().get("/fruits")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("$.size()", greaterThan(0))
                .extract().body().as(Fruit[].class)[0];

        var fruits = given()
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .when()
                .delete("/fruits/" + fruit.id())
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract().body().as(Fruit[].class);

        assertThat("Fruit has not removed", Arrays.asList(fruits), not(hasItem(fruit)));
    }

    @Test
    public void multilinePlainTextTest() {
        given()
                .when().get("/fruits/jpe_378")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body(is(EXPECTED_TEXT_BLOCK));
    }

    @Test
    public void SqlMultilineTextTest() {
        given()
                .when().get("/fruits/longestDesc")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("description", equalTo("Tropical fruit"));
    }
}
