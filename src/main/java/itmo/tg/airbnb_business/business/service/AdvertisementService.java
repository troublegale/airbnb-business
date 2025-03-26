package itmo.tg.airbnb_business.business.service;

import itmo.tg.airbnb_business.auth.model.Role;
import itmo.tg.airbnb_business.auth.model.User;
import itmo.tg.airbnb_business.business.dto.AdvertisementRequestDTO;
import itmo.tg.airbnb_business.business.dto.AdvertisementResponseDTO;
import itmo.tg.airbnb_business.business.exception.exceptions.ActiveBookingsException;
import itmo.tg.airbnb_business.business.exception.exceptions.NotAllowedException;
import itmo.tg.airbnb_business.business.misc.ModelDTOConverter;
import itmo.tg.airbnb_business.business.model.Advertisement;
import itmo.tg.airbnb_business.business.model.enums.AdvertisementStatus;
import itmo.tg.airbnb_business.business.model.enums.BookingStatus;
import itmo.tg.airbnb_business.business.repository.AdvertisementRepository;
import itmo.tg.airbnb_business.business.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class AdvertisementService {

    private final AdvertisementRepository advertisementRepository;
    private final BookingRepository bookingRepository;

    public AdvertisementResponseDTO get(Integer id) {
        var advert = advertisementRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException("Advertisement #" + id + " not found"));
        return ModelDTOConverter.convert(advert);
    }

    public List<AdvertisementResponseDTO> getAll() {
        var adverts = advertisementRepository.findAll();
        return ModelDTOConverter.toAdvertisementDTOList(adverts);
    }

    public List<AdvertisementResponseDTO> getOwned(User host, Boolean active) {
        List<Advertisement> adverts;
        if (active) {
            adverts = advertisementRepository.findByHostAndStatus(host, AdvertisementStatus.ACTIVE);
        } else {
            adverts = advertisementRepository.findByHost(host);
        }
        return ModelDTOConverter.toAdvertisementDTOList(adverts);
    }

    @Transactional
    public AdvertisementResponseDTO create(AdvertisementRequestDTO dto, User host) {
        var advert = ModelDTOConverter.convert(dto, host);
        advert.setStatus(AdvertisementStatus.ACTIVE);
        advertisementRepository.save(advert);
        return ModelDTOConverter.convert(advert);
    }

    @Transactional
    public AdvertisementResponseDTO update(Integer id, AdvertisementRequestDTO dto, User host) {
        var advert = advertisementRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException("Advertisement #" + id + " not found"));
        if (advert.getHost().equals(host) || host.getRole() == Role.ROLE_ADMIN) {
            advert.setAddress(dto.getAddress());
            advert.setRooms(dto.getRooms());
            advert.setBookPrice(dto.getBookPrice());
            advert.setPricePerNight(dto.getPricePerNight());
            advertisementRepository.save(advert);
            return ModelDTOConverter.convert(advert);
        }
        throw new NotAllowedException("Not allowed to update advertisement #" + id);
    }

    @Transactional
    public void delete(Integer id, User host) {
        var advert = advertisementRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException("Advertisement #" + id + " not found"));
        if (!advert.getHost().equals(host) && host.getRole() != Role.ROLE_ADMIN) {
            throw new NotAllowedException("Not allowed to delete advertisement #" + id);
        }
        if (bookingRepository.existsByAdvertisementAndStatus(advert, BookingStatus.ACTIVE)) {
            throw new ActiveBookingsException("There are active bookings on advertisement #" + id +
                    "\nCancel them or wait until their expiration.");
        }
        advertisementRepository.delete(advert);
    }

}
