package itmo.tg.airbnb_business.business.service;

import itmo.tg.airbnb_business.business.dto.HostDamageComplaintRequestDTO;
import itmo.tg.airbnb_business.business.dto.HostDamageComplaintResponseDTO;
import itmo.tg.airbnb_business.business.exception.exceptions.TicketAlreadyPublishedException;
import itmo.tg.airbnb_business.business.misc.ModelDTOConverter;
import itmo.tg.airbnb_business.business.model.Booking;
import itmo.tg.airbnb_business.business.model.HostDamageComplaint;
import itmo.tg.airbnb_business.business.model.enums.TicketStatus;
import itmo.tg.airbnb_business.business.repository.BookingRepository;
import itmo.tg.airbnb_business.business.repository.HostDamageComplaintRepository;
import itmo.tg.airbnb_business.security.model.User;
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
public class HostDamageComplaintService {

    private final HostDamageComplaintRepository hostDamageComplaintRepository;
    private final BookingRepository bookingRepository;

    public List<HostDamageComplaintResponseDTO> get(Integer page, Integer pageSize, String filter) {
        List<HostDamageComplaint> complaints;
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("id"));
        if (filter.equalsIgnoreCase("pending")) {
            complaints = hostDamageComplaintRepository.findByStatus(TicketStatus.PENDING, pageable).getContent();
        } else if (filter.equalsIgnoreCase("resolved")) {
            complaints = hostDamageComplaintRepository.findAll(pageable).stream().filter(
                    c -> c.getResolver() != null).toList();
        } else {
            complaints = hostDamageComplaintRepository.findAll(pageable).getContent();
        }
        return ModelDTOConverter.toHostDamageComplaintDTOList(complaints);
    }

    public List<HostDamageComplaintResponseDTO> getOwned(User host, Integer page, Integer pageSize, String filter) {
        List<HostDamageComplaint> complaints;
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("id"));
        if (filter.equalsIgnoreCase("pending")) {
            complaints = hostDamageComplaintRepository.findByHostAndStatus(host, TicketStatus.PENDING, pageable).getContent();
        } else if (filter.equalsIgnoreCase("resolved")) {
            complaints = hostDamageComplaintRepository.findByHost(host, pageable).stream().filter(
                    c -> c.getResolver() != null).toList();
        } else {
            complaints = hostDamageComplaintRepository.findByHost(host, pageable).getContent();
        }
        return ModelDTOConverter.toHostDamageComplaintDTOList(complaints);
    }

    @Transactional
    public HostDamageComplaintResponseDTO create(HostDamageComplaintRequestDTO dto, User host) {
        var bookingId = dto.getBookingId();
        var booking = bookingRepository.findById(bookingId).orElseThrow(() ->
                new NoSuchElementException("Booking #" + bookingId + " not found"));
        verifyComplaint(booking, host);
        var complaint = HostDamageComplaint.builder()
                .host(host)
                .booking(booking)
                .proofLink(dto.getProofLink())
                .compensationAmount(dto.getCompensationAmount())
                .status(TicketStatus.PENDING)
                .build();
        hostDamageComplaintRepository.save(complaint);
        return ModelDTOConverter.convert(complaint);
    }

    private void verifyComplaint(Booking booking, User host) {
        var exists = hostDamageComplaintRepository
                .existsByBookingAndHostAndStatusNot(booking, host, TicketStatus.REJECTED);
        if (exists) {
            throw new TicketAlreadyPublishedException(
                    "Your complaint on booking #" + booking.getId() + " is already approved or is still pending");
        }
    }

    @Transactional
    public HostDamageComplaintResponseDTO approve(Long id, User resolver) {

    }

    @Transactional
    public HostDamageComplaintResponseDTO reject(Long id, User resolver) {

    }

}
