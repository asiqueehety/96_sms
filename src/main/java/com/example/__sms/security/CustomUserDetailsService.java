package com.example.__sms.security;

import com.example.__sms.entity.Student;
import com.example.__sms.entity.Teacher;
import com.example.__sms.repository.StudentRepository;
import com.example.__sms.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        //teacher
        Optional<Teacher> teacher = teacherRepository.findByEmail(email);
        if (teacher.isPresent()) {
            Teacher t = teacher.get();
            return new User(
                t.getEmail(),
                t.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + t.getRole().getName().toUpperCase()))
            );
        }

        //student
        Optional<Student> student = studentRepository.findByEmail(email);
        if (student.isPresent()) {
            Student s = student.get();
            return new User(
                s.getEmail(),
                s.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + s.getRole().getName().toUpperCase()))
            );
        }

        throw new UsernameNotFoundException("User not found with email: " + email);
    }
}
