package com.MercureIT.HR_Manager.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.MercureIT.HR_Manager.services.ClientService;
import com.MercureIT.HR_Manager.services.UserService;
import com.MercureIT.HR_Manager.services.EmployeeService;

@Controller
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/admin/dashboard")
    public String dashboard(Model model) {
        Integer totalUsers = userService.countUsers();
        Integer totalClients = clientService.countClients();
        Integer totalEmployees = employeeService.countEmployees();

        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("totalClients", totalClients);
        model.addAttribute("totalEmployees", totalEmployees);

        return "admin";  // Renders admin.html
    }
}
