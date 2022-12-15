package ru.kata.spring.boot_security.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;



@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    final UserService userService;
    final RoleService roleService;
    final SuccessUserHandler loginSuccessHandler;

    @Autowired
    public WebSecurityConfig(UserService userService, RoleService roleService, SuccessUserHandler successUserHandler) {
        this.userService = userService;
        this.roleService = roleService;
        this.loginSuccessHandler = successUserHandler;
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // выключаем кроссдоменную секьюрность (на этапе обучения неважна)
        http.csrf().disable()
                // делаем страницу регистрации недоступной для авторизированных пользователей
                .authorizeRequests()
//                .antMatchers("/**").permitAll()

                // защищенные URL
                .antMatchers("/api/**").permitAll()
                .antMatchers("/admin/**", "/", "/index").hasRole("ADMIN")
                .antMatchers("/user/**").hasAnyRole("USER", "ADMIN")
                .anyRequest().authenticated()
                .and()
                // указываем страницу с формой логина
                // указываем логику обработки при логине
                .formLogin().successHandler(loginSuccessHandler).loginPage("/")
                // даем доступ к форме логина всем
                .permitAll()
                .and()
                // разрешаем делать логаут всем
                .logout()
                .permitAll();
    }
    @Bean
    protected BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder(12);
    }


    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder());
    }

//    @PostConstruct
//    public void init() {
//        Role role1 = new Role("ROLE_ADMIN");
//        Role role2 = new Role("ROLE_USER");
//
//        roleService.add(role1);
//        roleService.add(role2);
//
//        Set<Role> roleAdmin = new HashSet<>();
//        Set<Role> roleUser = new HashSet<>();
//
//        roleAdmin.add(role1);
//        roleUser.add(role2);
//
//        User user2 = new User(2,"Doom", "Guy", "22", "doom@mail.ru", "user", "user", roleUser);
//        User user1 = new User(1,"Mick", "Gordon", "37", "mick@mail.ru", "admin", "admin", roleAdmin);
//
//        userService.add(user1);
//        userService.add(user2);
//    }

}