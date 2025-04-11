package com.egrocery_back.controllers;

import com.egrocery_back.models.UsersEntity;
import com.egrocery_back.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UsersController {

    @Autowired
    private UsersService usersService;

    @GetMapping
    public List<UsersEntity> getAllUsers() {
        return usersService.findAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsersEntity> getUserById(@PathVariable Integer id) {
        Optional<UsersEntity> user = usersService.findUserById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    public UsersEntity createUser(@RequestBody UsersEntity user) {
        return usersService.saveUser(user);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<UsersEntity> updateUser(@PathVariable Integer id, @RequestBody UsersEntity userDetails) {
        Optional<UsersEntity> userOptional = usersService.findUserById(id);
        if (userOptional.isPresent()) {
            UsersEntity user = userOptional.get();
            user.setName(userDetails.getName());
            user.setEmail(userDetails.getEmail());
            user.setPasswordHash(userDetails.getPasswordHash());
            user.setPhone(userDetails.getPhone());
            user.setAddress(userDetails.getAddress());
            UsersEntity updatedUser = usersService.saveUser(user);
            return ResponseEntity.ok(updatedUser);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        Optional<UsersEntity> userOptional = usersService.findUserById(id);
        if (userOptional.isPresent()) {
            usersService.deleteUser(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
