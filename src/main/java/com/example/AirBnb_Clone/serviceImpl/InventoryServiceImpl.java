package com.example.AirBnb_Clone.serviceImpl;

import com.example.AirBnb_Clone.dto.request.HotelPriceDTO;
import com.example.AirBnb_Clone.dto.request.HotelSearchRequest;
import com.example.AirBnb_Clone.dto.response.HotelResponseDTO;
import com.example.AirBnb_Clone.entity.Hotel;
import com.example.AirBnb_Clone.entity.HotelMinPrice;
import com.example.AirBnb_Clone.entity.Inventory;
import com.example.AirBnb_Clone.entity.Room;
import com.example.AirBnb_Clone.repository.HotelMinPriceRepository;
import com.example.AirBnb_Clone.repository.InventoryRepository;
import com.example.AirBnb_Clone.service.InventoryService;
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

@Service
@Slf4j
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final HotelMinPriceRepository hotelMinPriceRepository;
    private final ModelMapper modelMapper;

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
        log.info("Searching Hotels with {} city , from {} to {}",hotelSearchRequest.getCity(),
                hotelSearchRequest.getCheckInDate(),hotelSearchRequest.getCheckOutDate());

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
}
