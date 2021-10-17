package br.com.faturaweb.fatura;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig  extends WebSecurityConfigurerAdapter 	{
		/*
		 * Este método permite gerenciar as permissões do usuário logado
		 * */
			@Override
			protected void configure(HttpSecurity http) throws Exception {
				http
					.authorizeRequests()
					.antMatchers("/listar").hasAnyRole("ADM")
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
						  .username("teste")
						  .password("$2a$12$OK.mTLniW.dRIN6qfXLXc.t0/B4RFAEKNIQ123X4.QfjM3NPceSni")
						  .roles("ADM")
						  .build();
				  return new InMemoryUserDetailsManager(user);
			}
			
}
