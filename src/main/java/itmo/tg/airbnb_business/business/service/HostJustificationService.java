package itmo.tg.airbnb_business.business.service;

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

    public HostJustificationResponseDTO create(HostJustificationRequestDTO dto, User host) {
        var complaintId = dto.getGuestComplaintId();
        var complaint = guestComplaintRepository.findById(complaintId).orElseThrow(() ->
                new NoSuchElementException("Complaint #" + complaintId + " not found"));
        var justification = HostJustification.builder()
                .host(host)
                .complaint(complaint)
                .proofLink(dto.getProofLink())
                .status(TicketStatus.PENDING)
                .build();
        hostJustificationRepository.save(justification);
        return ModelDTOConverter.convert(justification);
    }

}
