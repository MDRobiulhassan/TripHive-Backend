package com.example.AirBnb_Clone.serviceImpl;

import com.example.AirBnb_Clone.dto.request.BookingRequestDTO;
import com.example.AirBnb_Clone.dto.request.GuestDTO;
import com.example.AirBnb_Clone.dto.response.BookingResponseDTO;
import com.example.AirBnb_Clone.dto.response.GuestResponseDTO;
import com.example.AirBnb_Clone.entity.*;
import com.example.AirBnb_Clone.entity.enums.BookingStatus;
import com.example.AirBnb_Clone.exceptions.ResourceNotFoundException;
import com.example.AirBnb_Clone.exceptions.UnauthorisedException;
import com.example.AirBnb_Clone.repository.*;
import com.example.AirBnb_Clone.service.BookingService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    private final ModelMapper modelMapper;
    private final InventoryRepository inventoryRepository;
    private final GuestRepository guestRepository;

    @Override
    @Transactional
    public BookingResponseDTO initialiseBooking(BookingRequestDTO bookingRequestDTO) {
        log.info("initialising booking for Hotel: {} and Room: {} and date: {}-{}", bookingRequestDTO.getHotelId(), bookingRequestDTO.getRoomId(), bookingRequestDTO.getCheckInDate(), bookingRequestDTO.getCheckOutDate());
        Hotel hotel = hotelRepository.findById(bookingRequestDTO.getHotelId()).orElseThrow(() -> new RuntimeException("Hotel not found"));
        Room room = roomRepository.findById(bookingRequestDTO.getRoomId()).orElseThrow(() -> new RuntimeException("Room not found"));

        List<Inventory> inventoryList = inventoryRepository.findAndLockAvailableInventories(room.getId(), bookingRequestDTO.getCheckInDate(), bookingRequestDTO.getCheckOutDate(), bookingRequestDTO.getNumberOfRooms());

        long daysCount = ChronoUnit.DAYS.between(bookingRequestDTO.getCheckInDate(), bookingRequestDTO.getCheckOutDate()) + 1;
        if (inventoryList.size() != daysCount) {
            log.info("Requested days: {}, Inventory rows found: {}", daysCount, inventoryList.size());
            throw new RuntimeException("Rooms not available");
        }

        for (Inventory inventory : inventoryList) {
            inventory.setReservedCount(inventory.getReservedCount() + bookingRequestDTO.getNumberOfRooms());
        }

        inventoryRepository.saveAll(inventoryList);

        Booking booking = Booking.builder()
                .bookingStatus(BookingStatus.RESERVED)
                .hotel(hotel)
                .room(room)
                .user(getCurrentUser())
                .checkInDate(bookingRequestDTO.getCheckInDate())
                .checkOutDate(bookingRequestDTO.getCheckOutDate())
                .roomsCount(bookingRequestDTO.getNumberOfRooms())
                .amount(BigDecimal.TEN)
                .build();

        booking = bookingRepository.save(booking);
        log.info("Booking initialised with ID: {}", booking.getId());
        modelMapper.typeMap(Booking.class, BookingResponseDTO.class)
                .addMapping(Booking::getRoomsCount, BookingResponseDTO::setNumberOfRooms);

        return modelMapper.map(booking, BookingResponseDTO.class);
    }

    @Override
    public List<GuestResponseDTO> addGuests(Long bookingId, List<GuestDTO> guestDTOList) {
        log.info("Adding guests to booking ID: {}", bookingId);

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking with ID : {} not found" + bookingId));
        User user = getCurrentUser();

        if(!user.equals(booking.getUser())){
            throw new UnauthorisedException("User is not the owner of the booking");
        }

        if(hasBookingExpired(booking)){
            throw new IllegalArgumentException("Booking has already expired");
        }

        if(booking.getBookingStatus() != BookingStatus.RESERVED){
            throw new IllegalArgumentException("Guests can be added only to RESERVED bookings");
        }

        List<GuestResponseDTO> addedGuests = new ArrayList<>();

        for(GuestDTO guestDTO : guestDTOList){
            Guest guest = modelMapper.map(guestDTO, Guest.class);
            guest.setUser(user);
            guest = guestRepository.save(guest);
            booking.getGuests().add(guest);

            addedGuests.add(modelMapper.map(guest, GuestResponseDTO.class));
        }

        booking.setBookingStatus(BookingStatus.GUESTS_ADDED);
        bookingRepository.save(booking);

        return addedGuests;
    }


    private Boolean hasBookingExpired(Booking booking) {
        return booking.getCreatedAt().plusMinutes(10).isBefore(LocalDateTime.now());
    }

    private User getCurrentUser() {
        return (User) Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getPrincipal();
    }
}
