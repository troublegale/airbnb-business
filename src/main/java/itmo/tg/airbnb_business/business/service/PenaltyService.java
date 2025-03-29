package itmo.tg.airbnb_business.business.service;

import itmo.tg.airbnb_business.security.model.User;
import itmo.tg.airbnb_business.business.model.Advertisement;
import itmo.tg.airbnb_business.business.model.AdvertisementBlock;
import itmo.tg.airbnb_business.business.model.Fine;
import itmo.tg.airbnb_business.business.model.enums.AdvertisementStatus;
import itmo.tg.airbnb_business.business.model.enums.FineStatus;
import itmo.tg.airbnb_business.business.repository.AdvertisementBlockRepository;
import itmo.tg.airbnb_business.business.repository.AdvertisementRepository;
import itmo.tg.airbnb_business.business.repository.FineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class PenaltyService {

    private final FineRepository fineRepository;
    private final AdvertisementRepository advertisementRepository;
    private final AdvertisementBlockRepository advertisementBlockRepository;

    @Transactional
    public void blockAndAssignFine(Advertisement advertisement, LocalDate startDate, LocalDate endDate, User host) {

        var existingBlockOpt = advertisementBlockRepository.findByAdvertisement(advertisement);
        if (existingBlockOpt.isPresent()) {
            var existingBlock = existingBlockOpt.get();
            if (existingBlock.getDateUntil().isBefore(endDate)) {
                existingBlock.setDateUntil(endDate);
                advertisementBlockRepository.save(existingBlock);
            }
        } else {
            var block = AdvertisementBlock.builder()
                    .advertisement(advertisement)
                    .dateUntil(endDate)
                    .build();
            advertisementBlockRepository.save(block);
            advertisement.setStatus(AdvertisementStatus.BLOCKED);
            advertisementRepository.save(advertisement);
        }

        var amount = calculateFineAmount(
                startDate, endDate, advertisement.getBookPrice(), advertisement.getPricePerNight());
        var fine = Fine.builder()
                .user(host)
                .amount(amount)
                .status(FineStatus.ACTIVE)
                .build();
        fineRepository.save(fine);

    }

    @Transactional
    public void assignFine(Double amount, User user) {
        var fine = Fine.builder()
                .user(user)
                .amount(amount)
                .status(FineStatus.ACTIVE)
                .build();
        fineRepository.save(fine);
    }

    private double calculateFineAmount(
            LocalDate startDate, LocalDate endDate, Integer bookPrice, Integer pricePerNight) {
        var now = LocalDate.now();
        double amount;
        if (ChronoUnit.DAYS.between(now, startDate) <= 2 || startDate.isAfter(now)) {
            var nights = (int) ChronoUnit.DAYS.between(now, endDate) + 1;
            amount = pricePerNight * nights / 2.0;
        } else if (ChronoUnit.DAYS.between(now, startDate) <= 30) {
            amount = bookPrice / 4.0;
        } else {
            amount = bookPrice / 10.0;
        }
        if (amount < 50) amount = 50;
        return amount;
    }

}
