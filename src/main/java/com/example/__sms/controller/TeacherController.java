package com.example.__sms.controller;

import com.example.__sms.entity.Teacher;
import com.example.__sms.service.DepartmentService;
import com.example.__sms.service.RoleService;
import com.example.__sms.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/teachers")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private RoleService roleService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("teachers", teacherService.findAll());
        return "teachers/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("teacher", new Teacher());
        model.addAttribute("departments", departmentService.findAll());
        model.addAttribute("roles", roleService.findAll());
        return "teachers/form";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Teacher teacher,
                         @RequestParam Long departmentId,
                         @RequestParam Long roleId) {
        departmentService.findById(departmentId).ifPresent(teacher::setDepartment);
        roleService.findById(roleId).ifPresent(teacher::setRole);
        teacherService.save(teacher);
        return "redirect:/teachers";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<Teacher> teacher = teacherService.findById(id);
        if (teacher.isPresent()) {
            model.addAttribute("teacher", teacher.get());
            model.addAttribute("departments", departmentService.findAll());
            model.addAttribute("roles", roleService.findAll());
            return "teachers/form";
        }
        return "redirect:/teachers";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute Teacher teacher,
                         @RequestParam Long departmentId,
                         @RequestParam Long roleId) {
        Optional<Teacher> existingTeacher = teacherService.findById(teacher.getId());
        if (existingTeacher.isPresent()) {
            Teacher t = existingTeacher.get();
            t.setName(teacher.getName());
            t.setEmail(teacher.getEmail());
            t.setPhone(teacher.getPhone());
            departmentService.findById(departmentId).ifPresent(t::setDepartment);
            roleService.findById(roleId).ifPresent(t::setRole);
            teacherService.update(t);
        }
        return "redirect:/teachers";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        teacherService.deleteById(id);
        return "redirect:/teachers";
    }
}
