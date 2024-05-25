package uz.pdp.learningcenterapp.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.learningcenterapp.entity.TimeTable;
import uz.pdp.learningcenterapp.entity.TimeTableStudent;
import uz.pdp.learningcenterapp.entity.User;

import java.util.List;

public interface TimeTableStudentRepo extends JpaRepository<TimeTableStudent, Integer> {
    List<TimeTable> findAllByStudentId(Integer studentId);
}
