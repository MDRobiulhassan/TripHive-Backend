package com.example.AirBnb_Clone.serviceImpl;

import com.example.AirBnb_Clone.dto.request.RoomDTO;
import com.example.AirBnb_Clone.dto.response.RoomResponseDTO;
import com.example.AirBnb_Clone.entity.Hotel;
import com.example.AirBnb_Clone.entity.Room;
import com.example.AirBnb_Clone.entity.User;
import com.example.AirBnb_Clone.exceptions.UnauthorisedException;
import com.example.AirBnb_Clone.repository.HotelRepository;
import com.example.AirBnb_Clone.repository.RoomRepository;
import com.example.AirBnb_Clone.service.InventoryService;
import com.example.AirBnb_Clone.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
        if(!user.equals(hotel.getOwner())){
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
        if(!user.equals(hotel.getOwner())){
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
        if(!user.equals(room.getHotel().getOwner())){
            throw new UnauthorisedException("This User Does not Own this Room");
        }

        inventoryService.deleteAllInventories(room);
        roomRepository.deleteById(id);
    }
}
