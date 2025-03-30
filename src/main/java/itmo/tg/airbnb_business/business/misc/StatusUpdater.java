package itmo.tg.airbnb_business.business.misc;

import itmo.tg.airbnb_business.business.model.enums.AdvertisementStatus;
import itmo.tg.airbnb_business.business.model.enums.BookingStatus;
import itmo.tg.airbnb_business.business.repository.AdvertisementBlockRepository;
import itmo.tg.airbnb_business.business.repository.AdvertisementRepository;
import itmo.tg.airbnb_business.business.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class StatusUpdater {

    private final AdvertisementRepository advertisementRepository;
    private final AdvertisementBlockRepository advertisementBlockRepository;
    private final BookingRepository bookingRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void startStatusMonitoring() {
        var monitorThread = new Thread(this::doMonitoring);
        monitorThread.start();
    }

    private void doMonitoring() {
        while (true) {
            doStatusUpdate();
            try {
                Thread.sleep(1000 * 60 * 60);
            } catch (InterruptedException ignored) {
            }
        }
    }

    private void doStatusUpdate() {

        var blocks = advertisementBlockRepository.findAll();
        for (var block : blocks) {
            if (LocalDate.now().isAfter(block.getDateUntil())) {
                var advert = block.getAdvertisement();
                advert.setStatus(AdvertisementStatus.ACTIVE);
                advertisementRepository.save(advert);
                advertisementBlockRepository.delete(block);
            }
        }

        var bookings = bookingRepository.findByStatus(BookingStatus.ACTIVE);
        for (var booking : bookings) {
            if (LocalDate.now().isAfter(booking.getEndDate())) {
                booking.setStatus(BookingStatus.EXPIRED);
                bookingRepository.save(booking);
            }
        }

    }

}
