package ru.itmentor.spring.boot_security.demo.configs;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.itmentor.spring.boot_security.demo.model.Role;
import ru.itmentor.spring.boot_security.demo.model.User;
import ru.itmentor.spring.boot_security.demo.service.RoleService;
import ru.itmentor.spring.boot_security.demo.service.UserService;

import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final UserService userService;
    private final RoleService roleService;

    @Override
    public void run(String... args) throws Exception {
        // Проверяем и создаем роли (если их нет)
        Role adminRole = createRoleIfNotExists("ROLE_ADMIN");
        Role userRole = createRoleIfNotExists("ROLE_USER");

        // Создаем администратора (если еще не существует)
        createUserIfNotExists(
                "admin@mail.com",
                "Admin",
                30,
                "admin",
                Set.of(adminRole, userRole)
        );

        // Создаем обычного пользователя (если еще не существует)
        createUserIfNotExists(
                "user@mail.com",
                "User",
                25,
                "user",
                Set.of(userRole)
        );
    }

    private Role createRoleIfNotExists(String roleName) {
        Optional<Role> existingRole = roleService.findByName(roleName);
        return existingRole.orElseGet(() -> {
            Role newRole = new Role();
            newRole.setName(roleName);
            return roleService.save(newRole);
        });
    }

    private void createUserIfNotExists(String email, String name,
                                       int age, String password,
                                       Set<Role> roles) {
        try {
            userService.findByEmail(email);
        } catch (RuntimeException e) {
            User newUser = new User();
            newUser.setName(name);
            newUser.setAge(age);
            newUser.setEmail(email);
            newUser.setPassword(password);
            newUser.setRoles(roles);
            userService.save(newUser);
        }
    }
}