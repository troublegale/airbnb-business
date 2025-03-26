package itmo.tg.airbnb_business.business.service;

import itmo.tg.airbnb_business.auth.model.User;
import itmo.tg.airbnb_business.business.dto.FineDTO;
import itmo.tg.airbnb_business.business.misc.ModelDTOConverter;
import itmo.tg.airbnb_business.business.model.Fine;
import itmo.tg.airbnb_business.business.model.enums.FineStatus;
import itmo.tg.airbnb_business.business.repository.FineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class FineService {

    private final FineRepository fineRepository;

    public FineDTO get(Integer id) {
        var fine = fineRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException("Fine #" + id + " not found"));
        return ModelDTOConverter.convert(fine);
    }

    public List<FineDTO> getAll() {
        var fines = fineRepository.findAll();
        return ModelDTOConverter.toFineDTOList(fines);
    }

    public List<FineDTO> getAssignedTo(User user, Boolean active) {
        List<Fine> fines;
        if (active) {
            fines = fineRepository.findByUserAndStatus(user, FineStatus.ACTIVE);
        } else {
            fines = fineRepository.findByUser(user);
        }
        return ModelDTOConverter.toFineDTOList(fines);
    }

}
