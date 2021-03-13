package application.config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import net.bytebuddy.implementation.bind.MethodDelegationBinder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class JwtTokenValidation extends OncePerRequestFilter {

	private final JwtProperties jwtProperties;

	public JwtTokenValidation(JwtProperties jwtProperties) {
		this.jwtProperties = jwtProperties;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

		String authorizationHeader = request.getHeader(jwtProperties.getAuthorizationHeader());

		if (authorizationHeader == null || authorizationHeader.isEmpty() || !authorizationHeader.startsWith(jwtProperties.getTokenPrefix())) {
			filterChain.doFilter(request, response);
			return;
		}

		String token = authorizationHeader.replace(jwtProperties.getTokenPrefix(), "");

		try {

			Jws<Claims> claimsJws = Jwts.parser()
					.setSigningKey(Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes()))
					.parseClaimsJws(token);

			Claims body = claimsJws.getBody();

			String username = body.getSubject();

			var authorities = (List<Map<String, String>>) body.get("authorities");

			Set<SimpleGrantedAuthority> simpleGrantedAuthorities = authorities.stream()
					.map(m -> new SimpleGrantedAuthority(m.get("authority")))
					.collect(Collectors.toSet());

			Authentication authentication = new UsernamePasswordAuthenticationToken(
					username,
					null,
					simpleGrantedAuthorities
			);

			SecurityContextHolder.getContext().setAuthentication(authentication);

		} catch (JwtException e) {
			System.out.println("Untrusted token");
		}

		filterChain.doFilter(request, response);
	}
}
