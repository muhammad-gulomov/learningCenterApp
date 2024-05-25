package uz.pdp.learningcenterapp.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.learningcenterapp.entity.Group;

public interface GroupRepo extends JpaRepository<Group, Integer> {
}
