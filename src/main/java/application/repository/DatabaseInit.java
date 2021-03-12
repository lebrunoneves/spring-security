package application.repository;

import application.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class DatabaseInit implements CommandLineRunner {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository userRepository;

	@Override
	public void run(String... args) {

		User bruno = new User();
		bruno.setId(UUID.randomUUID());
		bruno.setUsername("bruno");
		bruno.setPassword(passwordEncoder.encode("123"));
		bruno.setActive(true);
		bruno.setRole("USER");

		User admin = new User();
		admin.setId(UUID.randomUUID());
		admin.setUsername("admin");
		admin.setPassword(passwordEncoder.encode("1"));
		admin.setActive(true);
		admin.setRole("ADMIN");

		userRepository.saveAll(List.of(bruno, admin));

	}
}
