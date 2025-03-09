package com.MercureIT.HR_Manager.controllers;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import org.springframework.web.bind.annotation.ModelAttribute;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.MercureIT.HR_Manager.models.JobApplication;
import com.MercureIT.HR_Manager.models.Leave;
import com.MercureIT.HR_Manager.models.User;
import com.MercureIT.HR_Manager.services.EmployeeService;
import com.MercureIT.HR_Manager.services.JobApplicationService;
import com.MercureIT.HR_Manager.services.LeaveService;
import com.MercureIT.HR_Manager.services.RoleService;
import com.MercureIT.HR_Manager.services.UserService;

@Controller
public class UserController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private EmployeeService employeeService;
	@Autowired
	private LeaveService leaveService;
	@Autowired
	private JobApplicationService jobApplicationService;
	private UserService UserService;

	@GetMapping("/api/users")
	@ResponseBody
	public List<User> getUsers() {
		return userService.getUsers();
	}
	@GetMapping("/user")
	public String getUsers(Model model, Principal principal) {
		String username = principal.getName();
		List<Leave> pendingLeaves = leaveService.getPendingLeaves();
		List<JobApplication> interviews = jobApplicationService.getInterviews();
		List<JobApplication> shortlisted = jobApplicationService.getShortlisted();

		model.addAttribute("employeeUser", employeeService.findByUsername(username));
		List<User> users = userService.getUsers();
		System.out.println(users); // Debug output
		model.addAttribute("users", users);
		model.addAttribute("roles", roleService.getRoles());
		model.addAttribute("pendingLeavesNumber", pendingLeaves.size());
		model.addAttribute("interviews", interviews);
		model.addAttribute("shortlistedNumber", shortlisted.size());

		return "admin";
	}

	@RequestMapping("/users/list")
	@ResponseBody
	public List<User> getUsersList(Model model, Principal principal) {
		return userService.getUsers();
	}
	
	//Modified method to Add a new user User
	@PostMapping(value="users/addnew")
	public RedirectView addNew(User user, RedirectAttributes redir) {
		
		userService.save(user);	
		RedirectView  redirectView= new RedirectView("/login",true);
	        redir.addFlashAttribute("message",
	    		"You successfully registered! You can now login");
	    return redirectView;				
	}

	@PostMapping("/save")
	public String saveUser(@ModelAttribute("user") User user, Model model) {
		userService.save(user);
		return "redirect:/users"; // Redirect to the users list page after saving
	}
	@GetMapping("/add")
	public String showAddUserPage(Model model) {
		model.addAttribute("user", new User()); // Initialize a new User object
		return "userAdd"; // This should match the Thymeleaf template name
	}
	@RequestMapping("/user/findbyid")
	@ResponseBody
	public Optional<User> findById(@RequestParam Integer id) {
		return userService.findById(id);
	}
	
	@RequestMapping(value="/user/update", method= {RequestMethod.PUT, RequestMethod.GET})
	public String update(User user) {
		userService.save(user);
		return "redirect:/user";
	}

	@PostMapping("/user/delete")
	public String delete(@RequestParam Integer id) {
		userService.delete(id);
		return "redirect:/users";
	}
	
	@RequestMapping("/user/assignrole")
	public String assignRole(@RequestParam Integer userId, @RequestParam Integer roleId){
	    roleService.assignUserRole(userId, roleId);
	    roleService.unassignUserRole(userId, (roleId+1)%2);
	    return "redirect:/user";
	}
	@PostMapping("/user/update")
	public String updateUser(@ModelAttribute User user) {
		// Fetch existing user from database
		User existingUser = userService.findById(user.getId()).orElse(null);

		// Update only the fields that are present in the form
		existingUser.setFirstname(user.getFirstname());
		existingUser.setLastname(user.getLastname());
		existingUser.setUsername(user.getUsername());
		// Leave the password unchanged

		userService.updateUser(existingUser);
		return "redirect:/users"; // Redirect to users list
	}
	@RequestMapping(value="/user/update-password", method= {RequestMethod.POST, RequestMethod.GET})
	public String updatePassword(	        
			@RequestParam("username") String username,
	        @RequestParam("password") String password,
	        @RequestParam("newPassword") String newPassword
	) {
		User user = userService.findByUsername(username);
		if(user.getPassword() == password) user.setPassword(newPassword);
		
		userService.save(user);
		return "redirect:/profile";
	}
}
