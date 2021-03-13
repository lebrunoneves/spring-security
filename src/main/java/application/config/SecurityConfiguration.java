package application.config;

import application.config.jwt.JwtAuthenticationFilter;
import application.config.jwt.JwtProperties;
import application.config.jwt.JwtTokenValidation;
import application.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	private UserService userService;
	private final JwtProperties jwtProperties;

	@Autowired
	public SecurityConfiguration(JwtProperties jwtProperties, UserService userService) {
		this.jwtProperties = jwtProperties;
		this.userService = userService;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) {
		auth.authenticationProvider(authenticationProvider());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				.csrf().disable()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

				.and()

				.addFilter(new JwtAuthenticationFilter(authenticationManager(), jwtProperties))
				.addFilterAfter(new JwtTokenValidation(jwtProperties), JwtAuthenticationFilter.class)

				.authorizeRequests()
				.antMatchers("/api/public").permitAll()
				.antMatchers("/api/admin").hasAuthority("ADMIN")
				.antMatchers("/api/user").hasRole("USER");

	}

	@Bean
	protected DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
		daoAuthenticationProvider.setUserDetailsService(userService);

		return daoAuthenticationProvider;
	}

	@Bean
	protected PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
