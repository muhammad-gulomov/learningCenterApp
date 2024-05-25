package uz.pdp.learningcenterapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import uz.pdp.learningcenterapp.entity.enums.RoleType;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "roles")
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    @Override
    public String getAuthority() {
        return roleType.toString();
    }
}
