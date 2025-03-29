package itmo.tg.airbnb_business.business.service;

import itmo.tg.airbnb_business.business.exception.exceptions.TicketAlreadyPublishedException;
import itmo.tg.airbnb_business.business.model.GuestComplaint;
import itmo.tg.airbnb_business.security.model.User;
import itmo.tg.airbnb_business.business.dto.HostJustificationRequestDTO;
import itmo.tg.airbnb_business.business.dto.HostJustificationResponseDTO;
import itmo.tg.airbnb_business.business.misc.ModelDTOConverter;
import itmo.tg.airbnb_business.business.model.HostJustification;
import itmo.tg.airbnb_business.business.model.enums.TicketStatus;
import itmo.tg.airbnb_business.business.repository.GuestComplaintRepository;
import itmo.tg.airbnb_business.business.repository.HostJustificationRepository;
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
public class HostJustificationService {

    private final HostJustificationRepository hostJustificationRepository;
    private final GuestComplaintRepository guestComplaintRepository;

    public List<HostJustificationResponseDTO> getOwned(User host, Integer page, Integer pageSize, String filter) {
        List<HostJustification> justifications;
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("id"));
        if (filter.equalsIgnoreCase("pending")) {
            justifications = hostJustificationRepository.findByHostAndStatus(host, TicketStatus.PENDING, pageable);
        } else if (filter.equalsIgnoreCase("resolved")) {
            justifications = hostJustificationRepository.findByHost(host, pageable).stream().filter(
                    j -> j.getResolver() != null).toList();
        } else {
            justifications = hostJustificationRepository.findByHost(host, pageable);
        }
        return ModelDTOConverter.toHostJustificationDTOList(justifications);
    }

    @Transactional
    public HostJustificationResponseDTO create(HostJustificationRequestDTO dto, User host) {
        var complaintId = dto.getGuestComplaintId();
        var complaint = guestComplaintRepository.findById(complaintId).orElseThrow(() ->
                new NoSuchElementException("Complaint #" + complaintId + " not found"));
        verifyJustification(complaint, host);
        var justification = HostJustification.builder()
                .host(host)
                .complaint(complaint)
                .proofLink(dto.getProofLink())
                .status(TicketStatus.PENDING)
                .build();
        hostJustificationRepository.save(justification);
        return ModelDTOConverter.convert(justification);
    }

    private void verifyJustification(GuestComplaint complaint, User host) {
        var exists = hostJustificationRepository
                .existsByComplaintAndHostAndStatusNot(complaint, host, TicketStatus.REJECTED);
        if (exists) {
            throw new TicketAlreadyPublishedException(
                    "Your justification on complaint #" + complaint.getId() + " is already approved or is still pending");
        }
    }

}
