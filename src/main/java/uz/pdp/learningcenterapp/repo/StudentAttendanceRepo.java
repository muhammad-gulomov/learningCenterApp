package uz.pdp.learningcenterapp.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.pdp.learningcenterapp.entity.StudentAttendance;

import java.util.List;

public interface StudentAttendanceRepo extends JpaRepository<StudentAttendance, Integer> {
    @Query(value = """
            select sa.* from student_attendance sa
            join public.time_table_student tts on tts.id = sa.time_table_student_id
            join public.users u on u.id = tts.student_id
            join public.users_roles ur on u.id = ur.users_id
            where ur.roles_id=2 and u.id= :studentId and sa.lesson_order = :lessonOrder
            """, nativeQuery = true)
    List<StudentAttendance> findAllByTimeTableStudentIdAndLessonOrder(Integer studentId, Integer lessonOrder);
}
