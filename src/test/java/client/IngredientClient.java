package client;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class IngredientClient {

    private static final String INGREDIENTS = "/api/ingredients";

    @Step("Получить список ингредиентов")
    public Response getIngredients() {
        return given()
                .get(INGREDIENTS);
    }
}