package itmo.tg.airbnb_business.business.repository;

import itmo.tg.airbnb_business.auth.model.User;
import itmo.tg.airbnb_business.business.model.Advertisement;
import itmo.tg.airbnb_business.business.model.enums.AdvertisementStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface AdvertisementRepository extends JpaRepository<Advertisement, Integer> {

    List<Advertisement> findByHost(User host);

    List<Advertisement> findByHostAndStatus(User host, AdvertisementStatus status);
}
