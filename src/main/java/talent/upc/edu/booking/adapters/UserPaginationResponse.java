package talent.upc.edu.booking.adapters;

import lombok.Data;
import talent.upc.edu.booking.model.User;

import java.util.List;

@Data
public class UserPaginationResponse {
    private int page;
    private int perPage;
    private int total;
    private int totalPages;
    private List<User> data;
}