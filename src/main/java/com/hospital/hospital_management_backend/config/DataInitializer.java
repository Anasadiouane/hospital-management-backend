package com.hospital.hospital_management_backend.config;

import com.hospital.hospital_management_backend.entity.Role;
import com.hospital.hospital_management_backend.entity.User;
import com.hospital.hospital_management_backend.enums.ROLES;
import com.hospital.hospital_management_backend.repository.RoleRepository;
import com.hospital.hospital_management_backend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * Creates the 5 roles and the first admin user on an empty database.
 * Safe to run on every start: it only inserts what is missing.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${ADMIN_CIN:}")
    private String adminCin;

    @Value("${ADMIN_PASSWORD:}")
    private String adminPassword;

    public DataInitializer(RoleRepository roleRepository,
                           UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        for (ROLES r : ROLES.values()) {
            if (!roleRepository.existsByName(r.name())) {
                Role role = new Role();
                role.setName(r.name());
                roleRepository.save(role);
                log.info("Role created: {}", r.name());
            }
        }

        if (!StringUtils.hasText(adminCin) || !StringUtils.hasText(adminPassword)) {
            log.warn("ADMIN_CIN / ADMIN_PASSWORD not set: no admin user created");
            return;
        }
        if (userRepository.existsByCin(adminCin)) {
            return;
        }

        Role adminRole = roleRepository.findByName(ROLES.ROLE_ADMIN.name()).orElseThrow();

        User admin = new User();
        admin.setFirstName("Admin");
        admin.setLastName("Admin");
        admin.setCin(adminCin);
        admin.setEmail(adminCin.toLowerCase() + "@hospital.local");
        admin.setPassword(passwordEncoder.encode(adminPassword));
        admin.setRole(adminRole);
        userRepository.save(admin);

        log.info("Admin user created with CIN {}", adminCin);
    }
}
