package dev.excelhunt.excel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.apache.catalina.Context;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomAuthenticationSuccessHandler successHandler;

    @Autowired
    private CustomAuthenticationFailureHandler failureHandler;



    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/","/search"
                        ,"/css/**", "/js/**", "/images/**","ico/**"
                        ,"main/**","main/icon.ico","main/style.css","main/bk.css"
                        ,"login/**","login/icon.ico","login/css"
                        ,"error/**","error/icon.ico","error/css"
                        ,"/login","/login/style.css","/login/icon.ico"
                        ,"/**"
                )// 允許這些端點可以被匿名訪問
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                    .loginPage("/login") // 指定自定義登入頁面的URL
                    .loginProcessingUrl("/login") // 登入表單提交路徑
                    .defaultSuccessUrl("/control", true) // 登入成功後的重定向URL
                    .permitAll()
//                .successHandler(successHandler)
//                .failureHandler(failureHandler)
                .permitAll();
    }

    @Autowired
    private CustomUserDetailsService userDetailsService;


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());  // 調用 passwordEncoder() 方法而不是使用 passwordEncoder 字段
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
