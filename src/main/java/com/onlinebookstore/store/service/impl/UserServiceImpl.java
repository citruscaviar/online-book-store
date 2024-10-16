package com.onlinebookstore.store.service.impl;

import com.onlinebookstore.store.dto.UserRegistrationRequestDto;
import com.onlinebookstore.store.dto.UserResponseDto;
import com.onlinebookstore.store.exceptions.RegistrationException;
import com.onlinebookstore.store.mapper.UserMapper;
import com.onlinebookstore.store.model.User;
import com.onlinebookstore.store.repository.UserRepository;
import com.onlinebookstore.store.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto) {
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new RegistrationException("Unable to complete registration: email "
                + requestDto.getEmail() + " is already in use.");
        }
        User user = userMapper.toModel(requestDto);
        return userMapper.toDto(userRepository.save(user));
    }
}
