package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;


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

    @GetMapping
    public String home(Model model) {
        model.addAttribute("users", userService.listUsers());
        return "index";
    }

    //------------------------------------------------------------------------------------------------------------------
    @GetMapping(value = "/users")
    public String getAllUsers(Model model) {
        model.addAttribute("users", userService.listUsers());
        return "users";
    }

    @GetMapping("/{id}")
    public String getUser(@PathVariable("id") int id, Model model) {
        model.addAttribute("user", userService.findById(id));
        return "user";
    }

    //------------------------------------------------------------------------------------------------------------------

    @GetMapping(value = "/new")
    public String newUser(@ModelAttribute("user") User user, Model model) {
        model.addAttribute("listRoles", userService.listRoles());
        return "new";
    }
    @PostMapping("/new")
    public String create(@ModelAttribute("user") User user) {
        List<String> listS = user.getRoles().stream().map(r -> r.getRole()).collect(Collectors.toList());
        Set<Role> listR = userService.listByRole(listS);
        user.setRoles(listR);
        userService.add(user);
        return "redirect:/admin/users";
    }
    //------------------------------------------------------------------------------------------------------------------
    @GetMapping("edit/{id}")
    public String edit(@PathVariable("id") int id, Model model) {
        User user = userService.findById(id);
        model.addAttribute("user", userService.findByUsername(userService.findById(id).getUsername()));
        model.addAttribute("listRoles", userService.listRoles());
        return "edit";
    }

    @PostMapping("edit")
    public String update(@ModelAttribute("user") User user) {
//        if (bindingResult.hasErrors()) {
//            return "edit";
//        }
        List<String> listS = user.getRoles().stream().map(r -> r.getRole()).collect(Collectors.toList());
        Set<Role> listR = userService.listByRole(listS);
        user.setRoles(listR);
        userService.update(user);
        return "redirect:/admin/users";
    }

    //------------------------------------------------------------------------------------------------------------------
    @GetMapping("delete/{id}")
    public String deleteUser(@PathVariable("id") int id) {
        userService.delete(id);
        return "redirect:/admin/users";
    }
}
