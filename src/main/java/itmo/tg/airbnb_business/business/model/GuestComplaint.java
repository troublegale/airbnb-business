package itmo.tg.airbnb_business.business.model;

import itmo.tg.airbnb_business.auth.model.User;
import itmo.tg.airbnb_business.business.model.enums.TicketStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "guest_complaints")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GuestComplaint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @JoinColumn(name = "guest_id", nullable = false)
    @ManyToOne
    private User guest;

    @JoinColumn(name = "advertisement_id", nullable = false)
    @ManyToOne
    private Advertisement advertisement;

    @JoinColumn(name = "booking_id", nullable = false)
    @ManyToOne
    private Booking booking;

    @Column(name = "proof_link", nullable = false)
    private String proofLink;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private TicketStatus status;

    @JoinColumn(name = "resolver_id")
    @ManyToOne
    private User resolver;

}
