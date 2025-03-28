package talent.upc.edu.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;
import talent.upc.edu.booking.model.Booking;
import talent.upc.edu.booking.model.Room;
import talent.upc.edu.booking.repository.RoomRepository;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * The `@DataJpaTest` annotation focuses only on JPA components.
 * 1. It scans the `@Entity` classes and Spring Data JPA repositories.
 * 2. Set the `spring.jpa.show-sql` property to true and enable the SQL queries logging.
 * 3. Default, JPA test data are transactional and roll back at the end of each test;
 * it means we do not need to clean up saved or modified table data after each test.
 * 4. Replace the application DataSource, run and configure the embedded database on classpath.
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=none",
        "spring.sql.init.mode=never"
})
public class RoomRepositoryTest {
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
