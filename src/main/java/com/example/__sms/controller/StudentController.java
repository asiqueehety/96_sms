package com.example.__sms.controller;

import com.example.__sms.entity.Student;
import com.example.__sms.service.DepartmentService;
import com.example.__sms.service.RoleService;
import com.example.__sms.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private RoleService roleService;

    @GetMapping
    public String list(Model model, Authentication authentication) {
        model.addAttribute("students", studentService.findAll());
        boolean isTeacher = authentication.getAuthorities()
            .contains(new SimpleGrantedAuthority("ROLE_TEACHER"));
        model.addAttribute("isTeacher", isTeacher);
        return "students/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("student", new Student());
        model.addAttribute("departments", departmentService.findAll());
        model.addAttribute("roles", roleService.findAll());
        return "students/form";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Student student,
                         @RequestParam Long departmentId,
                         @RequestParam Long roleId) {
        departmentService.findById(departmentId).ifPresent(student::setDepartment);
        roleService.findById(roleId).ifPresent(student::setRole);
        studentService.save(student);
        return "redirect:/students";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, Authentication authentication) {
        Optional<Student> student = studentService.findById(id);
        if (student.isPresent()) {
            boolean isTeacher = authentication.getAuthorities()
                .contains(new SimpleGrantedAuthority("ROLE_TEACHER"));

            if (!isTeacher) {
                String currentUserEmail = authentication.getName();
                if (!student.get().getEmail().equals(currentUserEmail)) {
                    return "redirect:/students?error=unauthorized";
                }
            }

            model.addAttribute("student", student.get());
            model.addAttribute("departments", departmentService.findAll());
            model.addAttribute("roles", roleService.findAll());
            model.addAttribute("isTeacher", isTeacher);
            return "students/form";
        }
        return "redirect:/students";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute Student student,
                         @RequestParam(required = false) Long departmentId,
                         @RequestParam(required = false) Long roleId,
                         Authentication authentication) {
        boolean isTeacher = authentication.getAuthorities()
            .contains(new SimpleGrantedAuthority("ROLE_TEACHER"));

        Optional<Student> existingStudent = studentService.findById(student.getId());
        if (existingStudent.isPresent()) {
            Student s = existingStudent.get();

            if (!isTeacher) {
                String currentUserEmail = authentication.getName();
                if (!s.getEmail().equals(currentUserEmail)) {
                    return "redirect:/students?error=unauthorized";
                }
                s.setName(student.getName());
                s.setPhone(student.getPhone());
            } else {
                s.setName(student.getName());
                s.setEmail(student.getEmail());
                s.setPhone(student.getPhone());
                s.setRoll(student.getRoll());
                s.setMarks(student.getMarks());
                s.setGpa(student.getGpa());
                s.setCgpa(student.getCgpa());
                if (departmentId != null) {
                    departmentService.findById(departmentId).ifPresent(s::setDepartment);
                }
                if (roleId != null) {
                    roleService.findById(roleId).ifPresent(s::setRole);
                }
            }
            studentService.update(s);
        }
        return "redirect:/students";
    }

    // Only teachers can delete
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        studentService.deleteById(id);
        return "redirect:/students";
    }
}
