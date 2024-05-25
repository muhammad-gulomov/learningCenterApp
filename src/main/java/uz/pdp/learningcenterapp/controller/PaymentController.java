package uz.pdp.learningcenterapp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import uz.pdp.learningcenterapp.entity.Group;
import uz.pdp.learningcenterapp.entity.Payment;
import uz.pdp.learningcenterapp.entity.User;
import uz.pdp.learningcenterapp.entity.enums.PayType;
import uz.pdp.learningcenterapp.repo.MyUserRepo;
import uz.pdp.learningcenterapp.repo.PaymentRepo;
import uz.pdp.learningcenterapp.repo.RoleRepo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentRepo paymentRepo;
    private final MyUserRepo myUserRepo;

    @GetMapping("/admin/payment")
    public String searchPayments(
            @RequestParam(value = "startDate", required = false) String startDateStr,
            @RequestParam(value = "endDate", required = false) String endDateStr,
            Model model) {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        try {

            if(startDateStr!=null && endDateStr!=null){
                Date startDate = formatter.parse(startDateStr);
                Date endDate = formatter.parse(endDateStr);

                List<Payment> payments = paymentRepo.findPaymentsByDateRange(startDate, endDate);
                model.addAttribute("payments", payments);
            }else{
                List<Payment> payments = paymentRepo.findAll();
                model.addAttribute("payments", payments);
            }


        } catch (ParseException e) {
            model.addAttribute("error", "Invalid date format. Please use yyyy-MM-dd.");
            List<Payment> payments = paymentRepo.findAll();
            model.addAttribute("payments", payments);
        }

        return "payment/payment";
    }

    @GetMapping("/admin/payment/add")
    public String addView(Model model){
        List<User> students = myUserRepo.findRole();
        model.addAttribute("payment", new Payment());
        model.addAttribute("payTypes", PayType.values());
        model.addAttribute("students", students);
        return "payment/addPayment";
    }

    @PostMapping("/admin/payment/add")
    private String addPayment( @ModelAttribute Payment payment){
       payment.setDate(Calendar.getInstance().getTime());



        paymentRepo.save(payment);
        return "redirect:/admin/payment";
    }

    @GetMapping("/admin/payment/edit/{id}")
    public String editView(@PathVariable UUID id, Model model){
        Payment payment = paymentRepo.findById(id).get();
        model.addAttribute("payment", payment);
        model.addAttribute("students", myUserRepo.findRole());
        model.addAttribute("payTypes", PayType.values());
        return "payment/addPayment";
    }

    @PostMapping("/admin/payment/edit")
    public String editCountry(@ModelAttribute Payment payment) {
        payment.setDate(Calendar.getInstance().getTime());
        paymentRepo.save(payment);
        return "redirect:/admin/payment";

    }

    @GetMapping("/admin/payment/delete/{id}")
    public String deleteDistrict(@PathVariable UUID id) {
        paymentRepo.deleteById(id);
        return "redirect:/admin/payment";
    }

}
