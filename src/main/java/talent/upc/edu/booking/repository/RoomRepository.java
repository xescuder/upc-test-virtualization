package talent.upc.edu.booking.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import talent.upc.edu.booking.model.Room;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RoomRepository extends CrudRepository<Room, Long> {
    List<Room> findAll();

    @Query("SELECT r FROM Room r WHERE r.capacity >= ?1 AND r.id NOT IN " +
            "( SELECT br.room.id FROM Booking br WHERE ((br.checkInDate <= ?2) AND (br.checkInDate >= ?3)) )")
    List<Room> findAvailableRooms(int totalGuests, LocalDate checkInDate, LocalDate checkOutDate);
}

