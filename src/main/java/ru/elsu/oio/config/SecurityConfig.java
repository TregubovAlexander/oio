package ru.elsu.oio.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.filter.CharacterEncodingFilter;
import ru.elsu.oio.Url;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring()
				.antMatchers("/resources/**")
				.antMatchers("/api/**");		// TODO: УБРАТЬ!!!
	}

	// Конфигурация web based security для конкретных http-запросов
	@Override
	protected void configure(HttpSecurity http) throws Exception {

		// CharacterEncodingFilter должен быть первым, иначе логин из формы будет приходить не в UTF-8
		CharacterEncodingFilter filter = new CharacterEncodingFilter();
		filter.setEncoding("UTF-8");
		filter.setForceEncoding(true);
		http.addFilterBefore(filter,CsrfFilter.class);

		http.authorizeRequests()
                .antMatchers("/admin").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_DEVELOPER')")
                .antMatchers("/**").access("hasRole('ROLE_USER')");
        http.authorizeRequests()
                .and().formLogin()      // login configuration
                .loginPage("/login")
                .permitAll()
                .loginProcessingUrl("/login/check")
                .failureUrl("/login?error=true")
                .usernameParameter("username")
                .passwordParameter("password")
                .defaultSuccessUrl("/", false)
                .and().logout()         // logout configuration
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")
                .permitAll();
		http.authorizeRequests()
				.and().exceptionHandling().accessDeniedPage(Url.FORBIDDEN_PAGE);
				//.and().csrf(); // TODO: включить!!!

	}

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

	@Bean
	public PasswordEncoder passwordEncoder(){
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder;
	}

}
