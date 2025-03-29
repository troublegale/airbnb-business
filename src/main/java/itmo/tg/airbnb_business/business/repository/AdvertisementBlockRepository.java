package itmo.tg.airbnb_business.business.repository;

import itmo.tg.airbnb_business.business.model.Advertisement;
import itmo.tg.airbnb_business.business.model.AdvertisementBlock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface AdvertisementBlockRepository extends JpaRepository<AdvertisementBlock, Long> {

    List<AdvertisementBlock> findByAdvertisement(Advertisement advertisement);

}
