package itmo.tg.airbnb_business.business.repository;

import itmo.tg.airbnb_business.business.model.Advertisement;
import itmo.tg.airbnb_business.business.model.enums.AdvertisementStatus;
import itmo.tg.airbnb_business.security.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface AdvertisementRepository extends JpaRepository<Advertisement, Long> {

    Page<Advertisement> findByHost(User host, Pageable pageable);

    Page<Advertisement> findByStatus(AdvertisementStatus status, Pageable pageable);

    Page<Advertisement> findByHostAndStatus(User host, AdvertisementStatus status, Pageable pageable);

}
