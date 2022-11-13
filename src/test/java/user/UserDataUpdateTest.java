package user;

import dto.UserRequest;
import generator.UserRequestGenerator;
import io.qameta.allure.junit4.DisplayName;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import steps.UserSteps;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;

public class UserDataUpdateTest {

    private UserSteps userSteps;
    private String accessToken;

    @Before
    public void setUp(){
        userSteps = new UserSteps();

    }
    @After
    @DisplayName("Удаление пользователя после каждого теста при получении токена")
    //удаляем данные после каждого теста
    public void tearDown() {
        if (accessToken != null) {
            userSteps.deleteUser(accessToken).assertThat().statusCode(SC_ACCEPTED)
                    .body("success", equalTo(true));
        }
    }

    @Test
    @DisplayName("Изменение поля email с авторизацией")
    public void checkChangeEmailWithAuthorization() {
        UserRequest userRequest = UserRequestGenerator.getRandomUserRequest();
        accessToken = userSteps.create(userRequest)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("success", equalTo(true))
                .extract()
                .path("accessToken");
        userRequest.setEmail(RandomStringUtils.randomAlphabetic(10) + "@yandex.ru");
        String newEmail = userRequest.getEmail().toLowerCase();
        userSteps.changeUserData (userRequest,accessToken)
                .assertThat()
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("user.email", equalTo(newEmail));
    }

    @Test
    @DisplayName("Изменение поля name с авторизацией")
    public void checkChangeNameWithAuthorization() {
        UserRequest userRequest = UserRequestGenerator.getRandomUserRequest();
        accessToken = userSteps.create(userRequest)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("success", equalTo(true))
                .extract()
                .path("accessToken");
        userRequest.setName(RandomStringUtils.randomAlphabetic(10));
        userSteps.changeUserData (userRequest,accessToken)
                .assertThat()
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("user.name", equalTo(userRequest.getName()));
    }

    @Test
    @DisplayName("Изменение поля email без авторизации")
    public void checkChangeEmailWithoutAuthorization() {
        UserRequest userRequest = UserRequestGenerator.getRandomUserRequest();
        accessToken = userSteps.create(userRequest)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("success", equalTo(true))
                .extract()
                .path("accessToken");
        userRequest.setEmail(RandomStringUtils.randomAlphabetic(10) + "@yandex.ru");
        String newEmail = userRequest.getEmail().toLowerCase();
        userSteps.changeUserData (userRequest,"")
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message",equalTo("You should be authorised") );
    }

    @Test
    @DisplayName("Изменение поля name без авторизации")
    public void checkChangeUserNameWithoutAuthorization() {
        UserRequest userRequest = UserRequestGenerator.getRandomUserRequest();
        accessToken = userSteps.create(userRequest)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("success", equalTo(true))
                .extract()
                .path("accessToken");
        userRequest.setName(RandomStringUtils.randomAlphabetic(10));
        userSteps.changeUserData (userRequest,"")
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message",equalTo("You should be authorised") );
    }
}

