package ch.supsi.webapp.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration @EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    CustomUserDetailService customUserDetailService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .mvcMatchers("/").permitAll()
                .mvcMatchers("/ticket/new").authenticated()
                .mvcMatchers("/ticket/*/edit").hasRole("ADMIN")
                .mvcMatchers("/ticket/*/delete").hasRole("ADMIN")
                .mvcMatchers("/ticket/**").permitAll()
                .mvcMatchers("/image/**").permitAll()
                .mvcMatchers("/css/**").permitAll()
                .mvcMatchers("/js/**").permitAll()
                .mvcMatchers("/webjars/**").permitAll()
                .mvcMatchers("/fonts/**").permitAll()
                .mvcMatchers("/login").permitAll()
                .mvcMatchers("/register").permitAll()
                .antMatchers("/ticket/search").permitAll()
                .mvcMatchers("/ticket/*/attachment").permitAll()
                .mvcMatchers("/board").authenticated()
                .mvcMatchers("/ticket/*/time-spent").authenticated()
                .mvcMatchers("/ticket/*/show").authenticated()
                .mvcMatchers("/ticket/*/add-watched").authenticated()
                .mvcMatchers("/ticket/*/remove-watched").authenticated()
                .mvcMatchers("/get-watched-tickets-count").authenticated()
                .mvcMatchers("/ticket/*/is-watched").authenticated()
                .mvcMatchers("/watched-tickets").authenticated()
                .mvcMatchers("/ticket/*/watchers").authenticated()
                .anyRequest().authenticated()
                .and()
					.formLogin()
					.loginPage("/login")
					.failureUrl("/login?error")
                .and()
					.logout()
					.logoutUrl("/logout")
					.logoutSuccessUrl("/");
        http.csrf().disable();

        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailService);
        authProvider.setPasswordEncoder(new BCryptPasswordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(authProvider())
                .build();
    }

}
