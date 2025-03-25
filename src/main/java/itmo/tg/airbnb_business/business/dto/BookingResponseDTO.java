package itmo.tg.airbnb_business.business.dto;

import itmo.tg.airbnb_business.business.model.enums.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingResponseDTO {

    private Integer id;

    private Integer advertisementId;

    private String guestUsername;

    private LocalDate startDate;

    private LocalDate endDate;

    private BookingStatus status;

}
