package itmo.tg.airbnb_business.business.controller;

import itmo.tg.airbnb_business.business.dto.HostDamageComplaintRequestDTO;
import itmo.tg.airbnb_business.business.dto.HostDamageComplaintResponseDTO;
import itmo.tg.airbnb_business.business.service.HostDamageComplaintService;
import itmo.tg.airbnb_business.security.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/damage-complaints")
@RequiredArgsConstructor
public class HostDamageComplaintController {

    private final HostDamageComplaintService hostDamageComplaintService;
    private final UserService userService;

    @GetMapping("/my")
    public List<HostDamageComplaintResponseDTO> getOwned(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer pageSize,
            @RequestParam(defaultValue = "all") @Pattern(regexp = "all|pending|resolved") String filter) {
        return hostDamageComplaintService.getOwned(userService.getCurrentUser(), page, pageSize, filter);
    }

    @PostMapping
    public HostDamageComplaintResponseDTO publish(
            @RequestBody @Valid HostDamageComplaintRequestDTO dto) {
        return hostDamageComplaintService.create(dto, userService.getCurrentUser());
    }

}
