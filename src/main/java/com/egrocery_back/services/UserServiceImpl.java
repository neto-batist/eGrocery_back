package com.egrocery_back.services;

import com.egrocery_back.dto.OrderDTO;
import com.egrocery_back.dto.UserDTO;
import com.egrocery_back.dto.UserFilterDTO;
import com.egrocery_back.errors.NotFound;
import com.egrocery_back.errors.WrongPassword;
import com.egrocery_back.models.OrdersEntity;
import com.egrocery_back.models.UsersEntity;
import com.egrocery_back.repositories.UsersRepository;
import com.egrocery_back.specifications.UserSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UsersRepository userRepository;

    private final CartServiceImpl cartService;
    private final OrderServiceImpl orderService;

    public UserServiceImpl(UsersRepository userRepository, CartServiceImpl cartService, OrderServiceImpl orderService) {
        this.userRepository = userRepository;
        this.cartService = cartService;
        this.orderService = orderService;
    }

    @Override
    public UserDTO saveUser(UserDTO dto) throws NotFound {
        UsersEntity user = mapToEntity(dto);
        user.setCreatedAt(Timestamp.from(Instant.now()));
        if ((user.getId() != null) && (user.getId() == 0)) {
            user.setId(null);
        }
        UsersEntity saved = userRepository.save(user);
        return mapToDTO(saved);
    }

    @Override
    public Page<UserDTO> getAllUsers(UserFilterDTO filterDTO, Pageable pageable) {
        return userRepository.findAll(UserSpecification.filter(filterDTO), pageable)
                .map(this::mapToDTO);
    }

    @Override
    public UserDTO getUserById(Integer id) throws NotFound {
        UsersEntity user = findUserById(id);
        return mapToDTO(user);
    }

    @Override
    public UserDTO updateUser(Integer id, UserDTO dto) throws NotFound {
        UsersEntity existing = findUserById(id);

        // Atualiza apenas os campos que podem mudar
        if (dto.getName() != null) existing.setName(dto.getName());
        if (dto.getEmail() != null) existing.setEmail(dto.getEmail());
        if (dto.getPassword() != null) existing.setPasswordHash(dto.getPassword());
        if (dto.getPhone() != null) existing.setPhone(dto.getPhone());
        if (dto.getAddress() != null) existing.setAddress(dto.getAddress());

        existing.setUpdatedAt(Timestamp.from(Instant.now()));

        // Atualização opcional das relações (se for permitido)
        if (dto.getCarts() != null && !dto.getCarts().isEmpty()) {
            existing.setCartsById(
                    dto.getCarts().stream()
                            .map(cartService::mapToEntity)
                            .collect(Collectors.toList())
            );
        }

        if (dto.getOrders() != null && !dto.getOrders().isEmpty()) {
            List<OrdersEntity> list = new ArrayList<>();
            for (OrderDTO orderDTO : dto.getOrders()) {
                OrdersEntity ordersEntity = orderService.mapToEntity(orderDTO);
                list.add(ordersEntity);
            }
            existing.setOrdersById(list);
        }

        userRepository.save(existing);
        return mapToDTO(existing);
    }

    @Override
    public void deleteUser(Integer id) throws NotFound {
        UsersEntity user = findUserById(id);
        userRepository.delete(user);
    }

    @Override
    public UserDTO login(UserFilterDTO filterDTO, UserDTO userDTO) throws NotFound, WrongPassword {
        UsersEntity userEntity = findUserByEmail(userDTO.getEmail());

        if (! userEntity.getPasswordHash().equals(userDTO.getPassword()) ){
               throw new WrongPassword();
        }

        return mapToDTO(userEntity);
    }

    public UsersEntity mapToEntity(UserDTO dto) throws NotFound {
        UsersEntity entity = new UsersEntity();

        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setEmail(dto.getEmail());
        entity.setPasswordHash(dto.getPassword());
        entity.setPhone(dto.getPhone());
        entity.setAddress(dto.getAddress());
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setUpdatedAt(dto.getUpdatedAt());

        if (dto.getCarts() != null && !dto.getCarts().isEmpty()) {
            entity.setCartsById(
                    dto.getCarts().stream()
                            .map(cartService::mapToEntity)
                            .collect(Collectors.toList())
            );
        }

        if (dto.getOrders() != null && !dto.getOrders().isEmpty()) {
            List<OrdersEntity> list = new ArrayList<>();
            for (OrderDTO orderDTO : dto.getOrders()) {
                OrdersEntity ordersEntity = orderService.mapToEntity(orderDTO);
                list.add(ordersEntity);
            }
            entity.setOrdersById(list);
        }

        return entity;
    }

    public UserDTO mapToDTO(UsersEntity entity) {
        UserDTO dto = new UserDTO();

        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setEmail(entity.getEmail());
        dto.setPassword(entity.getPasswordHash());
        dto.setPhone(entity.getPhone());
        dto.setAddress(entity.getAddress());
        dto.setUpdatedAt(entity.getUpdatedAt());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());

        if (entity.getCartsById() != null && !entity.getCartsById().isEmpty()) {
            dto.setCarts(
                    entity.getCartsById().stream()
                            .map(cartService::mapToDTO)
                            .collect(Collectors.toList())
            );
        }

        if (entity.getOrdersById() != null && !entity.getOrdersById().isEmpty()) {
            dto.setOrders(
                    entity.getOrdersById().stream()
                            .map(orderService::mapToDTO)
                            .collect(Collectors.toList())
            );
        }

        return dto;
    }

    private UsersEntity findUserById(Integer id) throws NotFound {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFound("Usuário", id));
    }
    private UsersEntity findUserByEmail(String  email) throws NotFound {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFound("Usuário with email: "+email, 0));
    }

}
