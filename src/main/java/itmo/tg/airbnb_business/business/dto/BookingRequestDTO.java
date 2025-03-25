package itmo.tg.airbnb_business.business.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingRequestDTO {

    @NotNull
    private Integer advertisementId;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

}
