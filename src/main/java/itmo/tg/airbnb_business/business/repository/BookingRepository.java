package itmo.tg.airbnb_business.business.repository;

import itmo.tg.airbnb_business.business.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
}
