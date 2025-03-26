package itmo.tg.airbnb_business.business.service;

import itmo.tg.airbnb_business.auth.model.Role;
import itmo.tg.airbnb_business.auth.model.User;
import itmo.tg.airbnb_business.business.dto.BookingRequestDTO;
import itmo.tg.airbnb_business.business.dto.BookingResponseDTO;
import itmo.tg.airbnb_business.business.exception.exceptions.NotAllowedException;
import itmo.tg.airbnb_business.business.misc.ModelDTOConverter;
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
public class BookingService {

    private final AdvertisementRepository advertisementRepository;
    private final BookingRepository bookingRepository;

    public BookingResponseDTO get(Integer id) {
        var booking = bookingRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException("Booking #" + id + " not found"));
        return ModelDTOConverter.convert(booking);
    }

    public List<BookingResponseDTO> getAll() {
        var bookings = bookingRepository.findAll();
        return ModelDTOConverter.toBookingDTOList(bookings);
    }

    @Transactional
    public BookingResponseDTO create(BookingRequestDTO dto, User guest) {
        var advertId = dto.getAdvertisementId();
        var advert = advertisementRepository.findById(advertId).orElseThrow(() ->
                new NoSuchElementException("Advertisement #" + advertId + " not found"));
        var booking = ModelDTOConverter.convert(dto, advert, guest);
        booking.setStatus(BookingStatus.ACTIVE);
        bookingRepository.save(booking);
        return ModelDTOConverter.convert(booking);
    }

    @Transactional
    public BookingResponseDTO update(Integer id, BookingRequestDTO dto, User guest) {
        var booking = bookingRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException("Booking #" + id + " not found"));
        if (booking.getGuest().equals(guest) || guest.getRole() == Role.ROLE_ADMIN) {
            booking.setStartDate(dto.getStartDate());
            booking.setEndDate(dto.getEndDate());
            bookingRepository.save(booking);
            return ModelDTOConverter.convert(booking);
        }
        throw new NotAllowedException("Not allowed to update booking #" + id);
    }

    @Transactional
    public void delete(Integer id, User guest) {
        var booking = bookingRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException("Booking #" + id + " not found"));
        if (booking.getGuest().equals(guest) || guest.getRole() == Role.ROLE_ADMIN) {
            bookingRepository.delete(booking);
        }
        throw new NotAllowedException("Not allowed to delete advertisement #" + id);
    }

}
