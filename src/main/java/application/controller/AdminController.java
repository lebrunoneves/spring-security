package application.controller;

import application.model.User;
import application.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private UserRepository userRepository;

	@GetMapping
	public String admin() {
		return "Admin Page";
	}

	@GetMapping("/users")
	public List<User> users() {
		return userRepository.findAll();
	}
}
