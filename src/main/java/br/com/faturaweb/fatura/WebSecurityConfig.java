package br.com.faturaweb.fatura;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import br.com.faturaweb.fatura.model.Usuario;
import br.com.faturaweb.fatura.repository.UsuarioRepository;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig  extends WebSecurityConfigurerAdapter 	{
	@Autowired
	UsuarioRepository usuarioRepository;
		/*
		 * Este método permite gerenciar as permissões do usuário logado
		 * */
			@Override
			protected void configure(HttpSecurity http) throws Exception {
				http
					.authorizeRequests()
					.antMatchers("/listar").hasAnyRole("ADM")
					.antMatchers("/getlogo").hasAnyRole("ADM")
					.anyRequest().authenticated()
					.and()
					.formLogin(form -> form
							.loginPage("/login").permitAll())
					.logout(
									logout -> logout.logoutUrl("/logout")
									
									).csrf().disable();				
			}
			
		
			
			@Bean
			@Override
			public UserDetailsService userDetailsService() {
				 
				  UserDetails user =
						  User.withDefaultPasswordEncoder()
						  .username("Elias")
						  //$2a$12$OK.mTLniW.dRIN6qfXLXc.t0/B4RFAEKNIQ123X4.QfjM3NPceSni
						  .password("3L14s2007")
						  .roles("ADM")
						  .build();
				
				  return new InMemoryUserDetailsManager(user);
				  
			}
			
}
