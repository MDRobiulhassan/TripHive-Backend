package com.example.TripHive_Backend.serviceImpl;

import com.example.TripHive_Backend.dto.request.RoomDTO;
import com.example.TripHive_Backend.dto.response.RoomResponseDTO;
import com.example.TripHive_Backend.entity.Hotel;
import com.example.TripHive_Backend.entity.Room;
import com.example.TripHive_Backend.entity.User;
import com.example.TripHive_Backend.exceptions.ResourceNotFoundException;
import com.example.TripHive_Backend.exceptions.UnauthorisedException;
import com.example.TripHive_Backend.repository.HotelRepository;
import com.example.TripHive_Backend.repository.RoomRepository;
import com.example.TripHive_Backend.service.InventoryService;
import com.example.TripHive_Backend.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.example.TripHive_Backend.util.AppUtil.getCurrentUser;

@Service
@Slf4j
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;
    private final InventoryService inventoryService;
    private final ModelMapper modelMapper;

    @Override
    public RoomResponseDTO createRoom(Long hotelId, RoomDTO roomDTO) {
        log.info("Creating New Room in Hotel with ID: {}", hotelId);
        Hotel hotel = hotelRepository.findById(hotelId).orElseThrow(() -> new RuntimeException("Hotel Not Found with id:" + hotelId));

        User user = (User) Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getPrincipal();
        assert user != null;
        if (!user.equals(hotel.getOwner())) {
            throw new UnauthorisedException("This User Does not Own this Hotel");
        }

        Room room = modelMapper.map(roomDTO, Room.class);
        room.setHotel(hotel);
        room = roomRepository.save(room);

        if (hotel.getActive()) {
            inventoryService.initializeRoomForAYear(room);
        }

        return modelMapper.map(room, RoomResponseDTO.class);
    }

    @Override
    public List<RoomResponseDTO> getAllRoomsInHotel(Long hotelId) {
        log.info("Getting All Rooms in Hotel with ID: {}", hotelId);
        Hotel hotel = hotelRepository.findById(hotelId).orElseThrow(() -> new RuntimeException("Hotel Not Found with id:" + hotelId));

        User user = (User) Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getPrincipal();
        assert user != null;
        if (!user.equals(hotel.getOwner())) {
            throw new UnauthorisedException("This User Does not Own this Hotel");
        }

        log.info("Fetched All Rooms in Hotel with ID: {}", hotelId);
        return hotel.getRooms()
                .stream()
                .map((element) -> modelMapper.map(element, RoomResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public RoomResponseDTO getRoomById(Long id) {
        log.info("Getting Room with ID: {}", id);
        Room room = roomRepository.findById(id).orElseThrow(() -> new RuntimeException("Room Not Found with id:" + id));
        log.info("Fetched Room with ID: {}", id);
        return modelMapper.map(room, RoomResponseDTO.class);
    }

    @Override
    public void deleteRoomById(Long id) {
        log.info("Deleting Room with ID: {}", id);
        Room room = roomRepository.findById(id).orElseThrow(() -> new RuntimeException("Room Not Found with id:" + id));

        User user = (User) Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getPrincipal();
        assert user != null;
        if (!user.equals(room.getHotel().getOwner())) {
            throw new UnauthorisedException("This User Does not Own this Room");
        }

        inventoryService.deleteAllInventories(room);
        roomRepository.deleteById(id);
    }

    @Override
    public RoomResponseDTO updateRoomById(Long hotelId, Long roomId, RoomDTO roomDTO) {
        log.info("Updating Room with ID : {}", roomId);
        Hotel hotel = hotelRepository.findById(hotelId).orElseThrow(() -> new ResourceNotFoundException("Hotel Not Found with ID :" + hotelId));

        User user = getCurrentUser();
        if (!user.equals(hotel.getOwner())) {
            throw new UnauthorisedException("This User Does not Own this Hotel");
        }

        Room room = roomRepository.findById(roomId).orElseThrow(() -> new ResourceNotFoundException("Room Not Found with ID :" + roomId));
        modelMapper.map(roomDTO, room);
        room.setId(roomId);
        Room updatedRoom = roomRepository.save(room);

        return modelMapper.map(updatedRoom, RoomResponseDTO.class);
    }
}
