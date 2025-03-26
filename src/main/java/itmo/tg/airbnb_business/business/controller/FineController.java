package itmo.tg.airbnb_business.business.controller;

import itmo.tg.airbnb_business.auth.service.UserService;
import itmo.tg.airbnb_business.business.dto.FineDTO;
import itmo.tg.airbnb_business.business.service.FineService;
import jakarta.validation.Valid;
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
    public ResponseEntity<List<FineDTO>> getAll() {
        var response = fineService.getAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FineDTO> get(@PathVariable Integer id) {
        var response = fineService.get(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/my")
    public ResponseEntity<List<FineDTO>> getAssigned(
            @RequestParam(defaultValue = "false") @Valid Boolean active) {
        var response = fineService.getAssignedTo(userService.getCurrentUser(), active);
        return ResponseEntity.ok(response);
    }

}
