package itmo.tg.airbnb_business.business.controller;

import itmo.tg.airbnb_business.business.dto.*;
import itmo.tg.airbnb_business.business.exception.exceptions.ActiveBookingsException;
import itmo.tg.airbnb_business.business.service.AdvertisementBlockService;
import itmo.tg.airbnb_business.business.service.AdvertisementService;
import itmo.tg.airbnb_business.business.service.GuestComplaintService;
import itmo.tg.airbnb_business.security.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
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
    private final AdvertisementBlockService advertisementBlockService;
    private final UserService userService;
    private final GuestComplaintService guestComplaintService;

    @GetMapping
    public ResponseEntity<List<AdvertisementResponseDTO>> getAll(
            @RequestParam(defaultValue = "1") @Positive Integer page,
            @RequestParam(defaultValue = "20") @Positive Integer pageSize,
            @RequestParam(defaultValue = "false") @Valid Boolean active
    ) {
        var response = advertisementService.getAll(page, pageSize, active);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/my")
    public ResponseEntity<List<AdvertisementResponseDTO>> getOwned(
            @RequestParam(defaultValue = "1") @Positive Integer page,
            @RequestParam(defaultValue = "20") @Positive Integer pageSize,
            @RequestParam(defaultValue = "false") @Valid Boolean active) {
        var response = advertisementService.getOwned(userService.getCurrentUser(), page, pageSize, active);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdvertisementResponseDTO> get(@PathVariable Long id) {
        var response = advertisementService.get(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/complaints")
    public ResponseEntity<List<GuestComplaintResponseDTO>> getComplaintForAdvert(
            @PathVariable Long id,
            @RequestParam(defaultValue = "1") @Positive Integer page,
            @RequestParam(defaultValue = "20") @Positive Integer pageSize,
            @RequestParam(defaultValue = "true") @Valid Boolean approved) {
        var response = guestComplaintService.getForAdvertisement(id, page, pageSize, approved, userService.getCurrentUser());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/blocks")
    public ResponseEntity<List<AdvertisementBlockDTO>> getBlocksForAdvert(
            @PathVariable Long id,
            @RequestParam(defaultValue = "1") @Positive Integer page,
            @RequestParam(defaultValue = "20") @Positive Integer pageSize) {
        var response = advertisementBlockService.getForAdvert(id, page, pageSize, userService.getCurrentUser());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/bookings")
    public ResponseEntity<List<BookingResponseDTO>> getBookings(
            @PathVariable Long id,
            @RequestParam(defaultValue = "1") @Positive Integer page,
            @RequestParam(defaultValue = "20") @Positive Integer pageSize,
            @RequestParam(defaultValue = "false") @Valid Boolean active) {
        var response = advertisementService.getBookings(id, page, pageSize, active);
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

    @ExceptionHandler(ActiveBookingsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<String> handleActiveBookingsException(ActiveBookingsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

}
