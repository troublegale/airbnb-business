package itmo.tg.airbnb_business.business.repository;

import itmo.tg.airbnb_business.auth.model.User;
import itmo.tg.airbnb_business.business.model.Fine;
import itmo.tg.airbnb_business.business.model.enums.FineStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FineRepository extends JpaRepository<Fine, Long> {

    List<Fine> findByUser(User user, Pageable pageable);

    List<Fine> findByStatus(FineStatus status, Pageable pageable);

    List<Fine> findByUserAndStatus(User user, FineStatus status, Pageable pageable);

}
