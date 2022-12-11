package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;


import java.security.Principal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserServiceImpl userService;

    @Autowired
    public AdminController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping()
    public String getAllUsers(Model model, Principal principal) {
        model.addAttribute("users", userService.listUsers());
        model.addAttribute("userCurrent", userService.loadUserByUsername(principal.getName()));
        model.addAttribute("listRoles", userService.listRoles());
        model.addAttribute("newUser", new User());
        return "users";
    }

//    @GetMapping("/user")
//    public String getUser(Model model, Principal principal) {
//        UserDetails user = userService.loadUserByUsername(principal.getName());
//        model.addAttribute("userCurrent", user);
//        model.addAttribute("listRoles", userService.listRoles());
//        return "userAdmin";
//    }

    @PostMapping("/new")
    public String create(@ModelAttribute("user") User user) {
        userService.add(user);
        return "redirect:/admin";
    }
    @PostMapping("edit/{id}")
    public String edit(@ModelAttribute("user") User user) {
        userService.update(user);
        return "redirect:/admin";
    }

    @PostMapping("delete/{id}")
    public String deleteUser(@PathVariable("id") int id) {
        userService.delete(id);
        return "redirect:/admin";
    }
}
