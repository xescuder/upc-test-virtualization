package talent.upc.edu.booking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import talent.upc.edu.booking.model.Booking;
import talent.upc.edu.booking.model.Room;
import talent.upc.edu.booking.model.User;
import talent.upc.edu.booking.repository.BookingRepository;
import talent.upc.edu.booking.service.*;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {
    @Mock
    private RoomService roomService;

    @Mock
    private PricingService pricingService;

    @Mock
    private PaymentService paymentService;

    @Mock
    private UserService userService;

    @Mock
    private MailSender mailSender;

    @Mock
    private BookingRepository bookingRepository;



    @InjectMocks
    private BookingService bookingService;


    @Captor
    private ArgumentCaptor<Booking> bookingArgumentCaptor;


    @Test
    void should_MakeBooking_When_AllInputIsCorrect() {
        // Given
        Room room = Room.builder().build();
        User user = User.builder().email("xescuder@gmail.com").build();
        LocalDate checkInDate = LocalDate.of(2025, 3, 21);
        LocalDate checkOutDate = LocalDate.of(2025, 3, 25);

        Booking booking = Booking.builder().user(user).numOfAdults(2).numOfChildren(1).checkInDate(checkInDate).checkOutDate(checkOutDate).build();

        when(roomService.findAvailableRoom(any(LocalDate.class), any(LocalDate.class), any(Integer.class))).thenReturn(room);
        when(userService.getUser(any(Integer.class))).thenReturn(user);
        when(bookingRepository.save(any(Booking.class))).thenAnswer(invocation -> {
            Booking bookingToSave = invocation.getArgument(0);
            long bookingId = 1L + (long) (Math.random() * (10L - 1L));
            bookingToSave.setId(bookingId);
            return bookingToSave;
        });

        // when
        bookingService.makeBooking(booking);

        // then
        verify(bookingRepository).save(bookingArgumentCaptor.capture());
        Booking savedBooking = bookingArgumentCaptor.getValue();
        assertThat(booking).isEqualTo(savedBooking);
        verify(mailSender).sendMail(any(String.class), any(String.class), any(String.class));
    }
}
