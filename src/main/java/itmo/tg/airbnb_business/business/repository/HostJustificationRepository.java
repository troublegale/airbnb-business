package itmo.tg.airbnb_business.business.repository;

import itmo.tg.airbnb_business.business.model.GuestComplaint;
import itmo.tg.airbnb_business.business.model.HostJustification;
import itmo.tg.airbnb_business.business.model.enums.TicketStatus;
import itmo.tg.airbnb_business.security.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HostJustificationRepository extends JpaRepository<HostJustification, Long> {

    Page<HostJustification> findByStatus(TicketStatus status, Pageable pageable);

    Page<HostJustification> findByHost(User host, Pageable pageable);

    Page<HostJustification> findByHostAndStatus(User host, TicketStatus status, Pageable pageable);

    Boolean existsByComplaintAndHostAndStatusNot(GuestComplaint complaint, User host, TicketStatus status);

}
