package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;
import java.security.Principal;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService service;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserService service, RoleService roleService) {
        this.service = service;
        this.roleService = roleService;
    }

    @GetMapping
    public String printAll(Model model, Principal principal) {
        model.addAttribute("users", service.getAllUsers());
        model.addAttribute("email", ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEmail());
        model.addAttribute("user", (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        model.addAttribute("allRoles", roleService.getAllRoles());
        return "admin";
    }

    @PostMapping("/")
    public String add(@ModelAttribute("user") User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin";
        }
        service.save(user);
        return "redirect:/admin";
    }

    @PostMapping("/edit")
    public String edit(@ModelAttribute("user") User user, @RequestParam("id") Long id) {
        service.updateUser(user, id);
        return "redirect:/admin";
    }

    @PostMapping("/delete")
    public String remove(@RequestParam("id") Long id) {
        service.removeUserById(id);
        return "redirect:/admin";
    }
}