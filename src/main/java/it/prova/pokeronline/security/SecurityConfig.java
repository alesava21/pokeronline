package it.prova.pokeronline.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity 
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private JWTFilter jwtFilter;
	@Autowired
	private CustomUserDetailsService customUserDetailsService;

	@Autowired
	private JWTAuthEntryPoint unauthorizedHandler;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception { 
		http.csrf().disable() // Disabling csrf
				.httpBasic().disable() // Disabling http basic
				.cors() // Enabling cors
				.and()
 				
				.authorizeHttpRequests() 
				.antMatchers("/api/auth/login").permitAll()
				.antMatchers("/api/tavolo/cercaTavolo").hasAnyRole("ROLE_ADMIN", "ROLE_SPECIAL_USER", "ROLE_CLASSIC_USER")
				.antMatchers("/api/tavolo/cercaTavoliCreatiDaMe").hasAnyRole("ROLE_ADMIN", "ROLE_SPECIAL_USER")
				.antMatchers("/api/tavolo/**").hasAnyRole("ADMIN", "ROLE_SPECIAL_USER")
				.antMatchers("/api/utente/userInfo").authenticated()
				.antMatchers("/api/utente/compraCredito/**").authenticated()
				.antMatchers("/api/utente/**").hasRole("ROLE_ADMIN")
				.antMatchers("/api/gioco/**").authenticated()
				.antMatchers("/**").hasAnyRole("ROLE_ADMIN", "ROLE_SPECIAL_USER", "ROLE_CLASSIC_USER")
				// .antMatchers("/anonymous*").anonymous()
				.anyRequest().authenticated()
				.and()
				
				// imposto il mio custom user details service
				.userDetailsService(customUserDetailsService) 
				// quando qualcosa fallisce ho il mio handler che customizza la response
				.exceptionHandling().authenticationEntryPoint(unauthorizedHandler)
				.and()
				
				// non abbiamo bisogno di una sessione: meglio forzare a stateless
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); 

		// Adding the JWT filter
		http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
	}


}
