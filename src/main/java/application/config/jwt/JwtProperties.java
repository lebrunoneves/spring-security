package application.config.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "application.jwt")
@Getter
@Setter
public class JwtProperties {

	private String secretKey;
	private String tokenPrefix;
	private Integer tokenExpirationInDays;
	private String authorizationHeader;
}
