package talent.upc.edu.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import talent.upc.edu.booking.model.Booking;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
}
