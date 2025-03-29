package itmo.tg.airbnb_business.business.controller;

import itmo.tg.airbnb_business.business.dto.GuestComplaintResponseDTO;
import itmo.tg.airbnb_business.business.dto.HostDamageComplaintResponseDTO;
import itmo.tg.airbnb_business.business.dto.HostJustificationResponseDTO;
import itmo.tg.airbnb_business.business.service.GuestComplaintService;
import itmo.tg.airbnb_business.business.service.HostDamageComplaintService;
import itmo.tg.airbnb_business.business.service.HostJustificationService;
import itmo.tg.airbnb_business.security.service.UserService;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<List<GuestComplaintResponseDTO>> getGuestComplaints(
            @RequestParam(defaultValue = "1") @Positive Integer page,
            @RequestParam(defaultValue = "20") @Positive Integer pageSize,
            @RequestParam(defaultValue = "all") @Pattern(regexp = "all|pending|resolved") String filter) {
        var response = guestComplaintService.get(page, pageSize, filter);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/guest-complaints/{id}/approve")
    public ResponseEntity<GuestComplaintResponseDTO> approveGuestComplaint(@PathVariable Long id) {
        var response = guestComplaintService.approve(id, userService.getCurrentUser());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/guest-complaints/{id}/reject")
    public ResponseEntity<GuestComplaintResponseDTO> rejectGuestComplaint(@PathVariable Long id) {
        var response = guestComplaintService.reject(id, userService.getCurrentUser());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/damage-complaints")
    public ResponseEntity<List<HostDamageComplaintResponseDTO>> getDamageComplaints(
            @RequestParam(defaultValue = "1") @Positive Integer page,
            @RequestParam(defaultValue = "20") @Positive Integer pageSize,
            @RequestParam(defaultValue = "all") @Pattern(regexp = "all|pending|resolved") String filter) {
        var response = hostDamageComplaintService.get(page, pageSize, filter);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/damage-complaints/{id}/approve")
    public ResponseEntity<HostDamageComplaintResponseDTO> approveHostDamageComplaint(@PathVariable Long id) {
        var response = hostDamageComplaintService.approve(id, userService.getCurrentUser());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/damage-complaints/{id}/reject")
    public ResponseEntity<HostDamageComplaintResponseDTO> rejectHostDamageComplaint(@PathVariable Long id) {
        var response = hostDamageComplaintService.reject(id, userService.getCurrentUser());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/host-justifications")
    public ResponseEntity<List<HostJustificationResponseDTO>> getHostJustifications(
            @RequestParam(defaultValue = "1") @Positive Integer page,
            @RequestParam(defaultValue = "20") @Positive Integer pageSize,
            @RequestParam(defaultValue = "all") @Pattern(regexp = "all|pending|resolved") String filter) {
        var response = hostJustificationService.get(page, pageSize, filter);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/host-justifications/{id}/approve")
    public ResponseEntity<HostJustificationResponseDTO> approveHostJustification(@PathVariable Long id) {
        var response = hostJustificationService.approve(id, userService.getCurrentUser());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/host-justifications/{id}/reject")
    public ResponseEntity<HostJustificationResponseDTO> rejectHostJustification(@PathVariable Long id) {
        var response = hostJustificationService.reject(id, userService.getCurrentUser());
        return ResponseEntity.ok(response);
    }

}
