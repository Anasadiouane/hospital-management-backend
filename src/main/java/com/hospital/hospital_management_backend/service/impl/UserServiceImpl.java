package com.hospital.hospital_management_backend.service.impl;

import com.hospital.hospital_management_backend.dto.request.*;
import com.hospital.hospital_management_backend.dto.response.UserDtoResponse;
import com.hospital.hospital_management_backend.entity.File;
import com.hospital.hospital_management_backend.entity.Role;
import com.hospital.hospital_management_backend.entity.User;
import com.hospital.hospital_management_backend.enums.ROLES;
import com.hospital.hospital_management_backend.exception.AppException;
import com.hospital.hospital_management_backend.exception.ResourceNotFoundException;
import com.hospital.hospital_management_backend.mapper.UserMapper;
import com.hospital.hospital_management_backend.repository.FileRepository;
import com.hospital.hospital_management_backend.repository.RoleRepository;
import com.hospital.hospital_management_backend.repository.UserRepository;
import com.hospital.hospital_management_backend.auth.AuthenticationFacade;
import com.hospital.hospital_management_backend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service implementation for managing users.
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationFacade authenticationFacade;
    private final FileRepository fileRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder,
                           AuthenticationFacade authenticationFacade,
                           FileRepository fileRepository,
                           UserMapper userMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationFacade = authenticationFacade;
        this.fileRepository = fileRepository;
        this.userMapper = userMapper;
    }

    @Override
    @Transactional
    public UserDtoResponse createUser(UserDtoRequest userDtoRequest) {
        if (userRepository.existsByCin(userDtoRequest.cin()) || userRepository.existsByEmail(userDtoRequest.email())) {
            throw new AppException(HttpStatus.BAD_REQUEST, "User already exists");
        }

        User user = userMapper.userDtoRequestToUser(userDtoRequest);
        user.setPassword(passwordEncoder.encode(userDtoRequest.password()));

        addRoleToUser(user, userDtoRequest.role().name());

        User savedUser = userRepository.save(user);
        return userMapper.userToUserDtoResponse(savedUser);
    }

    @Override
    public UserDtoResponse getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId.toString()));
        return userMapper.userToUserDtoResponse(user);
    }

    @Override
    public UserDtoResponse getUserByCin(String cin) {
        User user = userRepository.findByCin(cin)
                .orElseThrow(() -> new ResourceNotFoundException("User", "cin", cin));
        return userMapper.userToUserDtoResponse(user);
    }

    @Override
    public UserDtoResponse getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
        return userMapper.userToUserDtoResponse(user);
    }

    @Override
    public List<UserDtoResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::userToUserDtoResponse)
                .collect(Collectors.toList());
    }

    @Override
    public UserDtoResponse updateUser(UserUpdateDtoRequest userDtoRequest, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId.toString()));

        if (StringUtils.hasText(userDtoRequest.firstName())) {
            user.setFirstName(userDtoRequest.firstName());
        }
        if (StringUtils.hasText(userDtoRequest.lastName())) {
            user.setLastName(userDtoRequest.lastName());
        }
        if (StringUtils.hasText(userDtoRequest.cin())) {
            user.setCin(userDtoRequest.cin());
        }
        if (StringUtils.hasText(userDtoRequest.email())) {
            user.setEmail(userDtoRequest.email());
        }
        if (userDtoRequest.role() != null && StringUtils.hasText(userDtoRequest.role().name())) {
            addRoleToUser(user, userDtoRequest.role().name());
        }

        User updatedUser = userRepository.save(user);
        return userMapper.userToUserDtoResponse(updatedUser);
    }

    @Override
    @Transactional
    public UserDtoResponse updatePassword(ChangePasswordDto passwordDto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId.toString()));

        if (isAdmin())
            throw new AppException(HttpStatus.UNAUTHORIZED, "Invalid credential");

        if (!passwordDto.newPassword().equals(passwordDto.confirmNewPassword()))
            throw new AppException(HttpStatus.BAD_REQUEST, "Invalid input");

        user.setPassword(passwordEncoder.encode(passwordDto.newPassword()));
        User updatedUser = userRepository.save(user);

        return userMapper.userToUserDtoResponse(updatedUser);
    }

    @Override
    @Transactional
    public String updatePassword(String cin, UpdateUserPasswordDto updateUserPasswordDto) {
        User user = userRepository.findByCin(cin)
                .orElseThrow(() -> new ResourceNotFoundException("User", "cin", cin));

        if (!authenticationFacade.getAuthentication().getName().equals(user.getCin())) {
            throw new AppException(HttpStatus.UNAUTHORIZED, "Invalid credential");
        }

        if (!passwordEncoder.matches(updateUserPasswordDto.oldPassword(), user.getPassword())) {
            throw new BadCredentialsException("Incorrect old password");
        }

        if (updateUserPasswordDto.oldPassword().equals(updateUserPasswordDto.newPassword())) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Password should not match the old one");
        }

        if (!updateUserPasswordDto.newPassword().equals(updateUserPasswordDto.confirmNewPassword())) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Passwords should be the same");
        }

        user.setPassword(passwordEncoder.encode(updateUserPasswordDto.newPassword()));
        userRepository.save(user);

        return "Password changed successfully";
    }

    @Override
    @Transactional
    public UserDtoResponse addImageToUser(Long userId, Long imageId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId.toString()));

        String loggedUserCin = authenticationFacade.getAuthentication().getName();
        if (!user.getCin().equals(loggedUserCin)) {
            throw new AppException(HttpStatus.UNAUTHORIZED, "Invalid credential");
        }

        File image = fileRepository.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("Image", "id", imageId.toString()));

        user.setImage(image);

        User updatedUser = userRepository.save(user);
        return userMapper.userToUserDtoResponse(updatedUser);
    }

    @Override
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId.toString()));
        userRepository.delete(user);
    }

    private void addRoleToUser(User user, String roleName) {
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "name", roleName));

        if (role.getName().equals(ROLES.ROLE_ADMIN.name()) && isAdmin()) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Don't have permission to create admin user");
        }

        user.setRole(role);
    }

    private boolean isAdmin() {
        String cin = authenticationFacade.getAuthentication().getName();
        User user = userRepository.findByCin(cin)
                .orElseThrow(() -> new ResourceNotFoundException("User", "cin", cin));
        return !user.getRole().getName().equals(ROLES.ROLE_ADMIN.name());
    }
}