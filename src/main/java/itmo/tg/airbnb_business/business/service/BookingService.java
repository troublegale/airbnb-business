package itmo.tg.airbnb_business.business.service;

import itmo.tg.airbnb_business.auth.model.User;
import itmo.tg.airbnb_business.business.dto.BookingRequestDTO;
import itmo.tg.airbnb_business.business.dto.BookingResponseDTO;
import itmo.tg.airbnb_business.business.exception.exceptions.*;
import itmo.tg.airbnb_business.business.misc.ModelDTOConverter;
import itmo.tg.airbnb_business.business.model.Booking;
import itmo.tg.airbnb_business.business.model.enums.AdvertisementStatus;
import itmo.tg.airbnb_business.business.model.enums.BookingStatus;
import itmo.tg.airbnb_business.business.repository.AdvertisementBlockRepository;
import itmo.tg.airbnb_business.business.repository.AdvertisementRepository;
import itmo.tg.airbnb_business.business.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final AdvertisementRepository advertisementRepository;
    private final AdvertisementBlockRepository advertisementBlockRepository;
    private final BookingRepository bookingRepository;

    private final PenaltyService penaltyService;

    public BookingResponseDTO get(Long id) {
        var booking = bookingRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException("Booking #" + id + " not found"));
        return ModelDTOConverter.convert(booking);
    }

    public List<BookingResponseDTO> getAll() {
        var bookings = bookingRepository.findAll();
        return ModelDTOConverter.toBookingDTOList(bookings);
    }

    public List<BookingResponseDTO> getOwned(User guest, Boolean showAll) {
        List<Booking> bookings;
        if (showAll) {
            bookings = bookingRepository.findByGuest(guest);
        } else {
            bookings = bookingRepository.findByGuestAndStatus(guest, BookingStatus.ACTIVE);
        }
        return ModelDTOConverter.toBookingDTOList(bookings);
    }

    @Transactional
    public BookingResponseDTO create(BookingRequestDTO dto, User guest) {

        var advertId = dto.getAdvertisementId();
        var advert = advertisementRepository.findById(advertId).orElseThrow(() ->
                new NoSuchElementException("Advertisement #" + advertId + " not found"));

        if (advert.getHost().equals(guest)) {
            throw new BookOwnAdvertisementException("You can't book your own advertisement");
        }

        if (advert.getStatus() == AdvertisementStatus.BLOCKED) {
            var block = advertisementBlockRepository.findByAdvertisement(advert);
            var until = block.getDateUntil();
            throw new AdvertisementBlockedException("Advertisement #" + advertId + " is blocked until " + until);
        }

        verifyDates(dto.getStartDate(), dto.getEndDate());

        var activeBookings = bookingRepository.findByAdvertisementAndStatus(advert, BookingStatus.ACTIVE);
        activeBookings.forEach(booking ->
                verifyDatesConflict(booking.getStartDate(), booking.getEndDate(), dto.getStartDate(), dto.getEndDate())
        );

        var booking = ModelDTOConverter.convert(dto, advert, guest);
        booking.setStatus(BookingStatus.ACTIVE);
        bookingRepository.save(booking);
        return ModelDTOConverter.convert(booking);

    }

    @Transactional
    public String cancel(Long id, User user) {

        var booking = bookingRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException("Booking #" + id + " not found"));

        if (!booking.getGuest().equals(user) && !booking.getAdvertisement().getHost().equals(user)) {
            throw new NotAllowedException("Not allowed to cancel booking #" + id);
        }

        if (booking.getStatus() != BookingStatus.ACTIVE) {
            throw new NotAllowedException("Booking #" + id + " is already cancelled or expired");
        }

        if (booking.getGuest().equals(user)) {
            booking.setStatus(BookingStatus.CANCELLED);
            bookingRepository.save(booking);
            return "You cancelled booking #" + id + " as a guest";
        }

        var advert = booking.getAdvertisement();
        penaltyService.blockAndAssignFine(
                advert, booking.getStartDate(), booking.getEndDate(), user);
        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);
        return "You cancelled booking #" + id + " as a host.\n" +
                "You were assigned with a fine. Refer to /fines/my\n" +
                "Your advertisement #" + advert.getId() + " is blocked for booking until " + booking.getEndDate();

    }

    private void verifyDates(LocalDate start, LocalDate end) {
        boolean cond1 = (start.isAfter(LocalDate.now()) || start.isEqual(LocalDate.now()))
                && end.isAfter(LocalDate.now());
        boolean cond2 = start.isBefore(end) || end.isEqual(start);
        if (!cond1 || !cond2) {
            throw new InvalidBookingDatesException("Booking dates are incorrect");
        }
    }

    private void verifyDatesConflict(LocalDate start1, LocalDate end1, LocalDate start2, LocalDate end2) {
        boolean cond1 = start2.isAfter(end1);
        boolean cond2 = start1.isAfter(end2);
        if (!cond1 && !cond2) {
            throw new BookingDatesConflictException("Another booking already exists in the same period");
        }
    }

}
