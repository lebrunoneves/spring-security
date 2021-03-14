package application.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private final AuthenticationManager authenticationManager;
	private final JwtProperties jwtProperties;

	public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtProperties jwtProperties) {
		this.authenticationManager = authenticationManager;
		this.jwtProperties = jwtProperties;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request,
												HttpServletResponse response) throws AuthenticationException {

		try {
			UserCredentials authenticationRequest = new ObjectMapper()
					.readValue(request.getInputStream(), UserCredentials.class);

			Authentication authentication = new UsernamePasswordAuthenticationToken(
					authenticationRequest.getUsername(),
					authenticationRequest.getPassword()
			);

			return authenticationManager.authenticate(authentication);

		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request,
											HttpServletResponse response,
											FilterChain chain,
											Authentication authResult) {
		String token = Jwts.builder()
				.setSubject(authResult.getName())
				.claim("authorities", authResult.getAuthorities())
				.setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(jwtProperties.getTokenExpirationInDays())))
				.signWith(Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes()))
				.compact();

		response.addHeader(jwtProperties.getAuthorizationHeader(), jwtProperties.getTokenPrefix() + token);
	}
}
