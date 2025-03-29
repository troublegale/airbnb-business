package itmo.tg.airbnb_business.business.service;

import itmo.tg.airbnb_business.security.model.Role;
import itmo.tg.airbnb_business.security.model.User;
import itmo.tg.airbnb_business.business.dto.AdvertisementRequestDTO;
import itmo.tg.airbnb_business.business.dto.AdvertisementResponseDTO;
import itmo.tg.airbnb_business.business.dto.BookingResponseDTO;
import itmo.tg.airbnb_business.business.exception.exceptions.ActiveBookingsException;
import itmo.tg.airbnb_business.business.exception.exceptions.NotAllowedException;
import itmo.tg.airbnb_business.business.misc.ModelDTOConverter;
import itmo.tg.airbnb_business.business.model.Advertisement;
import itmo.tg.airbnb_business.business.model.Booking;
import itmo.tg.airbnb_business.business.model.enums.AdvertisementStatus;
import itmo.tg.airbnb_business.business.model.enums.BookingStatus;
import itmo.tg.airbnb_business.business.repository.AdvertisementRepository;
import itmo.tg.airbnb_business.business.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class AdvertisementService {

    private final AdvertisementRepository advertisementRepository;
    private final BookingRepository bookingRepository;

    public AdvertisementResponseDTO get(Long id) {
        var advert = advertisementRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException("Advertisement #" + id + " not found"));
        return ModelDTOConverter.convert(advert);
    }

    public List<AdvertisementResponseDTO> getAll(Integer page, Integer pageSize, Boolean active) {
        List<Advertisement> adverts;
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by("id"));
        if (active) {
            adverts = advertisementRepository.findByStatus(AdvertisementStatus.ACTIVE, pageable).getContent();
        } else {
            adverts = advertisementRepository.findAll(pageable).getContent();
        }
        return ModelDTOConverter.toAdvertisementDTOList(adverts);
    }

    public List<BookingResponseDTO> getBookings(Long id, Integer page, Integer pageSize, Boolean active) {
        var advert = advertisementRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException("Advertisement #" + id + " not found"));
        List<Booking> bookings;
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by("id"));
        if (active) {
            bookings = bookingRepository.findByAdvertisementAndStatus(advert, BookingStatus.ACTIVE, pageable).getContent();
        } else {
            bookings = bookingRepository.findByAdvertisement(advert, pageable).getContent();
        }
        return ModelDTOConverter.toBookingDTOList(bookings);
    }

    public List<AdvertisementResponseDTO> getOwned(User host, Integer page, Integer pageSize, Boolean active) {
        List<Advertisement> adverts;
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by("id"));
        if (active) {
            adverts = advertisementRepository.findByHostAndStatus(host, AdvertisementStatus.ACTIVE, pageable).getContent();
        } else {
            adverts = advertisementRepository.findByHost(host, pageable).getContent();
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
    public AdvertisementResponseDTO update(Long id, AdvertisementRequestDTO dto, User host) {
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
    public void delete(Long id, User host) {
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
