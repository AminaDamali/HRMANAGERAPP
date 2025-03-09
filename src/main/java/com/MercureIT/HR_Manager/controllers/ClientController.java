package com.MercureIT.HR_Manager.controllers;

import com.MercureIT.HR_Manager.models.Client;
import com.MercureIT.HR_Manager.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;

    // Show all clients
    @GetMapping
    public String getClients(Model model) {
        List<Client> clients = clientService.getAllClients();
        model.addAttribute("clients", clients);
        return "clients"; // Thymeleaf template to display clients
    }

    // Add a new client
    @GetMapping("/add")
    public String showAddClientPage(Model model) {
        model.addAttribute("client", new Client());
        return "clientAdd"; // Thymeleaf template for adding client
    }

    @PostMapping("/save")
    public String saveClient(@ModelAttribute("client") Client client) {
        clientService.save(client);
        return "redirect:/clients";
    }

    // Edit client
    @GetMapping("/edit/{id}")
    public String editClient(@PathVariable Integer id, Model model) {
        Optional<Client> client = clientService.findById(id);
        if (client.isPresent()) {
            model.addAttribute("client", client.get());
            return "clientEdit"; // Thymeleaf template for editing client
        }
        return "redirect:/clients"; // Redirect if client not found
    }

    @PostMapping("/update")
    public String updateClient(@ModelAttribute("client") Client client) {
        clientService.update(client);
        return "redirect:/clients";
    }

    @GetMapping("/clientEdit/{id}")
    public String showEditForm(@PathVariable("id") Integer id, Model model) {
        System.out.println("Editing Client ID: " + id);  // Add logging for debugging
        Client client = clientService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid client ID: " + id));
        model.addAttribute("client", client);
        return "clientEdit";
    }


    @PostMapping("/clientEdit/{id}")
    public String updateClient(@PathVariable("id") Integer id, @ModelAttribute("client") Client client) {
        clientService.updateClient(id, client); // Update the client
        return "redirect:/clients"; // Redirect back to the client list
    }


    @PostMapping("/delete")
    public String delete(@RequestParam Integer id) {
        clientService.delete(id);
        return "redirect:/clients";
    }
    // Search client
    @GetMapping("/search")
    public String searchClients(@RequestParam String keyword, Model model) {
        List<Client> clients = clientService.searchClients(keyword);
        model.addAttribute("clients", clients);
        return "clients"; // Thymeleaf template to show search results
    }
}
