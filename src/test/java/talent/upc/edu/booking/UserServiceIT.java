package talent.upc.edu.booking;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestClient;
import org.wiremock.spring.ConfigureWireMock;
import org.wiremock.spring.EnableWireMock;
import org.wiremock.spring.InjectWireMock;
import talent.upc.edu.booking.adapters.UserPaginationResponse;
import talent.upc.edu.booking.model.User;
import talent.upc.edu.booking.service.UserService;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * More information:
 * https://wiremock.org/docs/spring-boot/
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@EnableWireMock(
        @ConfigureWireMock(
                name="user-service",
                baseUrlProperties = {"reqres.api.base-url"}
        )
)
class UserServiceIT {
    @Autowired
    private UserService userService;

    @InjectWireMock("user-service")
    WireMockServer mockUserService;

    @Test
    void should_ReturnUsers_WhenUsersAvailable() {
        stubFor(WireMock.get("/api/users?page=1")
                .willReturn(aResponse().withHeader("Content-Type", "application/json").withBodyFile("users.json")));

        RestClient client = RestClient.create();
        UserPaginationResponse response = client.get()
                .uri(mockUserService.baseUrl() + "/api/users?page=1")
                .retrieve()
                .body(UserPaginationResponse.class);

        List<User> users = userService.getUsers();
        assertThat(users).hasSize(6);
    }
}