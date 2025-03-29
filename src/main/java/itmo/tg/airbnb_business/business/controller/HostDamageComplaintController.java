package itmo.tg.airbnb_business.business.controller;

import itmo.tg.airbnb_business.auth.service.UserService;
import itmo.tg.airbnb_business.business.dto.HostDamageComplaintRequestDTO;
import itmo.tg.airbnb_business.business.dto.HostDamageComplaintResponseDTO;
import itmo.tg.airbnb_business.business.service.HostDamageComplaintService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/damage-complaint")
@RequiredArgsConstructor
public class HostDamageComplaintController {

    private final HostDamageComplaintService hostDamageComplaintService;
    private final UserService userService;

    @GetMapping("/my")
    public ResponseEntity<List<HostDamageComplaintResponseDTO>> getOwned(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer pageSize,
            @RequestParam(defaultValue = "all") @Pattern(regexp = "all|pending|resolved") String filter) {
        var response = hostDamageComplaintService.getOwned(userService.getCurrentUser(), page, pageSize, filter);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/publish")
    public ResponseEntity<HostDamageComplaintResponseDTO> publish(
            @RequestBody @Valid HostDamageComplaintRequestDTO dto) {
        var response = hostDamageComplaintService.create(dto, userService.getCurrentUser());
        return ResponseEntity.ok(response);
    }

}
