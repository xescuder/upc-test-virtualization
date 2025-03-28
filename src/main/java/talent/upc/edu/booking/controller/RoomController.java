package talent.upc.edu.booking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import talent.upc.edu.booking.model.Room;
import talent.upc.edu.booking.service.RoomService;

import java.time.LocalDate;
import java.util.List;

@Controller
public class RoomController {
    private final RoomService roomService;

    @Autowired
    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping("/rooms")
    public ResponseEntity<List<Room>> findAll() {
        return ResponseEntity.ok(roomService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Room> read(@PathVariable("id") Long id) {
        Room foundRoom = roomService.findById(id);
        if (foundRoom == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(foundRoom);
        }
    }

    @GetMapping("/rooms/available")
    public ResponseEntity<Room> findAvailableRoom(@RequestParam LocalDate checkInDate, @RequestParam LocalDate checkOutDate, @RequestParam int totalGuests) {
        Room availableRoom = roomService.findAvailableRoom(checkInDate, checkOutDate, totalGuests);
        if (availableRoom == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(availableRoom);
        }
    }
}
