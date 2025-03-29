package itmo.tg.airbnb_business.business.model;

import itmo.tg.airbnb_business.security.model.User;
import itmo.tg.airbnb_business.business.model.enums.AdvertisementStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "advertisements")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Advertisement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "rooms", nullable = false)
    private int rooms;

    @Column(name = "book_price", nullable = false)
    private int bookPrice;

    @Column(name = "price_per_night", nullable = false)
    private int pricePerNight;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private AdvertisementStatus status;

    @JoinColumn(name = "host_id", nullable = false)
    @ManyToOne
    private User host;

}
