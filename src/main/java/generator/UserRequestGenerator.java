package generator;

import dto.UserRequest;
import io.qameta.allure.Step;
import org.apache.commons.lang3.RandomStringUtils;

public class UserRequestGenerator {
    @Step("Ввод данных для создания пользователя")
    public static UserRequest getRandomUserRequest() {
        UserRequest userRequest = new UserRequest();
        userRequest.setEmail(RandomStringUtils.randomAlphabetic(10) + "@yandex.ru");
        userRequest.setPassword(RandomStringUtils.randomAlphanumeric(8));
        userRequest.setName(RandomStringUtils.randomAlphabetic(10));
        return userRequest;
    }

    @Step("Данные для проверки возможности регистрации без поля email")
    public static UserRequest getRandomUserRequestWithoutEmailField() {
        UserRequest userRequest = new UserRequest();
        userRequest.setPassword(RandomStringUtils.randomAlphanumeric(8));
        userRequest.setName(RandomStringUtils.randomAlphabetic(10));
        return userRequest;
    }

    @Step("Данные для проверки возможности регистрации без поля password")
    public static UserRequest getRandomUserRequestWithoutPasswordField() {
        UserRequest userRequest = new UserRequest();
        userRequest.setEmail(RandomStringUtils.randomAlphabetic(10) + "@yandex.ru");
        userRequest.setName(RandomStringUtils.randomAlphabetic(10));
        return userRequest;
    }

    @Step("Данные для проверки возможности регистрации без поля name")
    public static UserRequest getRandomUserRequestWithoutNameField() {
        UserRequest userRequest = new UserRequest();
        userRequest.setEmail(RandomStringUtils.randomAlphabetic(10) + "@yandex.ru");
        userRequest.setPassword(RandomStringUtils.randomAlphanumeric(8));
        return userRequest;
    }
}
