package itmo.tg.airbnb_business.business.controller;

import itmo.tg.airbnb_business.auth.service.UserService;
import itmo.tg.airbnb_business.business.dto.HostJustificationRequestDTO;
import itmo.tg.airbnb_business.business.dto.HostJustificationResponseDTO;
import itmo.tg.airbnb_business.business.service.HostJustificationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/justifications")
@RequiredArgsConstructor
public class HostJustificationController {

    private final HostJustificationService hostJustificationService;
    private final UserService userService;

    @GetMapping("/my")
    public ResponseEntity<List<HostJustificationResponseDTO>> getOwned(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer pageSize,
            @RequestParam(defaultValue = "all") @Pattern(regexp = "all|pending|resolved") String filter) {
        var response = hostJustificationService.getOwned(userService.getCurrentUser(), page, pageSize, filter);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/publish")
    public ResponseEntity<HostJustificationResponseDTO> publish(
            @RequestBody @Valid HostJustificationRequestDTO dto) {
        var response = hostJustificationService.create(dto, userService.getCurrentUser());
        return ResponseEntity.ok(response);
    }

}
