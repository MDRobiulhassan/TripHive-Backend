package com.example.TripHive_Backend.serviceImpl;

import com.example.TripHive_Backend.dto.request.HotelPriceDTO;
import com.example.TripHive_Backend.dto.request.HotelSearchRequest;
import com.example.TripHive_Backend.dto.request.UpdateInventoryRequestDto;
import com.example.TripHive_Backend.dto.response.InventoryResponse;
import com.example.TripHive_Backend.entity.Inventory;
import com.example.TripHive_Backend.entity.Room;
import com.example.TripHive_Backend.entity.User;
import com.example.TripHive_Backend.exceptions.ResourceNotFoundException;
import com.example.TripHive_Backend.repository.HotelMinPriceRepository;
import com.example.TripHive_Backend.repository.InventoryRepository;
import com.example.TripHive_Backend.repository.RoomRepository;
import com.example.TripHive_Backend.service.InventoryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.TripHive_Backend.util.AppUtil.getCurrentUser;

@Service
@Slf4j
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final HotelMinPriceRepository hotelMinPriceRepository;
    private final ModelMapper modelMapper;
    private final RoomRepository roomRepository;

    @Override
    @Transactional
    public void initializeRoomForAYear(Room room) {
        LocalDate today = LocalDate.now();
        LocalDate endDate = today.plusYears(1);

        List<Inventory> inventories = new ArrayList<>();
        for (; !today.isAfter(endDate); today = today.plusDays(1)) {
            Inventory inventory = Inventory.builder()
                    .hotel(room.getHotel())
                    .room(room)
                    .date(today)
                    .bookedCount(0)
                    .reservedCount(0)
                    .city(room.getHotel().getCity())
                    .price(room.getBasePrice())
                    .surgeFactor(BigDecimal.ONE)
                    .totalCount(room.getTotalCount())
                    .closed(false)
                    .build();
            inventories.add(inventory);
        }
        inventoryRepository.saveAll(inventories);
    }


    @Override
    public void deleteAllInventories(Room room) {
        log.info("Deleting all inventories for Room ID: {}", room.getId());
        inventoryRepository.deleteByRoom(room);
    }

    @Override
    public Page<HotelPriceDTO> searchHotels(HotelSearchRequest hotelSearchRequest) {
        log.info("Searching Hotels with {} city , from {} to {}", hotelSearchRequest.getCity(),
                hotelSearchRequest.getCheckInDate(), hotelSearchRequest.getCheckOutDate());

        Pageable pageable = PageRequest.of(hotelSearchRequest.getPage(), hotelSearchRequest.getSize());
        Long dateCount = ChronoUnit.DAYS.between(hotelSearchRequest.getCheckInDate(), hotelSearchRequest.getCheckOutDate());

        return hotelMinPriceRepository.findHotelsWithAvailableInventory(
                hotelSearchRequest.getCity(),
                hotelSearchRequest.getCheckInDate(),
                hotelSearchRequest.getCheckOutDate(),
                hotelSearchRequest.getNumberOfRooms(),
                dateCount,
                pageable
        );
    }

    @Override
    public List<InventoryResponse> getAllInventoryByRoom(Long roomId) {
        log.info("Getting all inventories for Room ID: {}", roomId);
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new ResourceNotFoundException("Room Not Found with id:" + roomId));
        User user = getCurrentUser();
        if (!user.equals(room.getHotel().getOwner())) {
            throw new RuntimeException("This User Does not Own this Room");
        }

        return inventoryRepository.findByRoomOrderByDate(room).stream()
                .map(inventory -> modelMapper.map(inventory, InventoryResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void updateInventory(Long roomId, UpdateInventoryRequestDto updateInventoryRequestDto) {
        log.info("Updating inventory for Room ID: {} between Date Range: {} to {}", roomId, updateInventoryRequestDto.getStartDate(), updateInventoryRequestDto.getEndDate());
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new ResourceNotFoundException("Room Not Found with id:" + roomId));
        User user = getCurrentUser();
        if (!user.equals(room.getHotel().getOwner())) {
            throw new RuntimeException("This User Does not Own this Room");
        }

        inventoryRepository.getAllInventoryAndLockBeforeUpdate(roomId, updateInventoryRequestDto.getStartDate(), updateInventoryRequestDto.getEndDate());
        inventoryRepository.updateInventory(roomId, updateInventoryRequestDto.getStartDate(), updateInventoryRequestDto.getEndDate(), updateInventoryRequestDto.getClosed(), updateInventoryRequestDto.getSurgeFactor());
    }
}
