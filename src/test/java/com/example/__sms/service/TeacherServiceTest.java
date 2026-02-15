package com.example.__sms.service;

import com.example.__sms.entity.Teacher;
import com.example.__sms.repository.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeacherServiceTest {

    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private TeacherService teacherService;

    private Teacher teacher;

    @BeforeEach
    void setUp() {
        teacher = new Teacher();
        teacher.setId(1L);
        teacher.setName("Dr. Smith");
        teacher.setEmail("smith@example.com");
        teacher.setPassword("password123");
        teacher.setPhone("1234567890");
    }

    @Test
    void findAll_ShouldReturnAllTeachers() {
        Teacher teacher2 = new Teacher();
        teacher2.setId(2L);
        teacher2.setName("Dr. Johnson");
        teacher2.setEmail("johnson@example.com");

        List<Teacher> teachers = Arrays.asList(teacher, teacher2);
        when(teacherRepository.findAll()).thenReturn(teachers);

        List<Teacher> result = teacherService.findAll();

        assertEquals(2, result.size());
        assertEquals("Dr. Smith", result.get(0).getName());
        assertEquals("Dr. Johnson", result.get(1).getName());
        verify(teacherRepository, times(1)).findAll();
    }

    @Test
    void findAll_WhenNoTeachers_ShouldReturnEmptyList() {
        when(teacherRepository.findAll()).thenReturn(List.of());

        List<Teacher> result = teacherService.findAll();

        assertTrue(result.isEmpty());
        verify(teacherRepository, times(1)).findAll();
    }

    @Test
    void findById_WhenTeacherExists_ShouldReturnTeacher() {
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher));

        Optional<Teacher> result = teacherService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("Dr. Smith", result.get().getName());
        assertEquals("smith@example.com", result.get().getEmail());
        verify(teacherRepository, times(1)).findById(1L);
    }

    @Test
    void findById_WhenTeacherDoesNotExist_ShouldReturnEmpty() {
        when(teacherRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Teacher> result = teacherService.findById(99L);

        assertFalse(result.isPresent());
        verify(teacherRepository, times(1)).findById(99L);
    }

    @Test
    void findByEmail_WhenTeacherExists_ShouldReturnTeacher() {
        when(teacherRepository.findByEmail("smith@example.com")).thenReturn(Optional.of(teacher));

        Optional<Teacher> result = teacherService.findByEmail("smith@example.com");

        assertTrue(result.isPresent());
        assertEquals("Dr. Smith", result.get().getName());
        verify(teacherRepository, times(1)).findByEmail("smith@example.com");
    }

    @Test
    void findByEmail_WhenTeacherDoesNotExist_ShouldReturnEmpty() {
        when(teacherRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        Optional<Teacher> result = teacherService.findByEmail("nonexistent@example.com");

        assertFalse(result.isPresent());
        verify(teacherRepository, times(1)).findByEmail("nonexistent@example.com");
    }

    @Test
    void save_NewTeacher_ShouldEncodePasswordAndSave() {
        Teacher newTeacher = new Teacher();
        newTeacher.setName("New Teacher");
        newTeacher.setEmail("new@example.com");
        newTeacher.setPassword("rawpassword");

        when(passwordEncoder.encode("rawpassword")).thenReturn("encodedpassword");
        when(teacherRepository.save(any(Teacher.class))).thenReturn(newTeacher);

        Teacher result = teacherService.save(newTeacher);

        verify(passwordEncoder, times(1)).encode("rawpassword");
        verify(teacherRepository, times(1)).save(newTeacher);
        assertEquals("encodedpassword", newTeacher.getPassword());
    }

    @Test
    void save_ExistingTeacher_ShouldNotEncodePassword() {
        when(teacherRepository.save(any(Teacher.class))).thenReturn(teacher);

        teacherService.save(teacher);

        verify(passwordEncoder, never()).encode(anyString());
        verify(teacherRepository, times(1)).save(teacher);
    }

    @Test
    void update_ShouldSaveTeacherWithoutEncodingPassword() {
        when(teacherRepository.save(any(Teacher.class))).thenReturn(teacher);

        Teacher result = teacherService.update(teacher);

        verify(passwordEncoder, never()).encode(anyString());
        verify(teacherRepository, times(1)).save(teacher);
        assertEquals("Dr. Smith", result.getName());
    }

    @Test
    void deleteById_ShouldCallRepositoryDelete() {
        doNothing().when(teacherRepository).deleteById(1L);

        teacherService.deleteById(1L);

        verify(teacherRepository, times(1)).deleteById(1L);
    }
}
