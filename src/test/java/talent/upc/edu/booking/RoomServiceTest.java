package talent.upc.edu.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import talent.upc.edu.booking.model.Booking;
import talent.upc.edu.booking.model.Room;
import talent.upc.edu.booking.repository.RoomRepository;
import talent.upc.edu.booking.service.RoomService;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;


@ExtendWith(MockitoExtension.class)
public class RoomServiceTest {
    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private RoomService roomService;

    private ArrayList<Room> rooms;

    @BeforeEach
    void setup() {
        // Initialize common test data
        Room roomOne = Room.builder().capacity(2).build();
        Room roomTwo = Room.builder().capacity(3).build();
        Room roomThree = Room.builder().capacity(4).build();
        Room roomFour = Room.builder().capacity(3).build();

        LocalDate checkInDate = LocalDate.of(2025, Month.MARCH, 21);
        LocalDate checkOutDate = LocalDate.of(2025, Month.MARCH, 25);
        roomOne.addBooking(Booking.builder().checkInDate(checkInDate).checkOutDate(checkOutDate).build());
        roomTwo.addBooking(Booking.builder().checkInDate(checkInDate).checkOutDate(checkOutDate).build());
        roomFour.addBooking(Booking.builder().checkInDate(checkInDate).checkOutDate(checkOutDate).build());
        rooms = new ArrayList<>(List.of(roomOne, roomTwo, roomThree, roomFour));
    }

    @Test
    void should_ReturnMoreThanZeroPlacesCount_When_AvailablePlaces() {
        // given
        LocalDate checkInDate = LocalDate.of(2025, Month.MARCH, 26);
        LocalDate checkOutDate = LocalDate.of(2025, Month.MARCH, 30);

        Mockito.when(roomRepository.findAll()).thenReturn(rooms);
        int expected = rooms.stream().filter(room -> room.isAvailable(checkInDate, checkOutDate)).map(Room::getCapacity).collect(Collectors.summingInt(Integer::intValue));

        // when
        int actual = roomService.getAvailablePlacesCount(checkInDate, checkOutDate);
        // then
        assertThat(actual).isEqualTo(expected);
    }


    @Test
    void should_ReturnFirstAvailableRoom_When_AvailableRoom() {
        // given
        Mockito.when(roomRepository.findAll()).thenReturn(rooms);

        LocalDate checkInDate = LocalDate.of(2025, Month.MARCH, 26);
        LocalDate checkOutDate = LocalDate.of(2025, Month.MARCH, 30);

        int totalGuests = 3;
        Room expected = rooms.get(1);

        // when
        Room actual = roomService.findAvailableRoom(checkInDate, checkOutDate, totalGuests);

        // then
        assertThat(actual).isEqualTo(expected);
    }


    @Test
    void should_ThrowException_When_NotAvailableRoom() {
        // given
        ArrayList<Room> rooms = new ArrayList<>(List.of(
                Room.builder().capacity(2).build(),
                Room.builder().capacity(3).build(),
                Room.builder().capacity(2).build(),
                Room.builder().capacity(3).build()
        ));
        Mockito.when(roomRepository.findAll()).thenReturn(rooms);

        LocalDate checkInDate = LocalDate.of(2025, Month.MARCH, 22);
        LocalDate checkOutDate = LocalDate.of(2025, Month.MARCH, 24);
        int totalGuests = 3;

        // then
        assertThatExceptionOfType(RuntimeException.class).isThrownBy(() -> roomService.findAvailableRoom(checkInDate, checkOutDate, totalGuests));
    }
}

