package com.example.TripHive_Backend.serviceImpl;

import com.example.TripHive_Backend.dto.request.BookingRequestDTO;
import com.example.TripHive_Backend.dto.request.GuestDTO;
import com.example.TripHive_Backend.dto.response.BookingResponseDTO;
import com.example.TripHive_Backend.dto.response.GuestResponseDTO;
import com.example.TripHive_Backend.dto.response.HotelReportResponse;
import com.example.TripHive_Backend.entity.*;
import com.example.TripHive_Backend.enums.BookingStatus;
import com.example.TripHive_Backend.exceptions.ResourceNotFoundException;
import com.example.TripHive_Backend.exceptions.UnauthorisedException;
import com.example.TripHive_Backend.repository.*;
import com.example.TripHive_Backend.service.BookingService;
import com.example.TripHive_Backend.service.CheckoutService;
import com.example.TripHive_Backend.strategy.PricingService;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.Refund;
import com.stripe.model.checkout.Session;
import com.stripe.param.RefundCreateParams;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.example.TripHive_Backend.util.AppUtil.getCurrentUser;

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
    private final CheckoutService checkoutService;
    private final PricingService pricingService;

    @Value("${frontend.url}")
    private String frontendUrl;

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

        inventoryRepository.initBooking(room.getId(), bookingRequestDTO.getCheckInDate(), bookingRequestDTO.getCheckOutDate(), bookingRequestDTO.getNumberOfRooms());

        BigDecimal priceForOneRoom = pricingService.calculateTotalPricing(inventoryList);
        BigDecimal totalPrice = priceForOneRoom.multiply(BigDecimal.valueOf(bookingRequestDTO.getNumberOfRooms()));

        Booking booking = Booking.builder()
                .bookingStatus(BookingStatus.RESERVED)
                .hotel(hotel)
                .room(room)
                .user(getCurrentUser())
                .checkInDate(bookingRequestDTO.getCheckInDate())
                .checkOutDate(bookingRequestDTO.getCheckOutDate())
                .roomsCount(bookingRequestDTO.getNumberOfRooms())
                .amount(totalPrice)
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

        if (!Objects.equals(booking.getUser().getId(), user.getId())) {
            throw new UnauthorisedException("User is not the owner of the booking");
        }

        if (hasBookingExpired(booking)) {
            throw new IllegalArgumentException("Booking has already expired");
        }

        if (booking.getBookingStatus() != BookingStatus.RESERVED) {
            throw new IllegalArgumentException("Guests can be added only to RESERVED bookings");
        }

        List<GuestResponseDTO> addedGuests = new ArrayList<>();

        for (GuestDTO guestDTO : guestDTOList) {
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

    @Override
    public String initiatePayments(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking with ID : {} not found" + bookingId));

        User user = getCurrentUser();
        if (!Objects.equals(booking.getUser().getId(), user.getId())) {
            throw new UnauthorisedException("User is not the owner of the booking");
        }
        if (hasBookingExpired(booking)) {
            throw new IllegalArgumentException("Booking has already expired");
        }

        String sessionUrl = checkoutService.getCheckoutUrl(booking, frontendUrl + "/payment/success", frontendUrl + "/payment/failure");

        booking.setBookingStatus(BookingStatus.PAYMENT_PENDING);
        bookingRepository.save(booking);
        return sessionUrl;
    }

    @Override
    @Transactional
    public void capturePayment(Event event) {

        log.info("EVENT RECEIVED: {}", event.getType());

        if (!"checkout.session.completed".equals(event.getType())) {
            log.info("Ignoring event: {}", event.getType());
            return;
        }

        Session session = (Session) event.getDataObjectDeserializer()
                .getObject()
                .orElse(null);

        if (session == null) {
            log.error("Session is null");
            return;
        }

        String sessionId = session.getId();

        Booking booking = bookingRepository.findByPaymentSessionId(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Booking not found for session: " + sessionId
                ));

        int updated = inventoryRepository.confirmBooking(
                booking.getRoom().getId(),
                booking.getCheckInDate(),
                booking.getCheckOutDate(),
                booking.getRoomsCount()
        );

        if (updated == 0) {
            throw new RuntimeException("Inventory update failed after payment");
        }

        booking.setBookingStatus(BookingStatus.CONFIRMED);
        bookingRepository.save(booking);

//        inventoryRepository.findAndLockReservedInventory(booking.getRoom().getId(), booking.getCheckInDate(), booking.getCheckOutDate(), booking.getRoomsCount());
//        inventoryRepository.confirmBooking(booking.getRoom().getId(), booking.getCheckInDate(), booking.getCheckOutDate(), booking.getRoomsCount());

        log.info("Payment captured successfully for booking ID: {}", booking.getId());
    }

    @Transactional
    @Override
    public void cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking with ID : {} not found" + bookingId));

        User user = getCurrentUser();
        if (!Objects.equals(booking.getUser().getId(), user.getId())) {
            throw new UnauthorisedException("User is not the owner of the booking");
        }

        if (booking.getBookingStatus() != BookingStatus.CONFIRMED) {
            throw new IllegalStateException("Only confirmed bookings can be cancelled");
        }

        booking.setBookingStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);

        List<Inventory> lockedInventories = inventoryRepository.findAndLockReservedInventory(
                booking.getRoom().getId(),
                booking.getCheckInDate(),
                booking.getCheckOutDate(),
                booking.getRoomsCount()
        );

        if (lockedInventories.isEmpty()) {
            throw new RuntimeException("No inventory found to cancel");
        }

        inventoryRepository.cancelBooking(
                booking.getRoom().getId(),
                booking.getCheckInDate(),
                booking.getCheckOutDate(),
                booking.getRoomsCount()
        );

        // refund
        try {
            Session session = Session.retrieve(booking.getPaymentSessionId());
            RefundCreateParams refundParams = RefundCreateParams.builder()
                    .setPaymentIntent(session.getPaymentIntent())
                    .build();

            Refund.create(refundParams);
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getBookingStatus(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking with ID : {} not found" + bookingId));

        User user = getCurrentUser();
        if (!Objects.equals(booking.getUser().getId(), user.getId())) {
            throw new UnauthorisedException("User is not the owner of the booking");
        }

        return booking.getBookingStatus().name();
    }

    @Override
    public List<BookingResponseDTO> getAllBookingsByHotel(Long hotelId) {
        Hotel hotel = hotelRepository.findById(hotelId).orElseThrow(() -> new ResourceNotFoundException("Hotel with ID " + hotelId + " not found"));
        User user = getCurrentUser();

        log.info("Getting all bookings for hotel with ID: {}", hotelId);

        if (!Objects.equals(hotel.getOwner().getId(), user.getId())) {
            throw new AccessDeniedException("User is not the owner of the hotel");
        }

        List<Booking> bookings = bookingRepository.findByHotel(hotel);

        return bookings.stream()
                .map((booking) -> modelMapper.map(booking, BookingResponseDTO.class))
                .toList();
    }

    @Override
    public HotelReportResponse getHotelReport(Long hotelId, LocalDate startDate, LocalDate endDate) {
        Hotel hotel = hotelRepository.findById(hotelId).orElseThrow(() -> new ResourceNotFoundException("Hotel with ID : {} not found" + hotelId));
        User user = getCurrentUser();

        log.info("Generating report for hotel with ID: {} from {} to {}", hotelId, startDate, endDate);

        if (!Objects.equals(hotel.getOwner().getId(), user.getId())) {
            throw new AccessDeniedException("User is not the owner of the hotel");
        }

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

        List<Booking> bookings = bookingRepository.findByHotelAndCreatedAtBetween(hotel, startDateTime, endDateTime);

        Long totalConfirmedBookings = bookings.stream()
                .filter(booking -> booking.getBookingStatus() == BookingStatus.CONFIRMED)
                .count();

        BigDecimal totalRevenueOfConfirmedBookings = bookings.stream()
                .filter(booking -> booking.getBookingStatus() == BookingStatus.CONFIRMED)
                .map(Booking::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal avgRevenue = totalConfirmedBookings == 0 ? BigDecimal.ZERO : totalRevenueOfConfirmedBookings.divide(BigDecimal.valueOf(totalConfirmedBookings), RoundingMode.HALF_UP);

        return new HotelReportResponse(totalConfirmedBookings, totalRevenueOfConfirmedBookings, avgRevenue);
    }

    @Override
    public List<BookingResponseDTO> getMyBookings() {
        User user = getCurrentUser();

        return bookingRepository.findByUser(user).stream()
                .map(booking -> modelMapper.map(booking, BookingResponseDTO.class))
                .collect(Collectors.toList());
    }


    private Boolean hasBookingExpired(Booking booking) {
        return booking.getCreatedAt().plusMinutes(10).isBefore(LocalDateTime.now());
    }
}
