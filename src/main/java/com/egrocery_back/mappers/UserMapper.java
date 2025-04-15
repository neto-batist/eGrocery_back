package com.egrocery_back.mappers;

import com.egrocery_back.dto.UserDTO;
import com.egrocery_back.models.UsersEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDTO toDTO(UsersEntity entity) {
        if (entity == null) return null;

        UserDTO dto = new UserDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setEmail(entity.getEmail());
        dto.setPassword(entity.getPasswordHash());
        return dto;
    }

    public UsersEntity toEntity(UserDTO dto) {
        if (dto == null) return null;

        UsersEntity entity = new UsersEntity();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setEmail(dto.getEmail());
        entity.setPasswordHash(dto.getPassword());
        return entity;
    }
}
