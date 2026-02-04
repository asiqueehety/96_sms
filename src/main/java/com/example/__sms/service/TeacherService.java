package com.example.__sms.service;

import com.example.__sms.entity.Teacher;
import com.example.__sms.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TeacherService {

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Teacher> findAll() {
        return teacherRepository.findAll();
    }

    public Optional<Teacher> findById(Long id) {
        return teacherRepository.findById(id);
    }

    public Optional<Teacher> findByEmail(String email) {
        return teacherRepository.findByEmail(email);
    }

    public Teacher save(Teacher teacher) {
        if (teacher.getId() == null) {
            teacher.setPassword(passwordEncoder.encode(teacher.getPassword()));
        }
        return teacherRepository.save(teacher);
    }

    public Teacher update(Teacher teacher) {
        return teacherRepository.save(teacher);
    }

    public void deleteById(Long id) {
        teacherRepository.deleteById(id);
    }
}
