package talent.upc.edu.booking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import talent.upc.edu.booking.adapters.UserPaginationResponse;
import talent.upc.edu.booking.adapters.UserResponse;
import talent.upc.edu.booking.adapters.UserRestAdapter;
import talent.upc.edu.booking.model.User;

import java.util.List;

@Service
public class UserService {
    private final UserRestAdapter userRestAdapter;

    @Autowired
    public UserService(UserRestAdapter userRestAdapter) {
        this.userRestAdapter = userRestAdapter;
    }

    public List<User> getUsers() {
        UserPaginationResponse response = userRestAdapter.findAllPaginated(1);
        return response.getData();
    }

    public User getUser(int id) {
        UserResponse response = userRestAdapter.getUserResponse(id);
        return response.getData();
    }
}
