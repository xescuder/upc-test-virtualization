package talent.upc.edu.booking.adapters;

import lombok.Data;
import talent.upc.edu.booking.model.User;

@Data
public class UserResponse {
    private User data;
    private UserSupport support;
}
