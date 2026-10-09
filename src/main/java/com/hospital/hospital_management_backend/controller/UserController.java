package com.hospital.hospital_management_backend.controller;

import com.hospital.hospital_management_backend.dto.request.ChangePasswordDto;
import com.hospital.hospital_management_backend.dto.request.ImageToUserDtoRequest;
import com.hospital.hospital_management_backend.dto.request.UpdateUserPasswordDto;
import com.hospital.hospital_management_backend.dto.request.UserDtoRequest;
import com.hospital.hospital_management_backend.dto.request.UserUpdateDtoRequest;
import com.hospital.hospital_management_backend.dto.response.UserDtoResponse;
import com.hospital.hospital_management_backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserDtoResponse> createUser(@Valid @RequestBody UserDtoRequest dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(dto));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDtoResponse> getUserById(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @GetMapping("/cin/{cin}")
    public ResponseEntity<UserDtoResponse> getUserByCin(@PathVariable String cin) {
        return ResponseEntity.ok(userService.getUserByCin(cin));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserDtoResponse> getUserByEmail(@PathVariable String email) {
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

    @GetMapping
    public ResponseEntity<List<UserDtoResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserDtoResponse> updateUser(@PathVariable Long userId,
                                                      @Valid @RequestBody UserUpdateDtoRequest dto) {
        return ResponseEntity.ok(userService.updateUser(dto, userId));
    }

    /** Password reset by user id. */
    @PatchMapping("/{userId}/password")
    public ResponseEntity<UserDtoResponse> updatePassword(@PathVariable Long userId,
                                                          @Valid @RequestBody ChangePasswordDto dto) {
        return ResponseEntity.ok(userService.updatePassword(dto, userId));
    }

    /** Password change by the logged-in user (identified by CIN, requires old password). */
    @PatchMapping("/cin/{cin}/password")
    public ResponseEntity<String> updatePasswordByCin(@PathVariable String cin,
                                                      @Valid @RequestBody UpdateUserPasswordDto dto) {
        return ResponseEntity.ok(userService.updatePassword(cin, dto));
    }

    @PutMapping("/{userId}/image")
    public ResponseEntity<UserDtoResponse> addImageToUser(@PathVariable Long userId,
                                                          @Valid @RequestBody ImageToUserDtoRequest dto) {
        return ResponseEntity.ok(userService.addImageToUser(userId, dto.imageId()));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}
