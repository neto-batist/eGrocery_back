package com.egrocery_back.services;

import com.egrocery_back.dto.UserDTO;
import com.egrocery_back.dto.UserFilterDTO;
import com.egrocery_back.errors.NotFound;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    UserDTO saveUser(UserDTO dto) throws NotFound;
    Page<UserDTO> getAllUsers(UserFilterDTO filterDTO,Pageable pageable);
    UserDTO getUserById(Integer id) throws NotFound;
    UserDTO updateUser(Integer id, UserDTO dto) throws NotFound;
    void deleteUser(Integer id) throws NotFound;
}
