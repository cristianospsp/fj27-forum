package br.com.alura.forum.configuration;

import br.com.alura.forum.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Order(1)
@Configuration
public class AdminSecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserService usersService;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.antMatcher("/admin/**")
				.authorizeRequests().anyRequest().hasRole("ADMIN")
				.and()
				.httpBasic();
	}
	@Override
	protected void configure(AuthenticationManagerBuilder auth)
			throws Exception {
		auth.userDetailsService(this.usersService)
				.passwordEncoder(new BCryptPasswordEncoder());
	}

}