package itmo.tg.airbnb_business.business.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import itmo.tg.airbnb_business.business.model.enums.TicketStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GuestComplaintResponseDTO {

    private Long id;

    private String guestUsername;

    private Long bookingId;

    private String proofLink;

    private LocalDate date;

    private TicketStatus status;

    private String resolverUsername;

}
