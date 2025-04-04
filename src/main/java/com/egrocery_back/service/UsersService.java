package com.egrocery_back.service;


import com.egrocery_back.models.UsersEntity;
import com.egrocery_back.repositorys.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsersService {

    @Autowired
    private UsersRepository usersRepository;

    public List<UsersEntity> findAllUsers() {
        return usersRepository.findAll();
    }

    public Optional<UsersEntity> findUserById(Integer id) {
        return usersRepository.findById(id);
    }

    public UsersEntity saveUser(UsersEntity user) {
        return usersRepository.save(user);
    }

    public void deleteUser(Integer id) {
        usersRepository.deleteById(id);
    }
}

