package uz.pdp.learningcenterapp.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.learningcenterapp.entity.Role;
import uz.pdp.learningcenterapp.entity.enums.RoleType;

import java.util.List;

public interface RoleRepo extends JpaRepository<Role, Integer> {
    Role findAllByRoleType(RoleType role);
}
