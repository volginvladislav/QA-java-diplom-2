package steps;
import dto.LoginRequest;
import dto.UserRequest;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static dto.EndPoints.*;
import static io.restassured.RestAssured.given;
public class UserSteps extends RestSteps {

    @Step("Регистрация нового пользователя")
    public ValidatableResponse create(UserRequest userRequest){
        return given()
                .spec(getDefaultRequestSpec())
                .body(userRequest)
                .post(CREATE_USER)
                .then();
    }
    @Step("Вход под созданным пользователем")
    public ValidatableResponse login(LoginRequest loginRequest){
        return given()
                .spec(getDefaultRequestSpec())
                .body(loginRequest)
                .post(LOGIN_USER)
                .then();
    }
    @Step("Удаление пользователя")
    //create
    public ValidatableResponse deleteUser(String accessToken) {
        return given()
                .header("authorization", accessToken)
                .spec(getDefaultRequestSpec())
                .delete(DELETE)
                .then();
    }
    @Step("Изменение данных пользователя")
    public ValidatableResponse changeUserData(UserRequest userRequest, String  accessToken) {
        return given()
                .header("Authorization", accessToken)
                .spec(getDefaultRequestSpec())
                .and()
                .body(userRequest)
                .when()
                .patch(INFO_USER)
                .then();
    }
}
