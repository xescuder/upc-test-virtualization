package talent.upc.edu.booking.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private LocalDate checkInDate;

    private LocalDate checkOutDate;

    private String guestFullName;

    private String guestEmail;

    @Column(name = "adults")
    private int numOfAdults;

    @Column(name = "children")
    private int numOfChildren;

    //@Getter(lazy= true)
    //private final int totalGuests = numOfAdults + numOfChildren;
    @Transient
    private int totalGuests;

    private String confirmationCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    /**
     * The user who made the booking, linked with external user service
     */
    @Transient
    private User user;

    @PostLoad
    @PostPersist
    @PostUpdate
    private void calculateTotalGuests() {
        this.totalGuests = this.numOfAdults + this.numOfChildren;
    }
}