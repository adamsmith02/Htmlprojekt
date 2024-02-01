package application.config;

import application.dao.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import application.service.CustomUserDetailsService;




@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
            .authorizeRequests().antMatchers("/edit/*").access("hasRole('USER') or hasRole('ADMIN')")
            .anyRequest().permitAll()
            .and()
            .formLogin()
              .loginPage("/login")
              .defaultSuccessUrl("/index")
              .permitAll()
            .permitAll()
            .and()
            .logout().logoutSuccessUrl("/index").permitAll();
  }

  @Autowired
  protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    auth
            .userDetailsService(userDetailsService())
            .passwordEncoder(passwordEncoder());
  }

  @Bean
  @Lazy
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public UserDetailsService userDetailsService() {
    return new CustomUserDetailsService();
  }
  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    // A super.authenticationManagerBean() hívása létrehozza és visszaadja az AuthenticationManager-t.
    return super.authenticationManagerBean();
  }
}
