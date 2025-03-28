package talent.upc.edu.booking.service;

import org.springframework.stereotype.Service;
import talent.upc.edu.booking.model.Room;
import talent.upc.edu.booking.repository.RoomRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class RoomService {
    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }


    public int getAvailablePlacesCount(LocalDate checkInDate, LocalDate checkOutDate) {
        List<Room> rooms = roomRepository.findAll();
        return rooms.stream()
                .filter(room -> room.isAvailable(checkInDate, checkOutDate))
                .mapToInt(Room::getCapacity)
                .sum();
    }

    public Room findAvailableRoom(LocalDate checkInDate, LocalDate checkOutDate, int totalGuests) {
        return roomRepository.findAll().stream()
                .filter(room -> (room.isAvailable(checkInDate, checkOutDate) && room.getCapacity() >= totalGuests))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No available room found"));
    }

    public long findAvailableRoomId(LocalDate checkInDate, LocalDate checkOutDate, int totalGuests) {
        return roomRepository.findAll().stream()
                .filter(room -> (room.isAvailable(checkInDate, checkOutDate) && room.getCapacity() >= totalGuests))
                .findFirst()
                .map(Room::getId)
                .orElseThrow(() -> new RuntimeException("No available room found"));
    }


    public List<Room> findAll() {
        return roomRepository.findAll();
    }

    public Room findById(Long id) {
        return roomRepository.findById(id).orElse(null);
    }
}
