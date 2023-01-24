import static org.hamcrest.Matchers.equalTo;

import dto.Support;
import dto.UserDTO;
import dto.UserData;
import dto.UserListDTO;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.ValidatableResponse;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import services.UserApi;

public class RestAssuredTests {

  private final UserApi userApi = new UserApi();

  /**
   * Проверка успешного получения пользователя
   * Действие:
   * - Для получения пользователя отправляем get запрос на url users/${userId}
   * Ожидаемый результат:
   * - статус ответа равен 200
   * - ответ совпадает с ожидаемым
   * - схема ответа соответсвует jsonSchema 'userSchema.json'
   */

  @Test
  @DisplayName("Получение информации о пользователе")

  public void getTestActions() {
    String userId = "2";
    UserDTO userDTO = getUserJson();
    ValidatableResponse validatableResponse = userApi.getUserById(userId).statusCode(200);

    UserDTO user = validatableResponse.extract().body().as(UserDTO.class);
    String userBodyResponse = validatableResponse.extract().response().body().prettyPrint();

    SoftAssertions softAssertions = new SoftAssertions();
    softAssertions.assertThat(user)
        .withFailMessage(String.format("Не совпадает отправленный '%s' и полученный '%s' пользователь", user, userDTO))
        .isEqualTo(userDTO);

    softAssertions.assertThat(JsonSchemaValidator
            .matchesJsonSchemaInClasspath("json/userSchema.json").matches(userBodyResponse))
        .withFailMessage("Не совпадает схема ответа")
        .isTrue();

    softAssertions.assertAll();
  }

  /**
   * Проверка что пользователь не найден
   * Действие:
   * - ищем пользователя с Id = 3
   * Ожидаемый результат:
   * - статус ответа равен 404
   * - error = "User with id = 3 not found"
   */

  @Test
  @DisplayName("Пользователь не найден")
  public void getUserNotFound() {
    String userId = "3";
    userApi.getUserById(userId)
        .statusCode(404)
        .body("error", equalTo("User with id = 3 not found"));
  }

  /**
   * Некорректный запрос получения списка пользователя
   * Действие:
   * - открываем url без параметра page
   * Ожидаемый результат:
   * - статус ответа равен 400
   * - error = "Bad Request, parameter page is required"
   */
  @Test
  @DisplayName("Некорректный запрос получения списка пользователя")
  public void getUserBadRequest() {
    userApi.getUserList("")
        .statusCode(400)
        .body("error", equalTo("Bad Request, parameter page is required"));
  }

  @Test
  @DisplayName("Проверяем что в списке пользователей 6 пользователей")
  public void getUserList() {
    ValidatableResponse validatableResponse = userApi.getUserList("1").statusCode(200);
    String userListBody = validatableResponse.extract().response().body().prettyPrint();
    UserListDTO userList = validatableResponse.extract().body().as(UserListDTO.class);
    Integer userCount = userList.getData().size();
    SoftAssertions softAssertions = new SoftAssertions();
    softAssertions.assertThat(userCount).isEqualTo(6);
    softAssertions.assertThat(JsonSchemaValidator
            .matchesJsonSchemaInClasspath("json/userListSchema.json").matches(userListBody))
        .withFailMessage("Не совпадает схема ответа")
        .isTrue();

    softAssertions.assertAll();
  }

  public UserDTO getUserJson() {
    UserDTO user = UserDTO
        .builder()
        .data(UserData.builder()
            .id(2)
            .email("janet.weaver@reqres.in")
            .firstName("Janet")
            .lastName("Weaver")
            .avatar("https://reqres.in/img/faces/2-image.jpg")
            .build())
        .support(Support.builder()
            .url("https://reqres.in/#support-heading")
            .text("To keep ReqRes free, contributions towards server costs are appreciated!")
            .build())
        .build();

    return user;
  }
}
