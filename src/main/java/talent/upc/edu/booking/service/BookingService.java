package talent.upc.edu.booking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import talent.upc.edu.booking.model.Booking;
import talent.upc.edu.booking.model.Room;
import talent.upc.edu.booking.model.User;
import talent.upc.edu.booking.repository.BookingRepository;

@Service
public class BookingService {
    private RoomService roomService;
    private PricingService pricingService;
    private PaymentService paymentService;
    private MailSender mailSender;
    private UserService userService;
    private BookingRepository bookingRepository;

    @Autowired
    public BookingService(RoomService roomService, PricingService pricingService, PaymentService paymentService, MailSender mailSender, UserService userService, BookingRepository bookingRepository) {
        this.roomService = roomService;
        this.pricingService = pricingService;
        this.paymentService = paymentService;
        this.mailSender = mailSender;
        this.userService = userService;
        this.bookingRepository = bookingRepository;
    }

    public long makeBooking(Booking booking) {
        Room room = roomService.findAvailableRoom(booking.getCheckInDate(), booking.getCheckOutDate(), booking.getTotalGuests());
        double price = pricingService.calculateTotalPrice(booking);

        paymentService.pay(booking, price);

        booking.setRoom(room);

        Booking bookingPersisted = this.save(booking);
        long bookingId = bookingPersisted.getId();

        User user = userService.getUser(booking.getUser().getId());
        String userEmail = user.getEmail();
        mailSender.sendMail(userEmail, "Booking confirmation", "Your booking has been confirmed with id: " + bookingId);

        return bookingId;
    }

    public Booking save(Booking any) {
        return bookingRepository.save(any);
    }
}
