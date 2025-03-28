package talent.upc.edu.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import talent.upc.edu.booking.model.Booking;
import talent.upc.edu.booking.model.Room;
import talent.upc.edu.booking.model.User;
import talent.upc.edu.booking.service.PricingService;
import talent.upc.edu.booking.service.TimeProvider;

import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static talent.upc.edu.booking.service.PricingService.BLACK_FRIDAY_DATES;

/**
 * User Story: Booking Price Calculation
 * As a Customer looking to book a stay
 * I want to Receive an accurate price calculation for my booking
 * So that I can know the total cost before confirming my reservation
 * Acceptance Criteria:
 * The total price is calculated by multiplying the number of nights by the price per night.
 * If the user has been registered for more than 1 year, they receive a 5% discount on the total booking price.
 * If the booking is made during Black Friday (between November 28 and December 3), a 15% discount will be applied to the total booking price.
 * If the user qualifies for both the Christmas discount and a new user or loyalty discount, only the highest discount is applied (not cumulative)
 */
@ExtendWith(MockitoExtension.class)
public class PricingServiceTest {
    @Mock
    private TimeProvider timeProvider;
    @InjectMocks
    private PricingService pricingService;
    private User user;
    private Room room;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;

    @BeforeEach
    void setup() {
        // Initialize common test data
        this.user = User.builder().id(1).build();
        this.room = Room.builder().id(1).pricePerNight(100.0).build();
        this.checkInDate = LocalDate.of(2025, Month.MARCH, 21);
        this.checkOutDate = LocalDate.of(2025, Month.MARCH, 25);
    }

    @Test
    void should_ReturnCorrectTotalPrice_When_MultipleNightsAreBooked() {
        // Given
        Booking bookingRequest = Booking.builder().user(user).room(room).checkInDate(checkInDate).checkOutDate(checkOutDate).build();
        double expected = Period.between(checkInDate, checkOutDate).getDays() * bookingRequest.getRoom().getPricePerNight();

        // When
        double actual = pricingService.calculateTotalPrice(bookingRequest);

        // Then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void should_ReturnDiscountedTotalPrice_When_UserRegisteredMoreThanOneYear() {
        // Given
        LocalDate registrationDate = LocalDate.now().minusYears(1).minusDays(1);
        User registeredUser = user.withRegistrationDate(registrationDate);
        Booking bookingRequest = Booking.builder().user(registeredUser).room(room).checkInDate(checkInDate).checkOutDate(checkOutDate).build();

        double expected = Period.between(checkInDate, checkOutDate).getDays() * bookingRequest.getRoom().getPricePerNight() * 95 / 100;

        // When
        double actual = pricingService.calculateTotalPrice(bookingRequest);

        // Then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void should_ReturnDiscountedTotalPrice_When_BookingIsDuringBlackFriday() {
        // Given
        Booking bookingRequest = Booking.builder().user(user).room(room).checkInDate(checkInDate).checkOutDate(checkOutDate).build();
        when(this.timeProvider.getCurrentDate()).thenReturn(Arrays.asList(BLACK_FRIDAY_DATES).get(1));

        double expected = Period.between(checkInDate, checkOutDate).getDays() * bookingRequest.getRoom().getPricePerNight() * 85 / 100;

        // When
        double actual = pricingService.calculateTotalPrice(bookingRequest);

        // Then
        assertThat(actual).isEqualTo(expected);
    }
}
