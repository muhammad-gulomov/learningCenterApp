package uz.pdp.learningcenterapp.component;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import uz.pdp.learningcenterapp.entity.*;
import uz.pdp.learningcenterapp.entity.enums.PayType;
import uz.pdp.learningcenterapp.entity.enums.RoleType;
import uz.pdp.learningcenterapp.repo.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Runner implements CommandLineRunner {
    private final EntityManager entityManager;
    private final MyUserRepo myUserRepo;
    private final RoleRepo roleRepo;
    private final PaymentRepo paymentRepo;
    private final GroupRepo groupRepo;
    private final TimeTableRepo timeTableRepo;
    private final TimeTableStudentRepo timeTableStudentRepo;
    private final StudentAttendanceRepo studentAttendanceRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public void run(String... args) throws Exception {

        List<User> all = myUserRepo.findAll();
        List<User> students = new ArrayList<>();
        List<TimeTableStudent> timeTableStudents = new ArrayList<>();



        if(all.isEmpty()){
            Role role1 = Role.builder()
                    .roleType(RoleType.ROLE_ADMIN)
                    .build();
            Role role2 = Role.builder()
                    .roleType(RoleType.ROLE_STUDENT)
                    .build();

            roleRepo.save(role1);
            roleRepo.save(role2);


            User user = User.builder()
                    .firstName("Abdulaziz")
                    .roles(List.of(role1))
                    .lastName("Ulashbaev")
                    .password(passwordEncoder.encode("123"))
                    .phoneNumber("+0777")
                    .build();

            myUserRepo.save(user);

            for (int i = 1; i<=10 ; i++) {
                User user1 = User.builder()
                        .firstName("student "+i)
                        .roles(List.of(role2))
                        .lastName(i+"ov")
                        .password(passwordEncoder.encode("32"+i))
                        .phoneNumber("+05"+i)
                        .build();

                myUserRepo.save(user1);
                students.add(user1);
            }


            for (User user1 : myUserRepo.findAll()) {
                for (int i = 1; i <= 2; i++) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.add(Calendar.DAY_OF_YEAR, -i);

                    Payment payment = Payment.builder()
                            .amount(i*100)
                            .payType(PayType.PAY_TYPE_CARD)
                            .student(user1)
                            .date(calendar.getTime())
                            .build();

                    paymentRepo.save(payment);
                }
            }

            Group group1 = Group.builder().name("G35").build();
            Group group2 = Group.builder().name("G36").build();
            groupRepo.save(group1);
            groupRepo.save(group2);

            TimeTable timeTable1 = TimeTable.builder()
                    .name("Timetable - 1")
                    .group(group1)
                    .currentLesson(1)
                    .build();

            TimeTable timeTable2 = TimeTable.builder()
                    .name("Timetable - 2")
                    .group(group2)
                    .currentLesson(1)
                    .build();

            timeTableRepo.save(timeTable1);
            timeTableRepo.save(timeTable2);

            for (int i = 1; i <= 5 ; i++) {
                TimeTableStudent timeTableStudent = TimeTableStudent.builder()
                        .timeTable(timeTable1)
                        .student(students.get(i-1))
                        .build();
                timeTableStudentRepo.save(timeTableStudent);
                timeTableStudents.add(timeTableStudent);
            }

            for (int i = 6; i <= 10 ; i++) {
                TimeTableStudent timeTableStudent = TimeTableStudent.builder()
                        .timeTable(timeTable2)
                        .student(students.get(i-1))
                        .build();
                timeTableStudentRepo.save(timeTableStudent);
                timeTableStudents.add(timeTableStudent);
            }


            for (TimeTableStudent timeTableStudent : timeTableStudents) {
                List<StudentAttendance> studentAttendances = gen12lessons(timeTableStudent);
                studentAttendanceRepo.saveAll(studentAttendances);
            }


        }

    }

    private List<StudentAttendance> gen12lessons(TimeTableStudent timeTableStudent) {
        List<StudentAttendance> studentAttendances = new ArrayList<>();
        for (int i = 1; i <= 12 ; i++) {
            studentAttendances.add(
                    StudentAttendance.builder()
                            .timeTableStudent(timeTableStudent)
                            .attendance(false)
                            .lessonOrder(i)
                            .build()
            );
        }
        return studentAttendances;
    }

}
