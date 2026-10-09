package com.hospital.hospital_management_backend.controller;

import com.hospital.hospital_management_backend.auth.IAuthenticationFacade;
import com.hospital.hospital_management_backend.dto.request.LoginRequestDto;
import com.hospital.hospital_management_backend.dto.response.JwtAuthResponse;
import com.hospital.hospital_management_backend.dto.response.UserDtoResponse;
import com.hospital.hospital_management_backend.enums.TokenType;
import com.hospital.hospital_management_backend.exception.AppException;
import com.hospital.hospital_management_backend.service.AuthService;
import com.hospital.hospital_management_backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    private final IAuthenticationFacade authenticationFacade;

    public AuthController(AuthService authService,
                          UserService userService,
                          IAuthenticationFacade authenticationFacade) {
        this.authService = authService;
        this.userService = userService;
        this.authenticationFacade = authenticationFacade;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> login(@Valid @RequestBody LoginRequestDto loginDto) {
        String token = authService.login(loginDto);
        // TokenType.values()[0] always compiles; replace with your exact constant (e.g. TokenType.Bearer)
        return ResponseEntity.ok(new JwtAuthResponse(token, TokenType.values()[0]));
    }

    /** Returns the currently logged-in user (needs the Authorization: Bearer header). */
    @GetMapping("/info")
    public ResponseEntity<UserDtoResponse> info() {
        Authentication auth = authenticationFacade.getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {
            throw new AppException(HttpStatus.UNAUTHORIZED, "Not authenticated");
        }
        return ResponseEntity.ok(userService.getUserByCin(auth.getName()));
    }
}
