package uz.pdp.learningcenterapp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import uz.pdp.learningcenterapp.entity.Group;
import uz.pdp.learningcenterapp.repo.GroupRepo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class GroupController {
    private final GroupRepo groupRepo;

    @GetMapping("/admin/group")
    public String showGroup(Model model){
        List<Group> all = groupRepo.findAll();
        model.addAttribute("groups", all);
        return "group/group";
    }

    @GetMapping("/admin/group/add")
    public String addView(){
        return "group/addGroup";
    }

    @PostMapping("/admin/group/add")
    private String addGroup(@RequestParam String name){
        Group group = Group.builder().name(name).build();
        groupRepo.save(group);
        return "redirect:/admin/group";
    }

    @GetMapping("/admin/group/edit/{id}")
    public String editView(@PathVariable Integer id, Model model){
        Group group = groupRepo.findById(id).get();

        model.addAttribute("group", group);
        return "group/addGroup";
    }

    @PostMapping("/admin/group/edit")
    public String editCountry(@RequestParam Integer id, @RequestParam String name) {
        Group group = groupRepo.findById(id).get();
        group.setName(name);
        groupRepo.save(group);
        return "redirect:/admin/group";

    }

    @GetMapping("/admin/group/delete/{id}")
    public String deleteDistrict(@PathVariable Integer id) {
        groupRepo.deleteById(id);
        return "redirect:/admin/group";
    }


}
