package itmo.tg.airbnb_business.business.controller;

import itmo.tg.airbnb_business.business.dto.GuestComplaintResponseDTO;
import itmo.tg.airbnb_business.business.dto.HostDamageComplaintResponseDTO;
import itmo.tg.airbnb_business.business.dto.HostJustificationResponseDTO;
import itmo.tg.airbnb_business.business.exception.exceptions.TicketAlreadyResolvedException;
import itmo.tg.airbnb_business.business.service.GuestComplaintService;
import itmo.tg.airbnb_business.business.service.HostDamageComplaintService;
import itmo.tg.airbnb_business.business.service.HostJustificationService;
import itmo.tg.airbnb_business.security.service.UserService;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final GuestComplaintService guestComplaintService;
    private final HostDamageComplaintService hostDamageComplaintService;
    private final HostJustificationService hostJustificationService;
    private final UserService userService;

    @GetMapping("/guest-complaints")
    public List<GuestComplaintResponseDTO> getGuestComplaints(
            @RequestParam(defaultValue = "1") @Positive Integer page,
            @RequestParam(defaultValue = "20") @Positive Integer pageSize,
            @RequestParam(defaultValue = "all") @Pattern(regexp = "all|pending|resolved") String filter) {
        return guestComplaintService.getList(page, pageSize, filter);
    }

    @GetMapping("/guest-complaints/{id}")
    public GuestComplaintResponseDTO getGuestComplaint(@PathVariable Long id) {
        return guestComplaintService.get(id);
    }

    @PostMapping("/guest-complaints/{id}")
    public GuestComplaintResponseDTO approveGuestComplaint(@PathVariable Long id) {
        return guestComplaintService.approve(id, userService.getCurrentUser());
    }

    @PutMapping("/guest-complaints/{id}")
    public GuestComplaintResponseDTO rejectGuestComplaint(@PathVariable Long id) {
        return guestComplaintService.reject(id, userService.getCurrentUser());
    }

    @GetMapping("/damage-complaints")
    public List<HostDamageComplaintResponseDTO> getDamageComplaints(
            @RequestParam(defaultValue = "1") @Positive Integer page,
            @RequestParam(defaultValue = "20") @Positive Integer pageSize,
            @RequestParam(defaultValue = "all") @Pattern(regexp = "all|pending|resolved") String filter) {
        return hostDamageComplaintService.getList(page, pageSize, filter);
    }

    @GetMapping("/damage-complaints/{id}")
    public HostDamageComplaintResponseDTO getDamageComplaint(@PathVariable Long id) {
        return hostDamageComplaintService.get(id);
    }

    @PostMapping("/damage-complaints/{id}")
    public HostDamageComplaintResponseDTO approveHostDamageComplaint(@PathVariable Long id) {
        return hostDamageComplaintService.approve(id, userService.getCurrentUser());
    }

    @PutMapping("/damage-complaints/{id}")
    public HostDamageComplaintResponseDTO rejectHostDamageComplaint(@PathVariable Long id) {
        return hostDamageComplaintService.reject(id, userService.getCurrentUser());
    }

    @GetMapping("/justifications")
    public List<HostJustificationResponseDTO> getHostJustifications(
            @RequestParam(defaultValue = "1") @Positive Integer page,
            @RequestParam(defaultValue = "20") @Positive Integer pageSize,
            @RequestParam(defaultValue = "all") @Pattern(regexp = "all|pending|resolved") String filter) {
        return hostJustificationService.getList(page, pageSize, filter);
    }

    @GetMapping("/justifications/{id}")
    public HostJustificationResponseDTO getHostJustification(@PathVariable Long id) {
        return hostJustificationService.get(id);
    }

    @PostMapping("/justifications/{id}")
    public HostJustificationResponseDTO approveHostJustification(@PathVariable Long id) {
        return hostJustificationService.approve(id, userService.getCurrentUser());
    }

    @PutMapping("/justifications/{id}")
    public HostJustificationResponseDTO rejectHostJustification(@PathVariable Long id) {
        return hostJustificationService.reject(id, userService.getCurrentUser());
    }

    @ExceptionHandler(TicketAlreadyResolvedException.class)
    public ResponseEntity<String> handleTicketAlreadyResolvedException(TicketAlreadyResolvedException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

}
