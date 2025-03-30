package itmo.tg.airbnb_business.business.service;

import itmo.tg.airbnb_business.business.dto.GuestComplaintRequestDTO;
import itmo.tg.airbnb_business.business.dto.GuestComplaintResponseDTO;
import itmo.tg.airbnb_business.business.exception.exceptions.BookingAlreadyExpiredException;
import itmo.tg.airbnb_business.business.exception.exceptions.NotAllowedException;
import itmo.tg.airbnb_business.business.exception.exceptions.TicketAlreadyPublishedException;
import itmo.tg.airbnb_business.business.exception.exceptions.TicketAlreadyResolvedException;
import itmo.tg.airbnb_business.business.misc.ModelDTOConverter;
import itmo.tg.airbnb_business.business.model.Booking;
import itmo.tg.airbnb_business.business.model.GuestComplaint;
import itmo.tg.airbnb_business.business.model.enums.BookingStatus;
import itmo.tg.airbnb_business.business.model.enums.FineReason;
import itmo.tg.airbnb_business.business.model.enums.TicketStatus;
import itmo.tg.airbnb_business.business.repository.AdvertisementRepository;
import itmo.tg.airbnb_business.business.repository.BookingRepository;
import itmo.tg.airbnb_business.business.repository.GuestComplaintRepository;
import itmo.tg.airbnb_business.security.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class GuestComplaintService {

    private final AdvertisementRepository advertisementRepository;
    private final GuestComplaintRepository guestComplaintRepository;
    private final BookingRepository bookingRepository;
    private final PenaltyService penaltyService;

    public List<GuestComplaintResponseDTO> get(Integer page, Integer pageSize, String filter) {
        List<GuestComplaint> complaints;
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("id"));
        if (filter.equalsIgnoreCase("pending")) {
            complaints = guestComplaintRepository.findByStatus(TicketStatus.PENDING, pageable).getContent();
        } else if (filter.equalsIgnoreCase("resolved")) {
            complaints = guestComplaintRepository.findAll(pageable).stream().filter(
                    c -> c.getResolver() != null).toList();
        } else {
            complaints = guestComplaintRepository.findAll(pageable).getContent();
        }
        return ModelDTOConverter.toGuestComplaintDTOList(complaints);
    }

    public List<GuestComplaintResponseDTO> getOwned(User guest, Integer page, Integer pageSize, String filter) {
        List<GuestComplaint> complaints;
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("id"));
        if (filter.equalsIgnoreCase("pending")) {
            complaints = guestComplaintRepository.findByGuestAndStatus(guest, TicketStatus.PENDING, pageable).getContent();
        } else if (filter.equalsIgnoreCase("resolved")) {
            complaints = guestComplaintRepository.findByGuest(guest, pageable).stream().filter(
                    c -> c.getResolver() != null).toList();
        } else {
            complaints = guestComplaintRepository.findByGuest(guest, pageable).getContent();
        }
        return ModelDTOConverter.toGuestComplaintDTOList(complaints);
    }

    public List<GuestComplaintResponseDTO> getForAdvertisement(
            Long advertId, Integer page, Integer pageSize, Boolean approved, User host) {
        var advert = advertisementRepository.findById(advertId).orElseThrow(() ->
                new NoSuchElementException("Advertisement #" + advertId + " not found"));
        if (!advert.getHost().equals(host)) {
            throw new NotAllowedException("You do not own advertisement " + advertId);
        }
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by("id"));
        List<GuestComplaint> complaints;
        if (approved) {
            complaints = guestComplaintRepository.findByAdvertisementAndStatus(advert, TicketStatus.APPROVED, pageable).getContent();
        } else {
            complaints = guestComplaintRepository.findByAdvertisement(advert, pageable).getContent();
        }
        return ModelDTOConverter.toGuestComplaintDTOList(complaints);
    }

    @Transactional
    public GuestComplaintResponseDTO create(GuestComplaintRequestDTO dto, User guest) {
        var bookingId = dto.getBookingId();
        var booking = bookingRepository.findById(bookingId).orElseThrow(() ->
                new NoSuchElementException("Booking #" + bookingId + " not found"));
        if (booking.getStatus() == BookingStatus.EXPIRED) {
            throw new BookingAlreadyExpiredException("Booking #" + bookingId + " is already expired");
        }
        verifyComplaint(booking, guest);
        var complaint = GuestComplaint.builder()
                .guest(guest)
                .advertisement(booking.getAdvertisement())
                .booking(booking)
                .proofLink(dto.getProofLink())
                .date(LocalDate.now())
                .status(TicketStatus.PENDING)
                .build();
        guestComplaintRepository.save(complaint);
        return ModelDTOConverter.convert(complaint);
    }

    private void verifyComplaint(Booking booking, User guest) {
        var exists = guestComplaintRepository
                .existsByBookingAndGuestAndStatusNot(booking, guest, TicketStatus.REJECTED);
        if (exists) {
            throw new TicketAlreadyPublishedException(
                    "Your complaint on booking #" + booking.getId() + " is already approved or is still pending");
        }
    }

    @Transactional
    public GuestComplaintResponseDTO approve(Long id, User resolver) {
        var ticket = guestComplaintRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException("Guest complaint #" + id + " not found"));
        if (ticket.getStatus() != TicketStatus.PENDING) {
            throw new TicketAlreadyResolvedException("Guest complaint #" + id + " is already resolved");
        }
        ticket.setStatus(TicketStatus.APPROVED);
        ticket.setResolver(resolver);
        guestComplaintRepository.save(ticket);
        var booking = ticket.getBooking();
        var advert = booking.getAdvertisement();
        var assigningDate = ticket.getDate();
        penaltyService.blockAndAssignFine(advert, ticket.getId(), FineReason.GUEST,
                assigningDate, booking.getStartDate(), booking.getEndDate(), advert.getHost());
        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);
        return ModelDTOConverter.convert(ticket);
    }

    @Transactional
    public GuestComplaintResponseDTO reject(Long id, User resolver) {
        var ticket = guestComplaintRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException("Guest complaint #" + id + " not found"));
        if (ticket.getStatus() != TicketStatus.PENDING) {
            throw new TicketAlreadyResolvedException("Guest complaint #" + id + " is already resolved");
        }
        ticket.setStatus(TicketStatus.REJECTED);
        ticket.setResolver(resolver);
        guestComplaintRepository.save(ticket);
        return ModelDTOConverter.convert(ticket);
    }

}
