package ru.itmentor.spring.boot_security.demo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itmentor.spring.boot_security.demo.model.Role;
import ru.itmentor.spring.boot_security.demo.model.User;
import ru.itmentor.spring.boot_security.demo.repository.RoleRepository;
import ru.itmentor.spring.boot_security.demo.repository.UserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    @Override
    public User save(User user) {

        if (user.getId() == null) {
            if (user.getPassword() == null || user.getPassword().isEmpty()) {
                throw new RuntimeException("Password is required for new user");
            }
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {

            User existingUser = findById(user.getId());


            if (user.getPassword() == null || user.getPassword().isEmpty()) {
                user.setPassword(existingUser.getPassword());
            } else {

                if (!passwordEncoder.matches(user.getPassword(), existingUser.getPassword())) {
                    user.setPassword(passwordEncoder.encode(user.getPassword()));
                }
            }
        }


        Set<Role> managedRoles = new HashSet<>();
        if (user.getRoles() != null && !user.getRoles().isEmpty()) {
            for (Role role : user.getRoles()) {
                managedRoles.add(roleRepository.findById(role.getId())
                        .orElseThrow(() -> new RuntimeException("Role not found with id: " + role.getId())));
            }
        } else {
            throw new RuntimeException("At least one role must be selected");
        }
        user.setRoles(managedRoles);

        return userRepository.save(user);
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }
}