package itmo.tg.airbnb_business.business.controller;

import itmo.tg.airbnb_business.business.dto.GuestComplaintResponseDTO;
import itmo.tg.airbnb_business.business.dto.HostDamageComplaintResponseDTO;
import itmo.tg.airbnb_business.business.dto.HostJustificationResponseDTO;
import itmo.tg.airbnb_business.business.service.GuestComplaintService;
import itmo.tg.airbnb_business.business.service.HostDamageComplaintService;
import itmo.tg.airbnb_business.business.service.HostJustificationService;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final GuestComplaintService guestComplaintService;
    private final HostDamageComplaintService hostDamageComplaintService;
    private final HostJustificationService hostJustificationService;

    @GetMapping("/guest-complaints")
    public ResponseEntity<List<GuestComplaintResponseDTO>> getGuestComplaints(
            @RequestParam(defaultValue = "1") @Positive Integer page,
            @RequestParam(defaultValue = "20") @Positive Integer pageSize,
            @RequestParam(defaultValue = "all") @Pattern(regexp = "all|pending|resolved") String filter) {
        var response = guestComplaintService.get(page, pageSize, filter);
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

    @GetMapping("/host-justifications")
    public ResponseEntity<List<HostJustificationResponseDTO>> getHostJustifications(
            @RequestParam(defaultValue = "1") @Positive Integer page,
            @RequestParam(defaultValue = "20") @Positive Integer pageSize,
            @RequestParam(defaultValue = "all") @Pattern(regexp = "all|pending|resolved") String filter) {
        var response = hostJustificationService.get(page, pageSize, filter);
        return ResponseEntity.ok(response);
    }

}
