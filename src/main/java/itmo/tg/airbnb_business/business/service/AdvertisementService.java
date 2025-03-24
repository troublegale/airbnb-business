package itmo.tg.airbnb_business.business.service;

import itmo.tg.airbnb_business.auth.model.User;
import itmo.tg.airbnb_business.business.dto.AdvertisementRequestDTO;
import itmo.tg.airbnb_business.business.dto.AdvertisementResponseDTO;
import itmo.tg.airbnb_business.business.misc.ModelDTOConverter;
import itmo.tg.airbnb_business.business.model.enums.AdvertisementStatus;
import itmo.tg.airbnb_business.business.repository.AdvertisementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdvertisementService {

    private final AdvertisementRepository advertisementRepository;

    public AdvertisementResponseDTO create(AdvertisementRequestDTO dto, User host) {
        var advert = ModelDTOConverter.convert(dto);
        advert.setStatus(AdvertisementStatus.ACTIVE);
        advert.setHost(host);
        advertisementRepository.save(advert);
        return ModelDTOConverter.convert(advert);
    }

}
