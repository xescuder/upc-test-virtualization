package talent.upc.edu.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import talent.upc.edu.booking.model.Booking;
import talent.upc.edu.booking.model.Room;
import talent.upc.edu.booking.repository.RoomRepository;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Testcontainers(disabledWithoutDocker = true)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RoomTestContainersRepositoryTest {
    @Container
    static final MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8.0.40")
            .withDatabaseName("testcontainer")
            .withUsername("test")
            .withPassword("s3cret");

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mySQLContainer::getUsername);
        registry.add("spring.datasource.password", mySQLContainer::getPassword);
    }

    @Autowired
    private RoomRepository roomRepository;

    @Autowired private TestEntityManager testEntityManager;

    private ArrayList<Room> rooms;

    @BeforeEach
    void beforeEach() {
        assertThat(0).isEqualTo(roomRepository.count());

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
        rooms.forEach(room -> testEntityManager.persist(room));
    }

    @Test
    void should_ReturnAvailableRooms_WhenRoomsStored() {
        // given
        LocalDate checkInDate = LocalDate.of(2025, Month.APRIL, 2);
        LocalDate checkOutDate = LocalDate.of(2025, Month.APRIL, 4);
        int totalGuests = 3;
        List<Room> expected = new ArrayList<>(Arrays.asList(rooms.get(1), rooms.get(2), rooms.get(3)));

        // when
        List<Room> actual = roomRepository.findAvailableRooms(totalGuests, checkInDate, checkOutDate);

        // then
        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }


}
