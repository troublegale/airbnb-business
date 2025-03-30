package itmo.tg.airbnb_business.business.controller;

import itmo.tg.airbnb_business.business.dto.FineDTO;
import itmo.tg.airbnb_business.business.service.FineService;
import itmo.tg.airbnb_business.security.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/fines")
@RequiredArgsConstructor
public class FineController {

    private final FineService fineService;
    private final UserService userService;

    @GetMapping
    public List<FineDTO> getAll(
            @RequestParam(defaultValue = "1") @Positive Integer page,
            @RequestParam(defaultValue = "20") @Positive Integer pageSize,
            @RequestParam(defaultValue = "false") @Valid Boolean active) {
        return fineService.getAll(page, pageSize, active);
    }

    @GetMapping("/my")
    public List<FineDTO> getAssigned(
            @RequestParam(defaultValue = "1") @Positive Integer page,
            @RequestParam(defaultValue = "20") @Positive Integer pageSize,
            @RequestParam(defaultValue = "true") @Valid Boolean active) {
        return fineService.getAssignedTo(userService.getCurrentUser(), page, pageSize, active);
    }

}
