package com.example.__sms.controller;

import com.example.__sms.entity.Role;
import com.example.__sms.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("roles", roleService.findAll());
        return "roles/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("role", new Role());
        return "roles/form";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Role role) {
        roleService.save(role);
        return "redirect:/roles";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<Role> role = roleService.findById(id);
        if (role.isPresent()) {
            model.addAttribute("role", role.get());
            return "roles/form";
        }
        return "redirect:/roles";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute Role role) {
        roleService.save(role);
        return "redirect:/roles";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        roleService.deleteById(id);
        return "redirect:/roles";
    }
}
