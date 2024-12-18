package com.onlinebookstore.store.service.impl;

import com.onlinebookstore.store.dto.UserRegistrationRequestDto;
import com.onlinebookstore.store.dto.UserResponseDto;
import com.onlinebookstore.store.exceptions.EntityNotFoundException;
import com.onlinebookstore.store.exceptions.RegistrationException;
import com.onlinebookstore.store.mapper.UserMapper;
import com.onlinebookstore.store.model.Role;
import com.onlinebookstore.store.model.User;
import com.onlinebookstore.store.repository.RoleRepository;
import com.onlinebookstore.store.repository.UserRepository;
import com.onlinebookstore.store.service.ShoppingCartService;
import com.onlinebookstore.store.service.UserService;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final ShoppingCartService shoppingCartService;

    @Override
    @Transactional
    public UserResponseDto register(UserRegistrationRequestDto requestDto) {
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new RegistrationException("Unable to complete registration: email "
                + requestDto.getEmail() + " is already in use.");
        }
        User user = userMapper.toModel(requestDto);
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        Role role = roleRepository
                .findByName(Role.RoleName.USER)
                .orElseThrow(
                    () -> new EntityNotFoundException("Can't find role by name"));
        user.setRoles(Set.of(role));
        userRepository.save(user);
        shoppingCartService.createNewShoppingCart(user);
        return userMapper.toDto(user);
    }
}
