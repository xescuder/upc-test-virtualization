package talent.upc.edu.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;
import talent.upc.edu.booking.controller.RoomController;
import talent.upc.edu.booking.model.Room;
import talent.upc.edu.booking.service.RoomService;

import java.time.LocalDate;
import java.time.Month;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;

/**
 * WebMvcTest annotation is used for unit testing Spring MVC application.
 * MockMvc can be used on its own to perform requests and verify responses using Hamcrest or through MockMvcTester
 * which provides a fluent API using AssertJ
 * Reference: https://docs.spring.io/spring-framework/reference/testing/mockmvc/assertj.html
 */
@WebMvcTest(RoomController.class)
public class RoomControllerTest {
    @Autowired
    private MockMvcTester mockMvc;

    @MockitoBean
    private RoomService roomService;

    @Test
    void should_ReturnAvailableRooms_WhenRoomsAvailable()  {
        // given
        Room room = Room.builder().pricePerNight(100.0).capacity(2).build();
        given(this.roomService.findAvailableRoom(any(LocalDate.class), any(LocalDate.class), anyInt())).willReturn(room);

        // when
        LocalDate checkInDate = LocalDate.of(2025, Month.APRIL, 2);
        LocalDate checkOutDate = LocalDate.of(2025, Month.APRIL, 5);

        MvcTestResult result = this.mockMvc.get().uri("/rooms/available")
                .param("checkInDate", checkInDate.toString())
                .param("checkOutDate", checkOutDate.toString())
                .param("totalGuests", "2")
                .accept(MediaType.APPLICATION_JSON)
                .exchange();

        // then
        assertThat(result).hasStatusOk().bodyJson().extractingPath("$").convertTo(Room.class).isEqualTo(room);
    }
}
