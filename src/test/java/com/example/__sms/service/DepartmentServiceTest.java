package com.example.__sms.service;

import com.example.__sms.entity.Department;
import com.example.__sms.repository.DepartmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DepartmentServiceTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private DepartmentService departmentService;

    private Department department;

    @BeforeEach
    void setUp() {
        department = new Department();
        department.setId(1L);
        department.setName("Computer Science");
        department.setDescription("Department of Computer Science and Engineering");
    }

    @Test
    void findAll_ShouldReturnAllDepartments() {
        Department department2 = new Department();
        department2.setId(2L);
        department2.setName("Electrical Engineering");
        department2.setDescription("Department of Electrical Engineering");

        List<Department> departments = Arrays.asList(department, department2);
        when(departmentRepository.findAll()).thenReturn(departments);

        List<Department> result = departmentService.findAll();

        assertEquals(2, result.size());
        assertEquals("Computer Science", result.get(0).getName());
        assertEquals("Electrical Engineering", result.get(1).getName());
        verify(departmentRepository, times(1)).findAll();
    }

    @Test
    void findAll_WhenNoDepartments_ShouldReturnEmptyList() {
        when(departmentRepository.findAll()).thenReturn(List.of());

        List<Department> result = departmentService.findAll();

        assertTrue(result.isEmpty());
        verify(departmentRepository, times(1)).findAll();
    }

    @Test
    void findById_WhenDepartmentExists_ShouldReturnDepartment() {
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));

        Optional<Department> result = departmentService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("Computer Science", result.get().getName());
        assertEquals("Department of Computer Science and Engineering", result.get().getDescription());
        verify(departmentRepository, times(1)).findById(1L);
    }

    @Test
    void findById_WhenDepartmentDoesNotExist_ShouldReturnEmpty() {
        when(departmentRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Department> result = departmentService.findById(99L);

        assertFalse(result.isPresent());
        verify(departmentRepository, times(1)).findById(99L);
    }

    @Test
    void findByName_WhenDepartmentExists_ShouldReturnDepartment() {
        when(departmentRepository.findByName("Computer Science")).thenReturn(Optional.of(department));

        Optional<Department> result = departmentService.findByName("Computer Science");

        assertTrue(result.isPresent());
        assertEquals("Computer Science", result.get().getName());
        verify(departmentRepository, times(1)).findByName("Computer Science");
    }

    @Test
    void findByName_WhenDepartmentDoesNotExist_ShouldReturnEmpty() {
        when(departmentRepository.findByName("Nonexistent Department")).thenReturn(Optional.empty());

        Optional<Department> result = departmentService.findByName("Nonexistent Department");

        assertFalse(result.isPresent());
        verify(departmentRepository, times(1)).findByName("Nonexistent Department");
    }

    @Test
    void save_ShouldSaveDepartment() {
        Department newDepartment = new Department("Mechanical Engineering", "Department of Mechanical Engineering");

        when(departmentRepository.save(any(Department.class))).thenReturn(newDepartment);

        Department result = departmentService.save(newDepartment);

        assertNotNull(result);
        assertEquals("Mechanical Engineering", result.getName());
        verify(departmentRepository, times(1)).save(newDepartment);
    }

    @Test
    void save_ShouldUpdateExistingDepartment() {
        department.setDescription("Updated Description");
        when(departmentRepository.save(any(Department.class))).thenReturn(department);

        Department result = departmentService.save(department);

        assertNotNull(result);
        assertEquals("Updated Description", result.getDescription());
        verify(departmentRepository, times(1)).save(department);
    }

    @Test
    void deleteById_ShouldCallRepositoryDelete() {
        doNothing().when(departmentRepository).deleteById(1L);

        departmentService.deleteById(1L);

        verify(departmentRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteById_WithNonExistentId_ShouldStillCallRepository() {
        doNothing().when(departmentRepository).deleteById(99L);

        departmentService.deleteById(99L);

        verify(departmentRepository, times(1)).deleteById(99L);
    }
}
