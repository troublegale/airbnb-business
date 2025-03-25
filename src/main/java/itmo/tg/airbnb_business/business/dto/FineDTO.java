package itmo.tg.airbnb_business.business.dto;

import itmo.tg.airbnb_business.business.model.enums.FineStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FineDTO {

    private Integer id;

    private Integer amount;

    private FineStatus status;

}
