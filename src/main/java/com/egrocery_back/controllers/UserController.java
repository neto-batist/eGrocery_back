package com.egrocery_back.controllers;

import com.egrocery_back.dto.UserDTO;
import com.egrocery_back.dto.UserFilterDTO;
import com.egrocery_back.errors.NotFound;
import com.egrocery_back.errors.WrongPassword;
import com.egrocery_back.services.UserService;
import com.egrocery_back.services.UserServiceImpl;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@Validated
public class UserController {

    private final UserService userService;

    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    // Criar usuário
    @PostMapping("/create")
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO userDTO) throws NotFound {
        UserDTO createdUser = userService.saveUser(userDTO);
        return ResponseEntity.ok(createdUser);
    }

    @GetMapping
    public ResponseEntity<Page<UserDTO>> getAllUsers(
            @Valid UserFilterDTO filterDTO,
            @PageableDefault(size = 10)
            @SortDefault.SortDefaults({
                    @SortDefault(sort = "name")
            }) Pageable pageable) {
        Page<UserDTO> users = userService.getAllUsers(filterDTO, pageable);
        return ResponseEntity.ok(users);
    }

    // Obter usuário por ID
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Integer id) throws NotFound {
        UserDTO user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    // Atualizar usuário por ID
    @PutMapping("/update/{id}")
    public ResponseEntity<UserDTO> updateUser(
            @PathVariable Integer id,
            @Valid @RequestBody UserDTO userDTO) throws NotFound {
        UserDTO updatedUser = userService.updateUser(id, userDTO);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) throws NotFound {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/login")
    public ResponseEntity<UserDTO> loginUser(
            @Valid UserFilterDTO filterDTO,
            @Valid @RequestBody UserDTO userDTO) throws NotFound, WrongPassword {
        UserDTO user = userService.login(filterDTO, userDTO);
        return ResponseEntity.ok(user);
    }
}
