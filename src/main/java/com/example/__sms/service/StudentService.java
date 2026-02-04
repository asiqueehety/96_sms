package com.example.__sms.service;

import com.example.__sms.entity.Student;
import com.example.__sms.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    public Optional<Student> findById(Long id) {
        return studentRepository.findById(id);
    }

    public Optional<Student> findByEmail(String email) {
        return studentRepository.findByEmail(email);
    }

    public Optional<Student> findByRoll(String roll) {
        return studentRepository.findByRoll(roll);
    }

    public Student save(Student student) {
        if (student.getId() == null) {
            student.setPassword(passwordEncoder.encode(student.getPassword()));
        }
        return studentRepository.save(student);
    }

    public Student update(Student student) {
        return studentRepository.save(student);
    }

    // For student self-edit (only name and phone)
    public Student updateBasicInfo(Long id, String name, String phone) {
        Optional<Student> optStudent = studentRepository.findById(id);
        if (optStudent.isPresent()) {
            Student student = optStudent.get();
            student.setName(name);
            student.setPhone(phone);
            return studentRepository.save(student);
        }
        return null;
    }

    public void deleteById(Long id) {
        studentRepository.deleteById(id);
    }
}
