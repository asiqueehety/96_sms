package com.example.__sms.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        model.addAttribute("username", authentication.getName());

        boolean isTeacher = authentication.getAuthorities()
            .contains(new SimpleGrantedAuthority("ROLE_TEACHER"));
        model.addAttribute("isTeacher", isTeacher);

        return "dashboard";
    }
}
