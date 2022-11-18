package orderTest;

import dto.Order;
import dto.UserRequest;
import generator.IngredientGenerator;
import generator.UserRequestGenerator;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import steps.OrderSteps;
import steps.UserSteps;

import java.util.List;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;

public class GetUsersOrdersTest {
    private UserSteps userSteps;
    private OrderSteps orderSteps;
    private String accessToken;
    private UserRequest userRequest;
    private Order order;
    private List<String> ingredientsList;

    @Before
    public void setUp(){
        userSteps = new UserSteps();
        orderSteps = new OrderSteps();
        userRequest = UserRequestGenerator.getRandomUserRequest();
        accessToken = userSteps.create(userRequest).extract().path("accessToken");
        ingredientsList = orderSteps.getAllIngredients().extract().path("data._id");
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
    @DisplayName("Получение заказов конкретного пользователя при авторизованном пользователе")
    public void checkCreateOrderForUser() {
        order = new Order(IngredientGenerator.getRandomIngredients(ingredientsList));
        orderSteps.createOrderWithAuthorization(order, accessToken)
                .assertThat()
                .statusCode(SC_OK)
                .body("success", equalTo(true));
        orderSteps.getOrdersUserWithAuthorization(accessToken)
                .assertThat()
                .statusCode(SC_OK)
                .body("success", equalTo(true));
    }
    @Test
    @DisplayName("Получение заказов конкретного пользователя при неавторизованном пользователе")
    public void checkCreateOrderForUserWithoutAuthorization() {
        order = new Order(IngredientGenerator.getRandomIngredients(ingredientsList));
        orderSteps.createOrderWithAuthorization(order, accessToken)
                .assertThat()
                .statusCode(SC_OK)
                .body("success", equalTo(true));
        orderSteps.getOrdersUserWithoutAuthorization()
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));

    }
}
