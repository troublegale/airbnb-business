package itmo.tg.airbnb_business.business.controller;

import itmo.tg.airbnb_business.auth.model.User;
import itmo.tg.airbnb_business.auth.service.UserService;
import itmo.tg.airbnb_business.business.dto.BookingRequestDTO;
import itmo.tg.airbnb_business.business.dto.BookingResponseDTO;
import itmo.tg.airbnb_business.business.exception.exceptions.AdvertisementBlockedException;
import itmo.tg.airbnb_business.business.exception.exceptions.BookOwnAdvertisementException;
import itmo.tg.airbnb_business.business.exception.exceptions.BookingDatesConflictException;
import itmo.tg.airbnb_business.business.exception.exceptions.InvalidBookingDatesException;
import itmo.tg.airbnb_business.business.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<BookingResponseDTO>> getAll() {
        var response = bookingService.getAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/my")
    public ResponseEntity<List<BookingResponseDTO>> getOwned(
            @RequestParam(defaultValue = "false") @Valid Boolean active) {
        var response = bookingService.getOwned(userService.getCurrentUser(), active);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingResponseDTO> get(@PathVariable Integer id) {
        var response = bookingService.get(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/create")
    public ResponseEntity<BookingResponseDTO> create(
            @RequestBody @Valid BookingRequestDTO dto) {
        var response = bookingService.create(dto, userService.getCurrentUser());
        return ResponseEntity.ok(response);
    }

    @ExceptionHandler(BookOwnAdvertisementException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<String> handleBookOwnAdvertisementException(BookOwnAdvertisementException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(BookingDatesConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<String> handleBookingDatesConflictException(BookingDatesConflictException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(InvalidBookingDatesException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleInvalidBookingDatesException(InvalidBookingDatesException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(AdvertisementBlockedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<String> handleAdvertisementBlockedException(AdvertisementBlockedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }

}
