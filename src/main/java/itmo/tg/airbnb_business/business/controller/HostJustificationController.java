package itmo.tg.airbnb_business.business.controller;

import itmo.tg.airbnb_business.business.dto.HostJustificationRequestDTO;
import itmo.tg.airbnb_business.business.dto.HostJustificationResponseDTO;
import itmo.tg.airbnb_business.business.service.HostJustificationService;
import itmo.tg.airbnb_business.security.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/justifications")
@RequiredArgsConstructor
public class HostJustificationController {

    private final HostJustificationService hostJustificationService;
    private final UserService userService;

    @GetMapping("/my")
    public List<HostJustificationResponseDTO> getOwned(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer pageSize,
            @RequestParam(defaultValue = "all") @Pattern(regexp = "all|pending|resolved") String filter) {
        return hostJustificationService.getOwned(userService.getCurrentUser(), page, pageSize, filter);
    }

    @PostMapping
    public HostJustificationResponseDTO publish(
            @RequestBody @Valid HostJustificationRequestDTO dto) {
        return hostJustificationService.create(dto, userService.getCurrentUser());
    }

}
