package com.example.__sms.controller;

import com.example.__sms.entity.Student;
import com.example.__sms.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private StudentService studentService;

    @GetMapping
    public String viewProfile(Authentication authentication, Model model) {
        String email = authentication.getName();
        Optional<Student> student = studentService.findByEmail(email);
        if (student.isPresent()) {
            model.addAttribute("student", student.get());
            return "profile/view";
        }
        return "redirect:/dashboard";
    }

    @GetMapping("/edit")
    public String editProfile(Authentication authentication, Model model) {
        String email = authentication.getName();
        Optional<Student> student = studentService.findByEmail(email);
        if (student.isPresent()) {
            model.addAttribute("student", student.get());
            return "profile/edit";
        }
        return "redirect:/dashboard";
    }

    @PostMapping("/update")
    public String updateProfile(@RequestParam String name,
                                @RequestParam String phone,
                                Authentication authentication) {
        String email = authentication.getName();
        Optional<Student> student = studentService.findByEmail(email);
        if (student.isPresent()) {
            studentService.updateBasicInfo(student.get().getId(), name, phone);
        }
        return "redirect:/profile";
    }
}
