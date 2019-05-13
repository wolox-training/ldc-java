package wolox.training.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityJavaConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomAuthenticationProvider authProvider;

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authProvider)
            .inMemoryAuthentication()
            .withUser("user").password(encoder().encode("userPass")).roles("USER");
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .exceptionHandling()
            .and()
            .authorizeRequests()
            .antMatchers(HttpMethod.GET, "/api/books/**").authenticated()
            .antMatchers(HttpMethod.GET, "/api/users/**").authenticated()
            .antMatchers(HttpMethod.DELETE, "/api/books/**").authenticated()
            .antMatchers(HttpMethod.DELETE, "/api/users/**").authenticated()
            .antMatchers(HttpMethod.PUT, "/api/books/**").authenticated()
            .antMatchers(HttpMethod.PUT, "/api/users/**").authenticated()
            .antMatchers(HttpMethod.POST, "/api/books").permitAll()
            .antMatchers(HttpMethod.POST, "/api/users").permitAll()
            .and()
            .formLogin()
            .and()
            .logout();
    }

}
