package itmo.tg.airbnb_business.business.repository;

import itmo.tg.airbnb_business.business.model.Advertisement;
import itmo.tg.airbnb_business.business.model.Booking;
import itmo.tg.airbnb_business.business.model.enums.BookingStatus;
import itmo.tg.airbnb_business.security.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface BookingRepository extends JpaRepository<Booking, Long> {

    Boolean existsByAdvertisementAndStatus(Advertisement advertisement, BookingStatus status);

    List<Booking> findByAdvertisementAndStatus(Advertisement advertisement, BookingStatus status);

    Page<Booking> findByAdvertisementAndStatus(Advertisement advertisement, BookingStatus status, Pageable pageable);

    Page<Booking> findByGuest(User guest, Pageable pageable);

    List<Booking> findByStatus(BookingStatus status);

    Page<Booking> findByStatus(BookingStatus status, Pageable pageable);

    Page<Booking> findByGuestAndStatus(User guest, BookingStatus status, Pageable pageable);

    Page<Booking> findByAdvertisement(Advertisement advertisement, Pageable pageable);
}
