package itmo.tg.airbnb_business.business.controller;

import itmo.tg.airbnb_business.auth.service.UserService;
import itmo.tg.airbnb_business.business.dto.AdvertisementRequestDTO;
import itmo.tg.airbnb_business.business.dto.AdvertisementResponseDTO;
import itmo.tg.airbnb_business.business.service.AdvertisementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/advertisements")
@RequiredArgsConstructor
public class AdvertisementController {

    private final AdvertisementService advertisementService;
    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<AdvertisementResponseDTO> createAdvertisement(
            @RequestBody @Valid AdvertisementRequestDTO dto) {
        var response = advertisementService.create(dto, userService.getCurrentUser());
        return ResponseEntity.ok(response);
    }

}
