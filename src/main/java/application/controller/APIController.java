package application.controller;

import application.model.User;
import application.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class APIController {

	@Autowired
	private UserService userService;

	// all authenticated users
	@GetMapping("/public")
	public ResponseEntity<String> test() {
		return ResponseEntity.ok().body("Test API public v1");
	}

	// ADMIN role only
	@GetMapping("/admin")
	public ResponseEntity<String> admin() {
		return ResponseEntity.ok().body("Admin data");
	}

	// USER role only
	@GetMapping("/user")
	public ResponseEntity<UserDetails> user(@AuthenticationPrincipal String username) {

		return ResponseEntity.ok().body(userService.loadUserByUsername(username));
	}

}
