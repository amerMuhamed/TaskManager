package com.store.store.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.sql.DataSource;

@Configuration
public class AuthenticationDemo {
//     Add support for JDBC ... No more hardcoded users :-)
    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource) {
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);

        // Set the SQL queries
        jdbcUserDetailsManager.setUsersByUsernameQuery(
                "SELECT login_id AS username, pw AS password, active AS enabled FROM members WHERE login_id = ?"

        );
        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery(
                "SELECT m.login_id, r.role FROM members m " +
                        "JOIN roles r ON m.id = r.user_id " +
                        "WHERE m.login_id = ?"
        );

        return jdbcUserDetailsManager;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http , AuthenticationSuccessHandler customAuthenticationSuccessHandler) throws Exception {
        http.authorizeHttpRequests(configurer ->
                        configurer
                                .requestMatchers("/").hasAnyRole("ADMIN","USER")  // Allow unauthenticated access to home page
                                .requestMatchers("/users/**").hasRole("ADMIN")
                                .requestMatchers("/create/**").permitAll()
                                .requestMatchers("/save-user/**").permitAll()
                                .requestMatchers("/update-user/**").hasRole("ADMIN")
                                .requestMatchers("/update/**").hasRole("ADMIN")
                                .requestMatchers("/delete/**").hasAnyRole("ADMIN")
                                .requestMatchers("/addTask").hasRole("ADMIN")
                                .requestMatchers("/saveTask").hasRole("ADMIN")
                                .requestMatchers("/taskList").hasRole("ADMIN")
                                .requestMatchers("/deleteTask").hasRole("ADMIN")
                                .requestMatchers("/updateTask").hasRole("ADMIN")
                                .requestMatchers("/css/**", "/js/**").permitAll()
                                .requestMatchers("/completeTask/**").hasAnyRole("ADMIN","USER")
                                .anyRequest().authenticated()
                )
                .formLogin(form ->
                        form
                                .loginPage("/showLoginPage")
                                .loginProcessingUrl("/authenticateTheUser")
                                .successHandler(customAuthenticationSuccessHandler)
                                .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/") // Redirect to this URL on logout
                        .permitAll()
                )

                .exceptionHandling(configurer ->
                        configurer
                                .accessDeniedPage("/access-denied")
                );
        return http.build();
    }
}
