package itmo.tg.airbnb_business.business.service;

import itmo.tg.airbnb_business.security.model.User;
import itmo.tg.airbnb_business.business.dto.GuestComplaintRequestDTO;
import itmo.tg.airbnb_business.business.dto.GuestComplaintResponseDTO;
import itmo.tg.airbnb_business.business.misc.ModelDTOConverter;
import itmo.tg.airbnb_business.business.model.GuestComplaint;
import itmo.tg.airbnb_business.business.model.enums.TicketStatus;
import itmo.tg.airbnb_business.business.repository.AdvertisementRepository;
import itmo.tg.airbnb_business.business.repository.BookingRepository;
import itmo.tg.airbnb_business.business.repository.GuestComplaintRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class GuestComplaintService {

    private final GuestComplaintRepository guestComplaintRepository;
    private final AdvertisementRepository advertisementRepository;
    private final BookingRepository bookingRepository;

    public List<GuestComplaintResponseDTO> getOwned(User guest, Integer page, Integer pageSize, String filter) {
        List<GuestComplaint> complaints;
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("id"));
        if (filter.equalsIgnoreCase("pending")) {
            complaints = guestComplaintRepository.findByGuestAndStatus(guest, TicketStatus.PENDING, pageable);
        } else if (filter.equalsIgnoreCase("resolved")) {
            complaints = guestComplaintRepository.findByGuest(guest, pageable).stream().filter(
                    c -> c.getResolver() != null).toList();
        } else {
            complaints = guestComplaintRepository.findByGuest(guest, pageable);
        }
        return ModelDTOConverter.toGuestComplaintDTOList(complaints);
    }

    public GuestComplaintResponseDTO create(GuestComplaintRequestDTO dto, User guest) {
        var advertId = dto.getAdvertisementId();
        var advert = advertisementRepository.findById(advertId).orElseThrow(() ->
                new NoSuchElementException("Advertisement #" + advertId + " not found"));
        var bookingId = dto.getBookingId();
        var booking = bookingRepository.findById(bookingId).orElseThrow(() ->
                new NoSuchElementException("Booking #" + bookingId + " not found"));
        var complaint = GuestComplaint.builder()
                .guest(guest)
                .advertisement(advert)
                .booking(booking)
                .proofLink(dto.getProofLink())
                .date(LocalDate.now())
                .status(TicketStatus.PENDING)
                .build();
        guestComplaintRepository.save(complaint);
        return ModelDTOConverter.convert(complaint);
    }

}
