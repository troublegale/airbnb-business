package itmo.tg.airbnb_business.business.controller;

import itmo.tg.airbnb_business.business.dto.GuestComplaintRequestDTO;
import itmo.tg.airbnb_business.business.dto.GuestComplaintResponseDTO;
import itmo.tg.airbnb_business.business.exception.exceptions.BookingAlreadyExpiredException;
import itmo.tg.airbnb_business.business.service.GuestComplaintService;
import itmo.tg.airbnb_business.security.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/guest-complaints")
@RequiredArgsConstructor
public class GuestComplaintController {

    private final GuestComplaintService guestComplaintService;
    private final UserService userService;

    @GetMapping("/my")
    public ResponseEntity<List<GuestComplaintResponseDTO>> getOwned(
            @RequestParam(defaultValue = "1") @Positive Integer page,
            @RequestParam(defaultValue = "20") @Positive Integer pageSize,
            @RequestParam(defaultValue = "all") @Pattern(regexp = "all|pending|resolved") String filter) {
        var response = guestComplaintService.getOwned(userService.getCurrentUser(), page, pageSize, filter);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/publish")
    public ResponseEntity<GuestComplaintResponseDTO> publish(
            @RequestBody @Valid GuestComplaintRequestDTO guestComplaintRequestDTO) {
        var response = guestComplaintService.create(guestComplaintRequestDTO, userService.getCurrentUser());
        return ResponseEntity.ok(response);
    }

    @ExceptionHandler(BookingAlreadyExpiredException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<String> handleBookingAlreadyExpiredException(BookingAlreadyExpiredException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

}
