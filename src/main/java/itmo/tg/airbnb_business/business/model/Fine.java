package itmo.tg.airbnb_business.business.model;

import itmo.tg.airbnb_business.auth.model.User;
import itmo.tg.airbnb_business.business.model.enums.FineStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "fines")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Fine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne
    private User user;

    @Column(name = "amount", nullable = false)
    private int amount;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private FineStatus status;

}
