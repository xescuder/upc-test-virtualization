package talent.upc.edu.booking;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;

@SpringBootApplication
public class BookingApplication {
    @Value("${server.port}")
    private String serverPort;

    @Value("${spring.application.name}")
    private String name;

    public static void main(String[] args) {
        SpringApplication.run(BookingApplication.class, args);
    }

    @PostConstruct
    public void init() {
        System.out.println("Server port from application.properties: " + serverPort);
    }

    @RequestMapping(value = "/")
    public String name() {
        return name;
    }
}