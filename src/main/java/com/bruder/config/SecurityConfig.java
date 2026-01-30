package com.bruder.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.bruder.security.CustomUserDetailsService;

@Configuration
public class SecurityConfig {

	private final CustomUserDetailsService userDetailsService;

	public SecurityConfig(CustomUserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	// ENCRIPTACIÓN
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	// PROVEEDOR DE AUTENTICACIÓN
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}

	// CONFIGURACIÓN DE SEGURIDAD
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http.csrf(csrf -> csrf.disable()).authenticationProvider(authenticationProvider())

				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/", "/login", "/registro", "/css/**", "/js/**", "/images/**").permitAll()

						.requestMatchers("/cerveza/**", "/produccion/**", "/inventarioInicial/**", "/crear/**",
								"/editar/**", "/eliminar/**")
						.hasRole("BODEGA")

						.requestMatchers("/inicio", "/inventarioFinal/**", "/bajas/**", "/despachos/**",
								"/movimientos/**", "/exportar/**")
						.hasAnyRole("BODEGA", "DISTRIBUIDORA")

						.anyRequest().authenticated())

				.formLogin(form -> form.loginPage("/login").loginProcessingUrl("/login").usernameParameter("correo")
						.passwordParameter("password").defaultSuccessUrl("/inicio", true)
						.failureUrl("/login?error=true").permitAll())

				//
				.exceptionHandling(ex -> ex.accessDeniedPage("/acceso-denegado"))

				.logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl("/login?logout=true").permitAll());

		return http.build();
	}

}
