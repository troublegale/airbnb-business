package itmo.tg.airbnb_business.business.misc;

import itmo.tg.airbnb_business.business.dto.AdvertisementRequestDTO;
import itmo.tg.airbnb_business.business.dto.AdvertisementResponseDTO;
import itmo.tg.airbnb_business.business.model.Advertisement;

import java.util.List;

public class ModelDTOConverter {

    public static Advertisement convert(AdvertisementRequestDTO dto) {
        return Advertisement.builder()
                .address(dto.getAddress())
                .rooms(dto.getRooms())
                .bookPrice(dto.getBookPrice())
                .pricePerNight(dto.getPricePerNight())
                .build();
    }

    public static AdvertisementResponseDTO convert(Advertisement advertisement) {
        return AdvertisementResponseDTO.builder()
                .id(advertisement.getId())
                .address(advertisement.getAddress())
                .rooms(advertisement.getRooms())
                .bookPrice(advertisement.getBookPrice())
                .pricePerNight(advertisement.getPricePerNight())
                .status(advertisement.getStatus())
                .hostUsername(advertisement.getHost().getUsername())
                .build();
    }

    public static List<AdvertisementResponseDTO> toAdvertisementDtoList(List<Advertisement> adverts) {
        return adverts.stream().map(ModelDTOConverter::convert).toList();
    }

}
