package com.example.__sms.config;

import com.example.__sms.entity.Department;
import com.example.__sms.entity.Role;
import com.example.__sms.entity.Teacher;
import com.example.__sms.repository.DepartmentRepository;
import com.example.__sms.repository.RoleRepository;
import com.example.__sms.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        Role teacherRole = roleRepository.findByName("TEACHER").orElseGet(() -> {
            Role role = new Role("TEACHER");
            return roleRepository.save(role);
        });

        Role studentRole = roleRepository.findByName("STUDENT").orElseGet(() -> {
            Role role = new Role("STUDENT");
            return roleRepository.save(role);
        });

        Department defaultDept = departmentRepository.findByName("CSE").orElseGet(() -> {
            Department dept = new Department("CSE", "Department of Computer Science and Engineering");
            return departmentRepository.save(dept);
        });

        if (teacherRepository.findByEmail("adminbhai@sms.com").isEmpty()) {
            Teacher admin = new Teacher();
            admin.setName("shikkhok");
            admin.setEmail("adminbhai@sms.com");
            admin.setPassword(passwordEncoder.encode("adminbhai"));
            admin.setPhone("01896121096");
            admin.setDepartment(defaultDept);
            admin.setRole(teacherRole);
            teacherRepository.save(admin);

            System.out.println("Email: adminbhai@sms.com");
            System.out.println("Password: adminbhai");
        }
    }
}
