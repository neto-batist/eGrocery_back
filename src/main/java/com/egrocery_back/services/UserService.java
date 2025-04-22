package com.egrocery_back.services;

import com.egrocery_back.dto.UserDTO;
import com.egrocery_back.dto.UserFilterDTO;
import com.egrocery_back.errors.NotFound;
import com.egrocery_back.errors.WrongPassword;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    UserDTO saveUser(UserDTO dto) throws NotFound;
    Page<UserDTO> getAllUsers(UserFilterDTO filterDTO,Pageable pageable);
    UserDTO getUserById(Integer id) throws NotFound;
    UserDTO updateUser(Integer id, UserDTO dto) throws NotFound;

    UserDTO login(UserFilterDTO filterDTO, UserDTO userDTO) throws NotFound, WrongPassword;
    void deleteUser(Integer id) throws NotFound;
}
