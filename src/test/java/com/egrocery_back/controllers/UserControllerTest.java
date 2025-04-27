package com.egrocery_back.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.egrocery_back.dto.UserDTO;
import com.egrocery_back.dto.UserFilterDTO;
import com.egrocery_back.errors.NotFound;
import com.egrocery_back.errors.WrongPassword;
import com.egrocery_back.services.UserServiceImpl;

public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserServiceImpl userService;

    private UserDTO userDTO;
    private UserFilterDTO userFilterDTO;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userDTO = new UserDTO();
        userDTO.setId(1);
        userDTO.setName("João Silva");
        userDTO.setEmail("joao.silva@example.com");
        userDTO.setPassword("password123");
        userFilterDTO = new UserFilterDTO();
        pageable = PageRequest.of(0, 10);
    }

    @Test
    void createUser_ValidInput_ReturnsOkWithCreatedUserDTO() throws NotFound {
        when(userService.saveUser(userDTO)).thenReturn(userDTO);

        ResponseEntity<UserDTO> response = userController.createUser(userDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDTO, response.getBody());
        verify(userService, times(1)).saveUser(userDTO);
    }

    @Test
    void createUser_NotFoundExceptionFromService_ThrowsNotFound() throws NotFound {
        when(userService.saveUser(userDTO)).thenThrow(new NotFound("Erro ao criar usuário",1));

        assertThrows(NotFound.class, () -> userController.createUser(userDTO));
        verify(userService, times(1)).saveUser(userDTO);
    }

    @Test
    void getAllUsers_NoFilter_ReturnsOkWithUserPage() {
        List<UserDTO> usersList = Collections.singletonList(userDTO);
        Page<UserDTO> userPage = new PageImpl<>(usersList, pageable, usersList.size());
        when(userService.getAllUsers(any(UserFilterDTO.class), any(Pageable.class))).thenReturn(userPage);

        ResponseEntity<Page<UserDTO>> response = userController.getAllUsers(userFilterDTO, pageable);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userPage, response.getBody());
        verify(userService, times(1)).getAllUsers(userFilterDTO, pageable);
    }

    @Test
    void getUserById_ValidId_ReturnsOkWithUserDTO() throws NotFound {
        int userId = 1;
        when(userService.getUserById(userId)).thenReturn(userDTO);

        ResponseEntity<UserDTO> response = userController.getUserById(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDTO, response.getBody());
        verify(userService, times(1)).getUserById(userId);
    }

    @Test
    void getUserById_NotFoundExceptionFromService_ThrowsNotFound() throws NotFound {
        int userId = 1;
        when(userService.getUserById(userId)).thenThrow(new NotFound("Usuário não encontrado",1));

        assertThrows(NotFound.class, () -> userController.getUserById(userId));
        verify(userService, times(1)).getUserById(userId);
    }

    @Test
    void updateUser_ValidInput_ReturnsOkWithUpdatedUserDTO() throws NotFound {
        int userId = 1;
        UserDTO updatedUserDTO = new UserDTO();
        updatedUserDTO.setId(userId);
        updatedUserDTO.setName("Maria Souza");
        updatedUserDTO.setEmail("maria.souza@example.com");
        when(userService.updateUser(userId, userDTO)).thenReturn(updatedUserDTO);

        ResponseEntity<UserDTO> response = userController.updateUser(userId, userDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedUserDTO, response.getBody());
        verify(userService, times(1)).updateUser(userId, userDTO);
    }

    @Test
    void updateUser_NotFoundExceptionFromService_ThrowsNotFound() throws NotFound {
        int userId = 1;
        when(userService.updateUser(userId, userDTO)).thenThrow(new NotFound("Usuário não encontrado",1));

        assertThrows(NotFound.class, () -> userController.updateUser(userId, userDTO));
        verify(userService, times(1)).updateUser(userId, userDTO);
    }

    @Test
    void deleteUser_ValidId_ReturnsNoContent() throws NotFound {
        int userId = 1;
        doNothing().when(userService).deleteUser(userId);

        ResponseEntity<Void> response = userController.deleteUser(userId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(userService, times(1)).deleteUser(userId);
    }

    @Test
    void deleteUser_NotFoundExceptionFromService_ThrowsNotFound() throws NotFound {
        int userId = 1;
        doThrow(new NotFound("Usuário não encontrado",1)).when(userService).deleteUser(userId);

        assertThrows(NotFound.class, () -> userController.deleteUser(userId));
        verify(userService, times(1)).deleteUser(userId);
    }

    @Test
    void loginUser_ValidCredentials_ReturnsOkWithUserDTO() throws NotFound, WrongPassword {
        when(userService.login(userFilterDTO, userDTO)).thenReturn(userDTO);

        ResponseEntity<UserDTO> response = userController.loginUser(userFilterDTO, userDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDTO, response.getBody());
        verify(userService, times(1)).login(userFilterDTO, userDTO);
    }

    @Test
    void loginUser_NotFoundExceptionFromService_ThrowsNotFound() throws NotFound, WrongPassword {
        when(userService.login(userFilterDTO, userDTO)).thenThrow(new NotFound("Usuário não encontrado",1));

        assertThrows(NotFound.class, () -> userController.loginUser(userFilterDTO, userDTO));
        verify(userService, times(1)).login(userFilterDTO, userDTO);
    }

    @Test
    void loginUser_WrongPasswordExceptionFromService_ThrowsWrongPassword() throws NotFound, WrongPassword {
        when(userService.login(userFilterDTO, userDTO)).thenThrow(new WrongPassword());

        assertThrows(WrongPassword.class, () -> userController.loginUser(userFilterDTO, userDTO));
        verify(userService, times(1)).login(userFilterDTO, userDTO);
    }
}