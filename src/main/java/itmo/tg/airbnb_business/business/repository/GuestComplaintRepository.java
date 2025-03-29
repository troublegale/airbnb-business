package itmo.tg.airbnb_business.business.repository;

import itmo.tg.airbnb_business.security.model.User;
import itmo.tg.airbnb_business.business.model.GuestComplaint;
import itmo.tg.airbnb_business.business.model.enums.TicketStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GuestComplaintRepository extends JpaRepository<GuestComplaint, Long> {

    List<GuestComplaint> findByStatus(TicketStatus status, Pageable pageable);

    List<GuestComplaint> findByGuest(User guest, Pageable pageable);

    List<GuestComplaint> findByGuestAndStatus(User guest, TicketStatus status, Pageable pageable);

}
