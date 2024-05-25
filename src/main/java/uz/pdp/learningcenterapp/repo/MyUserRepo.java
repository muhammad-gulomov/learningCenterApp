package uz.pdp.learningcenterapp.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.pdp.learningcenterapp.entity.Role;
import uz.pdp.learningcenterapp.entity.User;

import java.util.List;

public interface MyUserRepo extends JpaRepository<User, Integer> {
    User findByFirstName(String firstName);
    User findByPhoneNumber(String phone);
    @Query(value = """
            select * from users u join public.users_roles ur on u.id = ur.users_id
            where (LOWER(u.first_name) LIKE LOWER(CONCAT('%', (?), '%')) OR LOWER(u.last_name) LIKE LOWER(CONCAT('%', (?), '%')))
            and ur.roles_id=2
            """, nativeQuery = true)
    List<User> findAllByFirstNameContainingOrLastNameContaining(String param, String param1);
    @Query(value = "select * from users u join public.users_roles ur on u.id=ur.users_id where ur.roles_id=2", nativeQuery = true)
    List<User> findByRoles(Role role);

    @Query(value = "select * from users u join public.users_roles ur on u.id=ur.users_id where ur.roles_id=2", nativeQuery = true)
    List<User> findRole();


}
