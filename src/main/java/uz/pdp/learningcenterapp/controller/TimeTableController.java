package uz.pdp.learningcenterapp.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import uz.pdp.learningcenterapp.entity.*;
import uz.pdp.learningcenterapp.projections.TimeTableProjection;
import uz.pdp.learningcenterapp.repo.*;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

@RequestMapping("/timetable")
@Controller
@RequiredArgsConstructor
public class TimeTableController {
    private final GroupRepo groupRepo;
    private final TimeTableRepo timeTableRepo;
    private final StudentAttendanceRepo studentAttendanceRepo;
    private final TimeTableStudentRepo timeTableStudentRepo;
    private final PaymentRepo paymentRepo;
    private final MyUserRepo myUserRepo;


    @GetMapping
    public String getTimeTable(
            Model model,
            @RequestParam(required = false) Integer groupId,
            @RequestParam(required = false) Integer timeTableId,
            @RequestParam(required = false) Integer currentLesson,
            HttpSession session
    ){
        List<Group> groups = groupRepo.findAll();
        List<Integer> lessons = List.of(1,2,3,4,5,6,7,8,9,10,11,12);

            if(groupId != null) {
                List<TimeTable> timeTables = timeTableRepo.findAllByGroupId(groupId);
                session.setAttribute("cLesson", currentLesson);
                session.setAttribute("ls", lessons);
                session.setAttribute("tables", timeTables);
                session.setAttribute("gId", groupId);
                model.addAttribute("lessons", session.getAttribute("ls"));
                model.addAttribute("timetables", session.getAttribute("tables"));
                model.addAttribute("groupId", session.getAttribute("gId"));
                model.addAttribute("currentLesson", session.getAttribute("cLesson"));
                model.addAttribute("changes", new ArrayList<>());
                if (timeTableId != null) {
                    session.setAttribute("ttId", timeTableId);
                    model.addAttribute("timeTableId", session.getAttribute("ttId"));
                    List<TimeTableProjection> timeTableData = timeTableRepo.getTimeTableData(timeTableId);
                    session.setAttribute("tableData", timeTableData);
                    model.addAttribute("timeTableData", session.getAttribute("tableData"));
                }

                model.addAttribute("groups", groups);
                return "timeTable/timeTable";
            }else{

                model.addAttribute("lessons", session.getAttribute("ls"));
                model.addAttribute("timetables", session.getAttribute("tables"));
                model.addAttribute("groupId", session.getAttribute("gId"));
                model.addAttribute("currentLesson", session.getAttribute("cLesson"));
                model.addAttribute("timeTableId", session.getAttribute("ttId"));
                model.addAttribute("timeTableData", session.getAttribute("tableData"));
                model.addAttribute("groups", groups);
                return "timeTable/timeTable";
            }
       }

    @PostMapping("/edit")
    public String editAttendance(
            @RequestParam(required = false) Integer column,
            @RequestParam(required = false) Integer row,
            @RequestParam(required = false) Integer rowSize,
            @RequestParam(required = false) Integer currentL,
            @RequestParam(required = false) Integer chosenStudentId,
            @RequestParam Boolean attendance,
            Model model
    ){
        System.out.println(column);
        System.out.println(row);
        System.out.println(currentL);


            if(row==currentL) {
                if (column != null && row != null) {

                    int id = rowSize * (column - 1) + row;

                    System.out.println(chosenStudentId);
                    System.out.println(row);

                    StudentAttendance studentAttendance = studentAttendanceRepo.findAllByTimeTableStudentIdAndLessonOrder(chosenStudentId, row).get(0);
                    Boolean b = studentAttendance.getAttendance();
                    if (b.equals(true)) {
                        studentAttendance.setAttendance(false);
                        studentAttendanceRepo.save(studentAttendance);
                    } else {
                        studentAttendance.setAttendance(true);
                        studentAttendanceRepo.save(studentAttendance);
                    }
                }
            }

            return "redirect:/timetable";
    }

    @GetMapping("/new")
    public String createTimeTableForm(Model model) {
        model.addAttribute("timetable", new TimeTable());
        model.addAttribute("groups", groupRepo.findAll());
        return "timeTable/addTimeTable";
    }

    @PostMapping("/add")
    public String saveTimeTable(@ModelAttribute TimeTable timeTable) {
        System.out.println(timeTable);
        timeTable.setCurrentLesson(1);
        timeTable.setPrice(150);
        timeTableRepo.save(timeTable);
        return "redirect:/timetable";
    }

    @GetMapping("/newStudents")
    public String createTimeTableStudentForm(Model model) {
        model.addAttribute("timetableStudent", new TimeTableStudent());
        model.addAttribute("timeTables", timeTableRepo.findAll());
        model.addAttribute("students", myUserRepo.findRole());
        return "timeTable/addTimeTableStudent";
    }

    @PostMapping("/student/add")
    public String saveTimeTableStudent(@ModelAttribute TimeTableStudent timeTableStudent) {
        System.out.println(timeTableStudent);
        User student = timeTableStudent.getStudent();
        Integer sum = paymentRepo.findSumPaymentsByStudentId(student.getId());
        Integer paidAll = timeTableRepo.getTimeTablePriceByStudentId(student.getId());

        if(paidAll!=null){
            if(sum>=paidAll){
                Integer countModules = timeTableRepo.getCount(student.getId());
                sum=sum-(paidAll*countModules);
                timeTableStudent.setPaid(sum);
                List<StudentAttendance> attendances = gen12lessons(timeTableStudent);
                timeTableStudent.setStudentAttendances(attendances);
                timeTableStudentRepo.save(timeTableStudent);
                studentAttendanceRepo.saveAll(attendances);
            }
        }else {
            if(sum>=150){
                timeTableStudent.setPaid(150);
                List<StudentAttendance> attendances = gen12lessons(timeTableStudent);
                timeTableStudent.setStudentAttendances(attendances);
                timeTableStudentRepo.save(timeTableStudent);
                studentAttendanceRepo.saveAll(attendances);
            }else {
                timeTableStudent.setPaid(sum);
                List<StudentAttendance> attendances = gen12lessons(timeTableStudent);
                timeTableStudent.setStudentAttendances(attendances);
                timeTableStudentRepo.save(timeTableStudent);
                studentAttendanceRepo.saveAll(attendances);
            }
        }


        return "redirect:/timetable";
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
