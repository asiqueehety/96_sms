package com.example.__sms.controller;

import com.example.__sms.entity.Department;
import com.example.__sms.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/departments")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("departments", departmentService.findAll());
        return "departments/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("department", new Department());
        return "departments/form";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Department department) {
        departmentService.save(department);
        return "redirect:/departments";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<Department> department = departmentService.findById(id);
        if (department.isPresent()) {
            model.addAttribute("department", department.get());
            return "departments/form";
        }
        return "redirect:/departments";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute Department department) {
        departmentService.save(department);
        return "redirect:/departments";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        departmentService.deleteById(id);
        return "redirect:/departments";
    }
}
