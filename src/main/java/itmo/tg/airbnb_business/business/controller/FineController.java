package itmo.tg.airbnb_business.business.controller;

import itmo.tg.airbnb_business.security.service.UserService;
import itmo.tg.airbnb_business.business.dto.FineDTO;
import itmo.tg.airbnb_business.business.service.FineService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fines")
@RequiredArgsConstructor
public class FineController {

    private final FineService fineService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<FineDTO>> getAll(
            @RequestParam(defaultValue = "1") @Positive Integer page,
            @RequestParam(defaultValue = "20") @Positive Integer pageSize,
            @RequestParam(defaultValue = "false") @Valid Boolean active) {
        var response = fineService.getAll(page, pageSize, active);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/my")
    public ResponseEntity<List<FineDTO>> getAssigned(
            @RequestParam(defaultValue = "1") @Positive Integer page,
            @RequestParam(defaultValue = "20") @Positive Integer pageSize,
            @RequestParam(defaultValue = "true") @Valid Boolean active) {
        var response = fineService.getAssignedTo(userService.getCurrentUser(), page, pageSize, active);
        return ResponseEntity.ok(response);
    }

}
