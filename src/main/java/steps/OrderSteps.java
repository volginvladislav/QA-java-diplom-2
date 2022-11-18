package steps;

import dto.Order;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static dto.EndPoints.*;
import static io.restassured.RestAssured.given;

public class OrderSteps extends RestSteps {
    @Step("Получить список всех ингредиентов")
    public ValidatableResponse getAllIngredients(){
        return given()
                .spec(getDefaultRequestSpec())
                .get(INGREDIENTS)
                .then();
    }
    @Step("Создание заказа с авторизацией")
    public ValidatableResponse createOrderWithAuthorization(Order ingredients, String accessToken){
        return given()
                .header("authorization", accessToken)
                .spec(getDefaultRequestSpec())
                .body(ingredients)
                .post(ORDERS)
                .then();
    }
    @Step("Создание заказа без авторизации")
    public ValidatableResponse createOrderWithoutAuthorization(Order ingredients){
        return given()
                .spec(getDefaultRequestSpec())
                .body(ingredients)
                .post(ORDERS)
                .then();
    }
    @Step("Получение заказов пользователя с авторизацией")
    public ValidatableResponse getOrdersUserWithAuthorization(String accessToken){
        return given()
                .header("authorization", accessToken)
                .spec(getDefaultRequestSpec())
                .get(ORDERS)
                .then();
    }
    @Step("Получение заказов пользователя без авторизации")
    public ValidatableResponse getOrdersUserWithoutAuthorization(){
        return given()
                .spec(getDefaultRequestSpec())
                .get(ORDERS)
                .then();
    }
}
