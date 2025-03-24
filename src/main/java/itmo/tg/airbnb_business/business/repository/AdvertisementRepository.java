package itmo.tg.airbnb_business.business.repository;

import itmo.tg.airbnb_business.business.model.Advertisement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdvertisementRepository extends JpaRepository<Advertisement, Integer> {
}
