package itmo.tg.airbnb_business.business.model;

import itmo.tg.airbnb_business.security.model.User;
import itmo.tg.airbnb_business.business.model.enums.BookingStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "bookings")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @JoinColumn(name = "advertisement_id", nullable = false)
    @ManyToOne
    private Advertisement advertisement;

    @JoinColumn(name = "guest_id", nullable = false)
    @ManyToOne
    private User guest;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private BookingStatus status;

}
