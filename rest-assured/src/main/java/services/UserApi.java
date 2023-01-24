package services;

import static io.restassured.RestAssured.given;

import dto.UserDTO;
import io.restassured.response.ValidatableResponse;
import java.util.HashMap;
import java.util.Map;

public class UserApi extends BaseApi {
  public static final String USER_URL = "/users";

  public ValidatableResponse createUser(UserDTO userDTO) {
    return given(specification)
        .body(userDTO)
        .when()
        .post(USER_URL)
        .then()
        .log().all();
  }

  public ValidatableResponse getUserById(String userId) {
    return given(specification)
        .basePath(USER_URL + "/{userId}")
        .pathParam("userId", userId)
        .when()
        .get()
        .then()
        .log().all();
  }

  public ValidatableResponse getUserList(String page) {
    Map<String, String> params = new HashMap<>();
    if (!page.isEmpty()) {
      params.put("page", page);
    }

    return given(specification)
        .basePath(USER_URL)
        .params(params)
        .when()
        .get()
        .then()
        .log().all();
  }

}
