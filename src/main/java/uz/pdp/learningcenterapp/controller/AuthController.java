package uz.pdp.learningcenterapp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import uz.pdp.learningcenterapp.entity.User;
import uz.pdp.learningcenterapp.repo.MyUserRepo;

@Controller
@RequiredArgsConstructor
public class AuthController {
    private final MyUserRepo myUserRepo;


    @GetMapping("/login")
    public String loginn(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String phone = authentication.getName();
        User user = myUserRepo.findByPhoneNumber(phone);
        model.addAttribute("myuser", user);
        return "login";
    }


}
