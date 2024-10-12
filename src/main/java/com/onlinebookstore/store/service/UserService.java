package com.onlinebookstore.store.service;

import com.onlinebookstore.store.dto.UserRegistrationRequestDto;
import com.onlinebookstore.store.dto.UserResponseDto;
import com.onlinebookstore.store.exceptions.RegistrationException;

public interface UserService {

    UserResponseDto register(UserRegistrationRequestDto
                                 userRegistrationRequestDto)
            throws RegistrationException;
}
