package talent.upc.edu.booking.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Blob;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "rooms")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name="capacity")
    private int capacity;
    @Column(name="price_per_night")
    private double pricePerNight;

    @Lob
    private Blob photo;

    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Booking> bookings ;

    public void addBooking(Booking booking){
        if(bookings == null){
            bookings = new ArrayList<>();
        }
        this.bookings.add(booking);
        booking.setRoom(this);
    }

    public boolean isAvailable(LocalDate checkInDate, LocalDate checkOutDate) {
        if (bookings == null) {
            return true;
        }
        return bookings.stream().noneMatch(booking -> booking.getCheckInDate().isBefore(checkOutDate) && booking.getCheckOutDate().isAfter(checkInDate));
    }
}
