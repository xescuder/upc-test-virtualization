package talent.upc.edu.booking.service;

import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class TimeProvider {
    public LocalDate getCurrentDate() {
        return LocalDate.now();
    }
}
