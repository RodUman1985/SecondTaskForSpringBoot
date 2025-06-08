package ru.itmentor.spring.boot_security.demo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.itmentor.spring.boot_security.demo.model.User;
import ru.itmentor.spring.boot_security.demo.service.RoleService;
import ru.itmentor.spring.boot_security.demo.service.UserService;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {
    private final UserService userService;
    private final RoleService roleService;

    @GetMapping("/users")
    public String listUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        return "admin/list";
    }

    @GetMapping("/users/new")
    public String showCreateForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("allRoles", roleService.findAll());
        return "admin/form";
    }

    @PostMapping("/users")
    public String saveUser(@Valid @ModelAttribute("user") User user, BindingResult result,
                           @RequestParam(value = "roleIds", required = false) Long[] roleIds, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("allRoles", roleService.findAll());
            return "admin/form";
        }

        if (roleIds == null || roleIds.length == 0) {
            model.addAttribute("roleError", "At least one role must be selected");
            model.addAttribute("allRoles", roleService.findAll());
            return "admin/form";
        }

        user.setRoles(Arrays.stream(roleIds)
                .map(roleService::findById)
                .collect(Collectors.toSet()));

        userService.save(user);
        return "redirect:/admin/users";
    }

    @GetMapping("/users/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        User user = userService.findById(id);
        model.addAttribute("user", user);
        model.addAttribute("allRoles", roleService.findAll());
        return "admin/form";
    }

    @PostMapping("/users/update/{id}")
    public String updateUser(@PathVariable Long id, @Valid @ModelAttribute("user") User user,
                             BindingResult result, @RequestParam("roleIds") Long[] roleIds, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("allRoles", roleService.findAll());
            return "admin/form";
        }

        user.setRoles(Arrays.stream(roleIds)
                .map(roleService::findById)
                .collect(Collectors.toSet()));

        userService.save(user);
        return "redirect:/admin/users";
    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        return "redirect:/admin/users";
    }
}