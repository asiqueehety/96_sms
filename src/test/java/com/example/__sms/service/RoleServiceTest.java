package com.example.__sms.service;

import com.example.__sms.entity.Role;
import com.example.__sms.repository.RoleRepository;
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
class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleService roleService;

    private Role role;

    @BeforeEach
    void setUp() {
        role = new Role();
        role.setId(1L);
        role.setName("TEACHER");
    }

    @Test
    void findAll_ShouldReturnAllRoles() {
        Role role2 = new Role();
        role2.setId(2L);
        role2.setName("STUDENT");

        List<Role> roles = Arrays.asList(role, role2);
        when(roleRepository.findAll()).thenReturn(roles);

        List<Role> result = roleService.findAll();

        assertEquals(2, result.size());
        assertEquals("TEACHER", result.get(0).getName());
        assertEquals("STUDENT", result.get(1).getName());
        verify(roleRepository, times(1)).findAll();
    }

    @Test
    void findAll_WhenNoRoles_ShouldReturnEmptyList() {
        when(roleRepository.findAll()).thenReturn(List.of());

        List<Role> result = roleService.findAll();

        assertTrue(result.isEmpty());
        verify(roleRepository, times(1)).findAll();
    }

    @Test
    void findById_WhenRoleExists_ShouldReturnRole() {
        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));

        Optional<Role> result = roleService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("TEACHER", result.get().getName());
        verify(roleRepository, times(1)).findById(1L);
    }

    @Test
    void findById_WhenRoleDoesNotExist_ShouldReturnEmpty() {
        when(roleRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Role> result = roleService.findById(99L);

        assertFalse(result.isPresent());
        verify(roleRepository, times(1)).findById(99L);
    }

    @Test
    void findByName_WhenRoleExists_ShouldReturnRole() {
        when(roleRepository.findByName("TEACHER")).thenReturn(Optional.of(role));

        Optional<Role> result = roleService.findByName("TEACHER");

        assertTrue(result.isPresent());
        assertEquals("TEACHER", result.get().getName());
        verify(roleRepository, times(1)).findByName("TEACHER");
    }

    @Test
    void findByName_WhenRoleDoesNotExist_ShouldReturnEmpty() {
        when(roleRepository.findByName("ADMIN")).thenReturn(Optional.empty());

        Optional<Role> result = roleService.findByName("ADMIN");

        assertFalse(result.isPresent());
        verify(roleRepository, times(1)).findByName("ADMIN");
    }

    @Test
    void findByName_StudentRole_ShouldReturnStudentRole() {
        Role studentRole = new Role("STUDENT");
        studentRole.setId(2L);
        when(roleRepository.findByName("STUDENT")).thenReturn(Optional.of(studentRole));

        Optional<Role> result = roleService.findByName("STUDENT");

        assertTrue(result.isPresent());
        assertEquals("STUDENT", result.get().getName());
        verify(roleRepository, times(1)).findByName("STUDENT");
    }

    @Test
    void save_NewRole_ShouldSaveRole() {
        Role newRole = new Role("ADMIN");

        when(roleRepository.save(any(Role.class))).thenReturn(newRole);

        Role result = roleService.save(newRole);

        assertNotNull(result);
        assertEquals("ADMIN", result.getName());
        verify(roleRepository, times(1)).save(newRole);
    }

    @Test
    void save_ExistingRole_ShouldUpdateRole() {
        role.setName("UPDATED_ROLE");
        when(roleRepository.save(any(Role.class))).thenReturn(role);

        Role result = roleService.save(role);

        assertNotNull(result);
        assertEquals("UPDATED_ROLE", result.getName());
        verify(roleRepository, times(1)).save(role);
    }

    @Test
    void deleteById_ShouldCallRepositoryDelete() {
        doNothing().when(roleRepository).deleteById(1L);

        roleService.deleteById(1L);

        verify(roleRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteById_WithNonExistentId_ShouldStillCallRepository() {
        doNothing().when(roleRepository).deleteById(99L);

        roleService.deleteById(99L);

        verify(roleRepository, times(1)).deleteById(99L);
    }
}
