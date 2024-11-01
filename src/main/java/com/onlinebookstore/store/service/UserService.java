package com.onlinebookstore.store.service;

import com.onlinebookstore.store.dto.UserRegistrationRequestDto;
import com.onlinebookstore.store.dto.UserResponseDto;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto
                                 userRegistrationRequestDto);

}
