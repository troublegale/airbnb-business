package itmo.tg.airbnb_business.business.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdvertisementBlockDTO {

    private Integer advertisementId;

    private LocalDate dateUntil;

}
