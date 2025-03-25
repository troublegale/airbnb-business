package itmo.tg.airbnb_business.business.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import itmo.tg.airbnb_business.business.model.enums.TicketStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HostDamageComplaintResponseDTO {

    private Integer id;

    private String hostUsername;

    private Integer bookingId;

    private String proofLink;

    private Integer compensationAmount;

    private TicketStatus status;

    private String resolverUsername;

}
