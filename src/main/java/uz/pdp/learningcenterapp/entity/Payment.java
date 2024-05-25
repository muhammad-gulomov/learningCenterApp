package uz.pdp.learningcenterapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.engine.internal.Cascade;
import uz.pdp.learningcenterapp.entity.enums.PayType;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity()
@Builder
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne()
    private User student;
    private Integer amount;
    @Enumerated(EnumType.STRING)
    private PayType payType;
    @Temporal(TemporalType.DATE)
    private Date date;
}
