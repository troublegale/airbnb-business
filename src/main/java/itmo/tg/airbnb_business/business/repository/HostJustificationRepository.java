package itmo.tg.airbnb_business.business.repository;

import itmo.tg.airbnb_business.auth.model.User;
import itmo.tg.airbnb_business.business.model.HostJustification;
import itmo.tg.airbnb_business.business.model.enums.TicketStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HostJustificationRepository extends JpaRepository<HostJustification, Long> {

    List<HostJustification> findByHost(User host, Pageable pageable);

    List<HostJustification> findByHostAndStatus(User host, TicketStatus status, Pageable pageable);

}
