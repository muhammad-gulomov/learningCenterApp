package uz.pdp.learningcenterapp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import uz.pdp.learningcenterapp.entity.Role;
import uz.pdp.learningcenterapp.entity.User;
import uz.pdp.learningcenterapp.entity.enums.RoleType;
import uz.pdp.learningcenterapp.repo.MyUserRepo;
import uz.pdp.learningcenterapp.repo.PaymentRepo;
import uz.pdp.learningcenterapp.repo.RoleRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class MyUserController {
    private final MyUserRepo myUserRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;
    private final RoleRepo roleRepo;
    private final PaymentRepo paymentRepo;

    @GetMapping("/student")
    public String getStudent(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String phone = authentication.getName();
        User student = myUserRepo.findByPhoneNumber(phone);
        model.addAttribute("student", student);
        return "studentPage";
    }

    @GetMapping("/admin/student/more/{id}")
    public String moreStudent(@PathVariable Integer id, Model model){
        User user = myUserRepo.findById(id).get();
        model.addAttribute("student", user);
        return "studentPage";
    }

    @GetMapping("/admin")
    public String getAdmin(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String phone = authentication.getName();
        User admin = myUserRepo.findByPhoneNumber(phone);
        model.addAttribute("admin", admin);
        return "adminPage";
    }

    @GetMapping("/admin/student")
    private String getStudents(@RequestParam(value = "search", required = false) String search, Model model){

        if(search==null || search.equals("")){
            Role byRoleType = roleRepo.findAllByRoleType(RoleType.ROLE_STUDENT);
            List<User> students = myUserRepo.findByRoles(byRoleType);
            model.addAttribute("students", students);
        }else {
            List<User> students = myUserRepo.findAllByFirstNameContainingOrLastNameContaining(search, search);
            model.addAttribute("students", students);
        }
        return "student/student";
    }

    @GetMapping("/admin/student/add")
    public String addView(){
        return "student/addStudent";
    }

    @PostMapping("/admin/student/add")
    private String addStudent(@RequestParam String firstName, String lastName, String phoneNumber, String password){
        Role byRoleType = roleRepo.findAllByRoleType(RoleType.ROLE_STUDENT);

        User user = User.builder()
                .firstName(firstName)
                .lastName(lastName)
                .phoneNumber(phoneNumber)
                .password(passwordEncoder.encode(password))
                .roles(List.of(byRoleType))
                .build();
        myUserRepo.save(user);
        return "redirect:/admin/student";
    }

    @GetMapping("/admin/student/edit/{id}")
    public String editView(@PathVariable Integer id, Model model){
        User student = myUserRepo.findById(id).get();

        model.addAttribute("student", student);
        return "student/addStudent";
    }

    @PostMapping("/admin/student/edit")
    public String editCountry(@RequestParam Integer id, @RequestParam String firstName, String lastName, String phoneNumber) {
        User user = myUserRepo.findById(id).get();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPhoneNumber(phoneNumber);
        myUserRepo.save(user);
        return "redirect:/admin/student";
    }

    @GetMapping("/admin/student/delete/{id}")
    public String deleteDistrict(@PathVariable Integer id) {
        paymentRepo.deleteByStudentId(id);
        myUserRepo.deleteById(id);
        return "redirect:/admin/student";
    }

}
