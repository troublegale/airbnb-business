package itmo.tg.airbnb_business.business.service;

import itmo.tg.airbnb_business.business.model.Advertisement;
import itmo.tg.airbnb_business.business.model.AdvertisementBlock;
import itmo.tg.airbnb_business.business.model.Fine;
import itmo.tg.airbnb_business.business.model.enums.AdvertisementStatus;
import itmo.tg.airbnb_business.business.model.enums.FineReason;
import itmo.tg.airbnb_business.business.model.enums.FineStatus;
import itmo.tg.airbnb_business.business.repository.AdvertisementBlockRepository;
import itmo.tg.airbnb_business.business.repository.AdvertisementRepository;
import itmo.tg.airbnb_business.business.repository.FineRepository;
import itmo.tg.airbnb_business.security.model.User;
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
    public void blockAndAssignFine(Advertisement advertisement, Long ticketId, FineReason fineReason,
                                   LocalDate assigningDate, LocalDate startDate, LocalDate endDate, User host) {
        var block = AdvertisementBlock.builder()
                .advertisement(advertisement)
                .dateUntil(endDate)
                .build();
        advertisementBlockRepository.save(block);
        if (advertisement.getStatus() != AdvertisementStatus.BLOCKED) {
            advertisement.setStatus(AdvertisementStatus.BLOCKED);
            advertisementRepository.save(advertisement);
        }

        var amount = calculateFineAmount(
                assigningDate, startDate, endDate, advertisement.getBookPrice(), advertisement.getPricePerNight());
        assignFine(amount, host, ticketId, fineReason);
    }

    @Transactional
    public void assignFine(Double amount, User user, Long ticketId, FineReason fineReason) {
        var fine = Fine.builder()
                .user(user)
                .amount(amount)
                .status(FineStatus.ACTIVE)
                .ticketId(ticketId)
                .fineReason(fineReason)
                .build();
        fineRepository.save(fine);
    }

    @Transactional
    public void retractPenalty(Advertisement advertisement, LocalDate until, Long ticketId, FineReason fineReason) {
        var blocks = advertisementBlockRepository.findByAdvertisement(advertisement);
        var exactBlock = blocks.stream().filter(b -> b.getDateUntil().equals(until)).toList();
        var block = exactBlock.get(0);
        advertisementBlockRepository.delete(block);
        if (blocks.size() == 1) {
            advertisement.setStatus(AdvertisementStatus.ACTIVE);
            advertisementRepository.save(advertisement);
        }

        var fine = fineRepository.findByTicketIdAndFineReason(ticketId, fineReason);
        fine.setStatus(FineStatus.CANCELLED);
        fineRepository.save(fine);
    }

    private double calculateFineAmount(
            LocalDate assigningDate, LocalDate startDate, LocalDate endDate, Integer bookPrice, Integer pricePerNight) {
        double amount;
        long nights;
        if (assigningDate.isAfter(startDate)) {
            nights = ChronoUnit.DAYS.between(assigningDate, endDate) + 1;
            amount = nights * pricePerNight / 2.0;
        } else if (ChronoUnit.DAYS.between(assigningDate, startDate) <= 2) {
            nights = ChronoUnit.DAYS.between(startDate, endDate) + 1;
            amount = nights * pricePerNight / 2.0;
        } else if (ChronoUnit.DAYS.between(assigningDate, startDate) <= 30) {
            amount = bookPrice / 4.0;
        } else {
            amount = bookPrice / 10.0;
        }
        if (amount < 50) {
            amount = 50;
        }
        return amount;
    }

}
