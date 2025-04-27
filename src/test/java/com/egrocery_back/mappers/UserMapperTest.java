package com.egrocery_back.mappers;

import com.egrocery_back.dto.UserDTO;
import com.egrocery_back.models.UsersEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class UserMapperTest {

    private final UserMapper userMapper = new UserMapper();

    @Test
    void toDTO_ValidEntity_ReturnsCorrectDTO() {
        UsersEntity entity = new UsersEntity();
        entity.setId(1);
        entity.setName("João Silva");
        entity.setEmail("joao.silva@example.com");
        entity.setPasswordHash("hashedPassword");

        UserDTO dto = userMapper.toDTO(entity);

        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getName(), dto.getName());
        assertEquals(entity.getEmail(), dto.getEmail());
        assertEquals(entity.getPasswordHash(), dto.getPassword());
    }

    @Test
    void toDTO_NullEntity_ReturnsNullDTO() {
        UserDTO dto = userMapper.toDTO(null);
        assertNull(dto);
    }

    @Test
    void toEntity_ValidDTO_ReturnsCorrectEntity() {
        UserDTO dto = new UserDTO();
        dto.setId(2);
        dto.setName("Maria Souza");
        dto.setEmail("maria.souza@example.com");
        dto.setPassword("plainPassword");

        UsersEntity entity = userMapper.toEntity(dto);

        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.getName(), entity.getName());
        assertEquals(dto.getEmail(), entity.getEmail());
        assertEquals(dto.getPassword(), entity.getPasswordHash());
    }

    @Test
    void toEntity_NullDTO_ReturnsNullEntity() {
        UsersEntity entity = userMapper.toEntity(null);
        assertNull(entity);
    }
}