package uz.pdp.learningcenterapp.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.pdp.learningcenterapp.entity.Payment;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface PaymentRepo extends JpaRepository<Payment, UUID> {
    @Query("SELECT p FROM Payment p WHERE p.date >= :startDate AND p.date <= :endDate")
    List<Payment> findPaymentsByDateRange(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    void deleteByStudentId(Integer id);

    @Query(value = "select sum(amount) as paymentsStudent from payment p where p.student_id = :studentId", nativeQuery = true)
    Integer findSumPaymentsByStudentId(Integer studentId);
}
