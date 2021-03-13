package application.config.jwt;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserCredentials {
	private String username;
	private String password;
}
