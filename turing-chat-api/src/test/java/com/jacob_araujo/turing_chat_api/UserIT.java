package com.jacob_araujo.turing_chat_api;

import com.jacob_araujo.turing_chat_api.web.dto.UserCreateDto;
import com.jacob_araujo.turing_chat_api.web.dto.UserPasswordDto;
import com.jacob_araujo.turing_chat_api.web.dto.UserResponseDto;
import com.jacob_araujo.turing_chat_api.web.exception.ErrorMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/users/users-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/users/users-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class UserIT {

    @Autowired
    WebTestClient testClient;

    @MockBean
    private JavaMailSender mailSender;

    @Test
    public void createUser_validUsernamePassword_returnUserCreatedStatus201(){
        UserResponseDto responseBody = testClient
                .post()
                .uri("api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserCreateDto("tody@email.com", "1234567A"))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(UserResponseDto.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getId()).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getUsername()).isEqualTo("tody@email.com");
        org.assertj.core.api.Assertions.assertThat(responseBody.getRole()).isEqualTo("CLIENT");
    }

    @Test
    public void createUser_invalidUsername_returnErrorMessageStatus422() {
        ErrorMessage responseBody = testClient
                .post()
                .uri("api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserCreateDto("", "1234567A"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = testClient
                .post()
                .uri("api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserCreateDto("tody@", "1234567A"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = testClient
                .post()
                .uri("api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserCreateDto("tody@email", "1234567A"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
    }

    @Test
    public void createUser_invalidPassword_returnErrorMessageStatus422() {
        ErrorMessage responseBody = testClient
                .post()
                .uri("api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserCreateDto("tody@email.com", ""))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = testClient
                .post()
                .uri("api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserCreateDto("tody@email.com", "12345"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = testClient
                .post()
                .uri("api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserCreateDto("tody@email.com", "1234567A7"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
    }

    @Test
    public void createUser_usernameAlreadyExists_returnErrorMessageStatus409() {
        ErrorMessage responseBody = testClient
                .post()
                .uri("api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserCreateDto("ana@email.com", "1234567A"))
                .exchange()
                .expectStatus().isEqualTo(409)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(409);

    }

    @Test
    public void searchUser_foundId_returnUserFoundStatus200(){
        UserResponseDto responseBody = testClient
                .get()
                .uri("api/v1/users/100")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "1234567A"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserResponseDto.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getId()).isEqualTo(100);
        org.assertj.core.api.Assertions.assertThat(responseBody.getUsername()).isEqualTo("ana@email.com");
        org.assertj.core.api.Assertions.assertThat(responseBody.getRole()).isEqualTo("ADMIN");

        responseBody = testClient
                .get()
                .uri("api/v1/users/101")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "1234567A"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserResponseDto.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getId()).isEqualTo(101);
        org.assertj.core.api.Assertions.assertThat(responseBody.getUsername()).isEqualTo("bia@email.com");
        org.assertj.core.api.Assertions.assertThat(responseBody.getRole()).isEqualTo("CLIENT");


        responseBody = testClient
                .get()
                .uri("api/v1/users/101")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "bia@email.com", "1234567A"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserResponseDto.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getId()).isEqualTo(101);
        org.assertj.core.api.Assertions.assertThat(responseBody.getUsername()).isEqualTo("bia@email.com");
        org.assertj.core.api.Assertions.assertThat(responseBody.getRole()).isEqualTo("CLIENT");
    }


    @Test
    public void searchUser_notFoundId_returnErrorMessageStatus404(){
        ErrorMessage responseBody = testClient
                .get()
                .uri("api/v1/users/0")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "1234567A"))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(404);
    }

    @Test
    public void searchUser_clientUserSearchingOtherClient_returnErrorMessageStatus403(){
        ErrorMessage responseBody = testClient
                .get()
                .uri("api/v1/users/102")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "bia@email.com", "1234567A"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);
    }

    @Test
    public void changePassword_validData_returnUserCreatedStatus204(){
        testClient
                .patch()
                .uri("api/v1/users/100")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "1234567A"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserPasswordDto("1234567A", "1234567B", "1234567B"))
                .exchange()
                .expectStatus().isNoContent();
        testClient
                .patch()
                .uri("api/v1/users/101")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "bia@email.com", "1234567A"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserPasswordDto("1234567A", "1234567B", "1234567B"))
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    public void changePassword_differentUser_returnErrorMessageStatus403() {
        ErrorMessage responseBody = testClient
                .patch()
                .uri("api/v1/users/0")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "1234567A"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserPasswordDto("1234567A", "1234567A", "1234567A"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);

        responseBody = testClient
                .patch()
                .uri("api/v1/users/0")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "bia@email.com", "1234567A"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserPasswordDto("1234567A", "1234567A", "1234567A"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);
    }

    @Test
    public void changePassword_invalidData_returnErrorMessageStatus422() {
        ErrorMessage responseBody = testClient
                .patch()
                .uri("api/v1/users/100")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "1234567A"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserPasswordDto("", "", ""))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = testClient
                .patch()
                .uri("api/v1/users/100")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "1234567A"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserPasswordDto("12345", "12345", "12345"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = testClient
                .patch()
                .uri("api/v1/users/100")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "1234567A"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserPasswordDto("1234567890123456789012345678901", "1234567A7", "1234567A7"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
    }

    @Test
    public void changePassword_passwordMismatch_returnErrorMessageStatus400() {
        ErrorMessage responseBody = testClient
                .patch()
                .uri("api/v1/users/100")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "1234567A"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserPasswordDto("1234567C", "1234567A", "1234567A"))
                .exchange()
                .expectStatus().isEqualTo(400)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(400);

        responseBody = testClient
                .patch()
                .uri("api/v1/users/100")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "1234567A"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserPasswordDto("1234567A", "1234567C", "1234567D"))
                .exchange()
                .expectStatus().isEqualTo(400)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(400);
    }

    @Test
    public void listUsers_noParameters_returnUsersListStatus200(){
        List<UserResponseDto> responseBody = testClient
                .get()
                .uri("api/v1/users")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "1234567A"))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(UserResponseDto.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.size()).isEqualTo(3);
    }

    @Test
    public void listUsers_UserLacksPermission_returnErrorMessageStatus403(){
        ErrorMessage responseBody = testClient
                .get()
                .uri("api/v1/users")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "bia@email.com", "1234567A"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);
    }
}
