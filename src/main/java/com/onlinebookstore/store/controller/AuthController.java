package com.onlinebookstore.store.controller;

import com.onlinebookstore.store.dto.UserLoginRequestDto;
import com.onlinebookstore.store.dto.UserLoginResponseDto;
import com.onlinebookstore.store.dto.UserRegistrationRequestDto;
import com.onlinebookstore.store.dto.UserResponseDto;
import com.onlinebookstore.store.security.AuthenticationService;
import com.onlinebookstore.store.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication management", description = "Manages authentication controller")
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @PostMapping("/registration")
    @Operation(description = "Registers a user")
    public UserResponseDto register(
            @RequestBody @Valid UserRegistrationRequestDto requestDto) {
        return userService.register(requestDto);
    }

    @PostMapping("/login")
    @Operation(description = "Logs in a user")
    public UserLoginResponseDto login(@RequestBody @Valid UserLoginRequestDto request) {
        return authenticationService.authenticate(request);
    }
}
