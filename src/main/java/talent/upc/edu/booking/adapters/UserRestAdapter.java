package talent.upc.edu.booking.adapters;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import talent.upc.edu.booking.model.User;


/**
 * We'll use RestClient to interact with ReqRes API. RestClient is an evolution of RestTemplate.
 */
@Component
public class UserRestAdapter {
    private final RestClient restClient;

    public UserRestAdapter(
            @Value("${reqres.api.base-url}") String baseUrl
    ) {
        this.restClient = RestClient.builder()
                .requestFactory(new HttpComponentsClientHttpRequestFactory())
                .baseUrl(baseUrl)
                .build();
    }

    public UserPaginationResponse findAllPaginated(int page) {
        return restClient.get().uri("/api/users?page={page}", page).accept(MediaType.APPLICATION_JSON).retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }

    public UserResponse getUserResponse(int id) {
        return restClient.get().uri("/api/users/{id}", id).retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                    throw new RuntimeException("User not found");
                })
                .body(UserResponse.class);
    }

    public User createUser(User user) {
        ResponseEntity<Void> response = restClient.post().uri("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .body(user)
                .retrieve()
                .toBodilessEntity();
        return user;
    }

    public void updateUser(int id, User user) {
        ResponseEntity<Void> response = restClient.put().uri("/api/users/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .body(user)
                .retrieve()
                .toBodilessEntity();
    }

    public void deleteUser(int id) {
        ResponseEntity<Void> response = restClient.delete().uri("/api/users/{id}", id)
                .retrieve()
                .toBodilessEntity();
    }
}
