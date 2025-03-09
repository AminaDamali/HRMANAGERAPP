package com.MercureIT.HR_Manager.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.MercureIT.HR_Manager.models.User;
import com.MercureIT.HR_Manager.services.NewUserService;

import java.util.List;

@Controller
@RequestMapping("/newuser")
public class NewUserController {

    @Autowired
    private NewUserService userService;

    @GetMapping("/all")
    public String getUsers(Model model) {
        List<User> users = userService.getUsers();
        model.addAttribute("users", users);
        return "user-list"; // This should be the Thymeleaf template name
    }

    @GetMapping("/add")
    public String showAddUserForm(Model model) {
        model.addAttribute("user", new User());
        return "user-form"; // This should be the Thymeleaf template name
    }

    @PostMapping("/save")
    public String saveUser(@ModelAttribute("user") User user) {
        userService.saveUser(user);
        return "redirect:/newuser/all";
    }

    @GetMapping("/edit/{id}")
    public String showEditUserForm(@PathVariable("id") Integer id, Model model) {
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        return "user-form";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id) {
        userService.deleteUser(id);
        return "redirect:/newuser/all";
    }
}
