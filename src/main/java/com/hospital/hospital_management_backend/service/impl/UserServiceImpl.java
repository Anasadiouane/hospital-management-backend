package com.hospital.hospital_management_backend.service.impl;

import com.hospital.hospital_management_backend.auth.AuthenticationFacade;
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
import com.hospital.hospital_management_backend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

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

    // =========================
    // CREATE
    // =========================
    @Override
    @Transactional
    public UserDtoResponse createUser(UserDtoRequest dto) {
        if (userRepository.existsByCin(dto.cin()) || userRepository.existsByEmail(dto.email())) {
            throw new AppException(HttpStatus.CONFLICT, "User already exists");
        }

        User user = userMapper.userDtoRequestToUser(dto);
        user.setPassword(passwordEncoder.encode(dto.password()));

        addRoleToUser(user, dto.role().name());

        return userMapper.userToUserDtoResponse(userRepository.save(user));
    }

    // =========================
    // READ
    // =========================
    @Override
    @Transactional(readOnly = true)
    public UserDtoResponse getUserById(Long userId) {
        return userMapper.userToUserDtoResponse(findUser(userId));
    }

    @Override
    @Transactional(readOnly = true)
    public UserDtoResponse getUserByCin(String cin) {
        User user = userRepository.findByCin(cin)
                .orElseThrow(() -> new ResourceNotFoundException("User", "cin", cin));
        return userMapper.userToUserDtoResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDtoResponse getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
        return userMapper.userToUserDtoResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDtoResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::userToUserDtoResponse)
                .toList();
    }

    // =========================
    // UPDATE
    // =========================
    @Override
    @Transactional
    public UserDtoResponse updateUser(UserUpdateDtoRequest dto, Long userId) {
        User user = findUser(userId);

        if (StringUtils.hasText(dto.firstName())) {
            user.setFirstName(dto.firstName());
        }
        if (StringUtils.hasText(dto.lastName())) {
            user.setLastName(dto.lastName());
        }
        if (StringUtils.hasText(dto.cin()) && !dto.cin().equals(user.getCin())) {
            if (userRepository.existsByCin(dto.cin())) {
                throw new AppException(HttpStatus.CONFLICT, "CIN already exists");
            }
            user.setCin(dto.cin());
        }
        if (StringUtils.hasText(dto.email()) && !dto.email().equals(user.getEmail())) {
            if (userRepository.existsByEmail(dto.email())) {
                throw new AppException(HttpStatus.CONFLICT, "Email already exists");
            }
            user.setEmail(dto.email());
        }
        if (dto.role() != null && StringUtils.hasText(dto.role().name())) {
            addRoleToUser(user, dto.role().name());
        }

        return userMapper.userToUserDtoResponse(userRepository.save(user));
    }

    /** Password reset by an ADMIN (no old password needed). */
    @Override
    @Transactional
    public UserDtoResponse updatePassword(ChangePasswordDto passwordDto, Long userId) {
        User user = findUser(userId);

        if (isNotAdmin()) {
            throw new AppException(HttpStatus.FORBIDDEN, "Only an admin can reset a password");
        }

        if (!passwordDto.newPassword().equals(passwordDto.confirmNewPassword())) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Passwords should be the same");
        }

        user.setPassword(passwordEncoder.encode(passwordDto.newPassword()));
        return userMapper.userToUserDtoResponse(userRepository.save(user));
    }

    /** Password change by the logged-in user. */
    @Override
    @Transactional
    public String updatePassword(String cin, UpdateUserPasswordDto dto) {
        User user = userRepository.findByCin(cin)
                .orElseThrow(() -> new ResourceNotFoundException("User", "cin", cin));

        if (!authenticationFacade.getAuthentication().getName().equals(user.getCin())) {
            throw new AppException(HttpStatus.FORBIDDEN, "You can only change your own password");
        }

        if (!passwordEncoder.matches(dto.oldPassword(), user.getPassword())) {
            throw new BadCredentialsException("Incorrect old password");
        }

        if (dto.oldPassword().equals(dto.newPassword())) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Password should not match the old one");
        }

        if (!dto.newPassword().equals(dto.confirmNewPassword())) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Passwords should be the same");
        }

        user.setPassword(passwordEncoder.encode(dto.newPassword()));
        userRepository.save(user);

        return "Password changed successfully";
    }

    @Override
    @Transactional
    public UserDtoResponse addImageToUser(Long userId, Long imageId) {
        User user = findUser(userId);

        String loggedUserCin = authenticationFacade.getAuthentication().getName();
        if (!user.getCin().equals(loggedUserCin)) {
            throw new AppException(HttpStatus.FORBIDDEN, "You can only change your own image");
        }

        File image = fileRepository.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("Image", "id", imageId.toString()));

        user.setImage(image);

        return userMapper.userToUserDtoResponse(userRepository.save(user));
    }

    // =========================
    // DELETE
    // =========================
    @Override
    @Transactional
    public void deleteUser(Long userId) {
        userRepository.delete(findUser(userId));
    }

    // =========================
    // HELPERS
    // =========================
    private User findUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id.toString()));
    }

    private void addRoleToUser(User user, String roleName) {
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "name", roleName));

        // only an admin can assign the ADMIN role
        if (role.getName().equals(ROLES.ROLE_ADMIN.name()) && isNotAdmin()) {
            throw new AppException(HttpStatus.FORBIDDEN, "Don't have permission to create admin user");
        }

        user.setRole(role);
    }

    /** true if the currently logged-in user is NOT an admin. */
    private boolean isNotAdmin() {
        String cin = authenticationFacade.getAuthentication().getName();
        User user = userRepository.findByCin(cin)
                .orElseThrow(() -> new ResourceNotFoundException("User", "cin", cin));
        return !user.getRole().getName().equals(ROLES.ROLE_ADMIN.name());
    }
}
