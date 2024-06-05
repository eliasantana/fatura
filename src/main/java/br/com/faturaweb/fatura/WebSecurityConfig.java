package br.com.faturaweb.fatura;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

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
					.antMatchers("/").hasAnyRole("ADM")	
					.antMatchers("/listar").hasAnyRole("ADM")
					.antMatchers("/conta/listar").hasAnyRole("ADM")	
					.antMatchers("/excluir/{id}").hasAnyRole("ADM")	
					.antMatchers("/conta/alterar/{id}").hasAnyRole("ADM")	
					.antMatchers("conta/creditar/{id}").hasAnyRole("ADM")	
					.antMatchers("conta/movimentacao").hasAnyRole("ADM")	
					.antMatchers("conta/transferir").hasAnyRole("ADM")	
					.antMatchers("/lotecontabil/pesquisar").hasAnyRole("ADM")	
					.antMatchers("/cadastro/salvar").hasAnyRole("ADM")	
					.antMatchers("/cadastro/excluir/{id}").hasAnyRole("ADM")	
					.antMatchers("/cadastro/alterar/{id}").hasAnyRole("ADM")	
					.antMatchers("/cartao/cadastro").hasAnyRole("ADM")	
					.antMatchers("/cartao/salvar").hasAnyRole("ADM")	
					.antMatchers("/cartao/excluir/{id}").hasAnyRole("ADM")	
					.antMatchers("/cartao/alterar/{id}").hasAnyRole("ADM")	
					.antMatchers("/chaves/listar").hasAnyRole("ADM")	
					.antMatchers("/chaves/alterar/{chave}").hasAnyRole("ADM")	
					.antMatchers("/extrato/financeiro/**").hasAnyRole("ADM")
					.antMatchers("/formapagto/**").hasAnyRole("ADM")
					.antMatchers("/dashboard").hasAnyRole("ADM")
					.antMatchers("/configuracoes/**").hasAnyRole("ADM")
					.antMatchers("/meta/**").hasAnyRole("ADM")
					.antMatchers("/itmeta/**").hasAnyRole("ADM")
					.antMatchers("/lancamento/**").hasAnyRole("ADM")
					.antMatchers("/log/**").hasAnyRole("ADM")
					.antMatchers("/lotecontabil/**").hasAnyRole("ADM")
					.antMatchers("/provisao/**").hasAnyRole("ADM")
					.antMatchers("/receita/**").hasAnyRole("ADM")
					.antMatchers("/relatorio/**").hasAnyRole("ADM")
					.antMatchers("/tipolancamento/**").hasAnyRole("ADM")
					.antMatchers("/upload/**").hasAnyRole("ADM")
					.antMatchers("/usuario/**").hasAnyRole("ADM")
					.antMatchers(HttpMethod.GET, "/getimagem").permitAll( )// Liberando a requisição da imagem na página de login
					.antMatchers(HttpMethod.GET, "/cartao/getimagem/").permitAll( )// Liberando a requisição da imagem na página de login
					.antMatchers(HttpMethod.GET, "/api").permitAll( )//Liberando endPoint
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
				 System.out.println("validação de segurança");
				  return new InMemoryUserDetailsManager(user);
				  
			}
			
}
