package com.example.__sms.service;

import com.example.__sms.entity.Student;
import com.example.__sms.repository.StudentRepository;
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
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private StudentService studentService;

    private Student student;

    @BeforeEach
    void setUp() {
        student = new Student();
        student.setId(1L);
        student.setName("John Doe");
        student.setEmail("john@example.com");
        student.setPassword("password123");
        student.setRoll("2021-001");
        student.setPhone("1234567890");
        student.setMarks(85.5);
        student.setGpa(3.5);
        student.setCgpa(3.6);
    }

    @Test
    void findAll_ShouldReturnAllStudents() {
        Student student2 = new Student();
        student2.setId(2L);
        student2.setName("Jane Doe");
        student2.setEmail("jane@example.com");
        student2.setRoll("2021-002");

        List<Student> students = Arrays.asList(student, student2);
        when(studentRepository.findAll()).thenReturn(students);

        List<Student> result = studentService.findAll();

        assertEquals(2, result.size());
        assertEquals("John Doe", result.get(0).getName());
        assertEquals("Jane Doe", result.get(1).getName());
        verify(studentRepository, times(1)).findAll();
    }

    @Test
    void findById_WhenStudentExists_ShouldReturnStudent() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        Optional<Student> result = studentService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("John Doe", result.get().getName());
        assertEquals("john@example.com", result.get().getEmail());
        verify(studentRepository, times(1)).findById(1L);
    }

    @Test
    void findById_WhenStudentDoesNotExist_ShouldReturnEmpty() {
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Student> result = studentService.findById(99L);

        assertFalse(result.isPresent());
        verify(studentRepository, times(1)).findById(99L);
    }

    @Test
    void findByEmail_WhenStudentExists_ShouldReturnStudent() {
        when(studentRepository.findByEmail("john@example.com")).thenReturn(Optional.of(student));

        Optional<Student> result = studentService.findByEmail("john@example.com");

        assertTrue(result.isPresent());
        assertEquals("John Doe", result.get().getName());
        verify(studentRepository, times(1)).findByEmail("john@example.com");
    }

    @Test
    void findByEmail_WhenStudentDoesNotExist_ShouldReturnEmpty() {
        when(studentRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        Optional<Student> result = studentService.findByEmail("nonexistent@example.com");

        assertFalse(result.isPresent());
        verify(studentRepository, times(1)).findByEmail("nonexistent@example.com");
    }

    @Test
    void findByRoll_WhenStudentExists_ShouldReturnStudent() {
        when(studentRepository.findByRoll("2021-001")).thenReturn(Optional.of(student));

        Optional<Student> result = studentService.findByRoll("2021-001");

        assertTrue(result.isPresent());
        assertEquals("John Doe", result.get().getName());
        verify(studentRepository, times(1)).findByRoll("2021-001");
    }

    @Test
    void findByRoll_WhenStudentDoesNotExist_ShouldReturnEmpty() {
        when(studentRepository.findByRoll("9999-999")).thenReturn(Optional.empty());

        Optional<Student> result = studentService.findByRoll("9999-999");

        assertFalse(result.isPresent());
        verify(studentRepository, times(1)).findByRoll("9999-999");
    }

    @Test
    void save_NewStudent_ShouldEncodePasswordAndSave() {
        Student newStudent = new Student();
        newStudent.setName("New Student");
        newStudent.setEmail("new@example.com");
        newStudent.setPassword("rawpassword");
        newStudent.setRoll("2021-003");

        when(passwordEncoder.encode("rawpassword")).thenReturn("encodedpassword");
        when(studentRepository.save(any(Student.class))).thenReturn(newStudent);

        Student result = studentService.save(newStudent);

        verify(passwordEncoder, times(1)).encode("rawpassword");
        verify(studentRepository, times(1)).save(newStudent);
        assertEquals("encodedpassword", newStudent.getPassword());
    }

    @Test
    void save_ExistingStudent_ShouldNotEncodePassword() {
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        Student result = studentService.save(student);

        verify(passwordEncoder, never()).encode(anyString());
        verify(studentRepository, times(1)).save(student);
    }

    @Test
    void update_ShouldSaveStudentWithoutEncodingPassword() {
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        Student result = studentService.update(student);

        verify(passwordEncoder, never()).encode(anyString());
        verify(studentRepository, times(1)).save(student);
        assertEquals("John Doe", result.getName());
    }

    @Test
    void updateBasicInfo_WhenStudentExists_ShouldUpdateNameAndPhone() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        Student result = studentService.updateBasicInfo(1L, "Updated Name", "9876543210");

        assertNotNull(result);
        assertEquals("Updated Name", result.getName());
        assertEquals("9876543210", result.getPhone());
        verify(studentRepository, times(1)).findById(1L);
        verify(studentRepository, times(1)).save(student);
    }

    @Test
    void updateBasicInfo_WhenStudentDoesNotExist_ShouldReturnNull() {
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());

        Student result = studentService.updateBasicInfo(99L, "Updated Name", "9876543210");

        assertNull(result);
        verify(studentRepository, times(1)).findById(99L);
        verify(studentRepository, never()).save(any(Student.class));
    }

    @Test
    void deleteById_ShouldCallRepositoryDelete() {
        doNothing().when(studentRepository).deleteById(1L);

        studentService.deleteById(1L);

        verify(studentRepository, times(1)).deleteById(1L);
    }
}
