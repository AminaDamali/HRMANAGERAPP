package com.MercureIT.HR_Manager;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import com.MercureIT.HR_Manager.models.User;
import com.MercureIT.HR_Manager.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import com.MercureIT.HR_Manager.models.Client;
import com.MercureIT.HR_Manager.models.Employee;
import com.MercureIT.HR_Manager.models.JobApplication;
import com.MercureIT.HR_Manager.models.Leave;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ApplicationController {

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private LeaveService leaveService;
	@Autowired
	private ClientService clientService;

	@Autowired
	private JobApplicationService jobApplicationService;

	@Autowired
	private UserService userService;

	private void populateModel(Model model, Principal principal) {
		String username = principal.getName();
		// Load all necessary data before rendering the view
		Employee employeeUser = employeeService.findByUsername(username);
		List<Employee> employees = employeeService.getEmployees();
		List<Employee> newHiresThisYear = employeeService.getEmployeesByHireCurrentYear();
		List<Employee> newHiresThisMonth = employeeService.getEmployeesByHireCurrentMonth();
		List<Leave> currentLeaves = leaveService.getCurrentLeaves();
		List<Leave> pendingLeaves = leaveService.getPendingLeaves();
		List<JobApplication> interviews = jobApplicationService.getInterviews();
		List<JobApplication> shortlisted = jobApplicationService.getShortlisted();
		Map<String, Integer> employeesNbrByJobs = employeeService.getEmployeesNbrByJobs();
		Map<String, Integer> employeesNbrByStatus = employeeService.getEmployeesNbrByStatus();

		model.addAttribute("employeeUser", employeeUser);
		model.addAttribute("employeesNumber", employees.size());
		model.addAttribute("newHiresThisYear", newHiresThisYear.size());
		model.addAttribute("newHiresThisMonth", newHiresThisMonth.size());
		model.addAttribute("currentLeaves", currentLeaves);
		model.addAttribute("pendingLeavesNumber", pendingLeaves.size());
		model.addAttribute("interviews", interviews);
		model.addAttribute("shortlistedNumber", shortlisted.size());
		model.addAttribute("employeesByJobs", employeesNbrByJobs);
		model.addAttribute("employeesByEmployeeType", employeesNbrByStatus);
	}

	@GetMapping("/index")
	public String goHome(Model model, Principal principal) {
		populateModel(model, principal);
		return "index";
	}

	@GetMapping("")
	public String home(Model model, Principal principal) {
		populateModel(model, principal);
		return "index";
	}



	@GetMapping("/users")
	public String showUsers(Model model) {
		List<User> users = userService.getUsers();
		model.addAttribute("users", users);
		return "users"; // Ensure this matches the Thymeleaf template name
	}

	@GetMapping("/login")
	public String login() {
		return "pages-login"; // Ensure this matches the Thymeleaf template name
	}


	@GetMapping("/admin")
	public String dashboard(Model model) {
		Integer totalUsers = userService.countUsers();
		Integer totalClients = clientService.countClients();
		Integer totalEmployees = employeeService.countEmployees();

		model.addAttribute("totalUsers", totalUsers);
		model.addAttribute("totalClients", totalClients);
		model.addAttribute("totalEmployees", totalEmployees);

		return "admin";  // Renders admin.html
	}
	@GetMapping("/clientEdit")
	public String clientEdit() {
		return "clientEdit"; // Ensure this matches the Thymeleaf template name
	}


	@GetMapping("/logout")
	public String logout() {
		return "pages-login"; // Ensure this matches the Thymeleaf template name
	}

	@GetMapping("/register")
	public String register() {
		return "register"; // Ensure this matches the Thymeleaf template name
	}
	@GetMapping("/userEdit/{id}")
	public String userEdit(@PathVariable("id") Integer id, Model model) {
		// Fetch the user by ID, assuming you have a user service to fetch the user details
		User user = userService.getUserById(id);

		// Add the user to the model to pass it to the Thymeleaf template
		model.addAttribute("user", user);

		return "userEdit"; // This should be the name of your Thymeleaf template
	}


	@GetMapping("/accessDenied")
	public String accessDenied(Model model, Principal principal) {
		String username = principal.getName();
		Employee employeeUser = employeeService.findByUsername(username);
		model.addAttribute("employeeUser", employeeUser);
		return "accessDenied"; // Ensure this matches the Thymeleaf template name
	}

	@GetMapping("/faq")
	public String faq(Model model, Principal principal) {
		String username = principal.getName();
		Employee employeeUser = employeeService.findByUsername(username);
		model.addAttribute("employeeUser", employeeUser);
		return "pages-faq"; // Ensure this matches the Thymeleaf template name
	}
	@GetMapping("/clientAdd")
	public String showClientAddPage(Model model) {
		model.addAttribute("client", new Client());  // Assuming you have a Client class
		return "clientAdd";  // This is the name of your Thymeleaf template
	}

	@PostMapping("/clients/add")
	public String addClient(@ModelAttribute("client") Client client, BindingResult result) {
		if (result.hasErrors()) {
			return "clientAdd";  // Return to the same page if there are validation errors
		}
		// Save the client object using a repository or service
		clientService.save(client);  // Assuming clientService is available to save the client

		return "redirect:/clients";  // Redirect to the list of clients or a success page
	}
}
