package za.ac.sanbi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Created by hocine on 2017/04/14.
 */
@Configuration
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /*http
                .antMatcher("/")
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/admin")
                .and()
                .authorizeRequests()
                .antMatchers("/webjars/**", "/", "/login").permitAll()
                .anyRequest().authenticated();*/
        http
                .authorizeRequests()
                .antMatchers("/webjars/**", "/img/**", "/users/**", "/", "/login", "/study", "/search/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/admin")
                .and()
                .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/login");

    }
}
