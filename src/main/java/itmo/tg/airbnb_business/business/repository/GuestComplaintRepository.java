package itmo.tg.airbnb_business.business.repository;

import itmo.tg.airbnb_business.business.model.Advertisement;
import itmo.tg.airbnb_business.business.model.Booking;
import itmo.tg.airbnb_business.business.model.GuestComplaint;
import itmo.tg.airbnb_business.business.model.enums.TicketStatus;
import itmo.tg.airbnb_business.security.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuestComplaintRepository extends JpaRepository<GuestComplaint, Long> {

    Page<GuestComplaint> findByStatus(TicketStatus status, Pageable pageable);

    Page<GuestComplaint> findByGuest(User guest, Pageable pageable);

    Page<GuestComplaint> findByGuestAndStatus(User guest, TicketStatus status, Pageable pageable);

    Boolean existsByBookingAndGuestAndStatusNot(Booking booking, User guest, TicketStatus status);

    Page<GuestComplaint> findByAdvertisement(Advertisement advertisement, Pageable pageable);

    Page<GuestComplaint> findByAdvertisementAndStatus(Advertisement advertisement, TicketStatus status, Pageable pageable);

}
