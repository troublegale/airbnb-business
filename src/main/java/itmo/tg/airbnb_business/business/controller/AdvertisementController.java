package itmo.tg.airbnb_business.business.controller;

import itmo.tg.airbnb_business.auth.service.UserService;
import itmo.tg.airbnb_business.business.dto.AdvertisementRequestDTO;
import itmo.tg.airbnb_business.business.dto.AdvertisementResponseDTO;
import itmo.tg.airbnb_business.business.dto.BookingResponseDTO;
import itmo.tg.airbnb_business.business.exception.exceptions.ActiveBookingsException;
import itmo.tg.airbnb_business.business.service.AdvertisementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/advertisements")
@RequiredArgsConstructor
public class AdvertisementController {

    private final AdvertisementService advertisementService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<AdvertisementResponseDTO>> getAll() {
        var response = advertisementService.getAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/my")
    public ResponseEntity<List<AdvertisementResponseDTO>> getOwned(
            @RequestParam(defaultValue = "false") @Valid Boolean active) {
        var response = advertisementService.getOwned(userService.getCurrentUser(), active);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdvertisementResponseDTO> get(@PathVariable Long id) {
        var response = advertisementService.get(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/bookings")
    public ResponseEntity<List<BookingResponseDTO>> getBookings(
            @PathVariable Long id, @RequestParam(defaultValue = "false") @Valid Boolean showAll) {
        var response = advertisementService.getBookings(id, showAll);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/create")
    public ResponseEntity<AdvertisementResponseDTO> create(
            @RequestBody @Valid AdvertisementRequestDTO dto) {
        var response = advertisementService.create(dto, userService.getCurrentUser());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<AdvertisementResponseDTO> update(
            @PathVariable Long id,
            @RequestBody @Valid AdvertisementRequestDTO dto) {
        var response = advertisementService.update(id, dto, userService.getCurrentUser());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        advertisementService.delete(id, userService.getCurrentUser());
        return ResponseEntity.ok("Deleted advertisement #" + id);
    }

    @ExceptionHandler(ActiveBookingsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<String> handleActiveBookingsException(ActiveBookingsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

}
