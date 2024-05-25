package uz.pdp.learningcenterapp.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.pdp.learningcenterapp.entity.TimeTable;
import uz.pdp.learningcenterapp.projections.TimeTableProjection;

import java.util.List;

public interface TimeTableRepo extends JpaRepository<TimeTable, Integer> {
    List<TimeTable> findAllByGroupId(Integer groupId);

    @Query(value = """
                select
                     u.id as id, u.first_name || ' ' || u.last_name as fullName, array_agg(sa.attendance order by sa.lesson_order) as attendances
                from time_table_student tts
                    join public.time_table tt on tt.id = tts.time_table_id
                    join public.users u on u.id = tts.student_id
                    join public.users_roles ur on u.id = ur.users_id
                    join public.roles r on r.id = ur.roles_id
                    join public.student_attendance sa on tts.id = sa.time_table_student_id
                where tt.id = :timeTableId and ur.roles_id=2
                group by u.id
            """, nativeQuery = true)
    List<TimeTableProjection> getTimeTableData(Integer timeTableId);

    @Query(value = """
            select sum(tt.price) from time_table tt
                join public.time_table_student tts on tt.id = tts.time_table_id
                join public.users u on u.id = tts.student_id
            where tts.student_id = :studentId
            """, nativeQuery = true)
    Integer getTimeTablePriceByStudentId(Integer studentId);

    @Query(value = """
            select count(tt.price) from time_table tt
                join public.time_table_student tts on tt.id = tts.time_table_id
                join public.users u on u.id = tts.student_id
            where tts.student_id = :studentId
            """, nativeQuery = true)
    Integer getCount(Integer studentId);

}
