package web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import web.config.handler.LoginSuccessHandler;
import web.service.UserService;

@Configuration
@EnableWebSecurity

public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserService userDetailsService; // сервис, с помощью которого тащим пользователя
    @Autowired
    private LoginSuccessHandler successUserHandler; // класс, в котором описана логика перенаправления пользователей по ролям
    @Autowired
    public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder()); // конфигурация для прохождения аутентификации
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //http.csrf().disable();
        http.authorizeRequests()
                .antMatchers("/").permitAll() // доступность всем
                .antMatchers("/user/**").access("hasAnyRole('ROLE_USER') or hasAnyRole('ROLE_ADMIN')") // разрешаем входить на /user пользователям с ролью User и Admin
                .antMatchers("/admin/**").access("hasAnyRole('ROLE_ADMIN')")
                .and().formLogin()  // Spring сам подставит свою логин форму
                .successHandler(successUserHandler); // подключаем наш SuccessHandler для перенеправления по ролям
        http.logout()
                .permitAll()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login")
                .and().csrf().disable();
    }
    // Необходимо для шифрования паролей
// В данном примере не используется, отключен
    @Bean
    public static NoOpPasswordEncoder passwordEncoder() {
        return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
    }

}
