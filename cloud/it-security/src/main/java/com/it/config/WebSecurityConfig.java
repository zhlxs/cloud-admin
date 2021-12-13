package com.it.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.it.security.JwtAccessDeniedHandler;
import com.it.security.JwtAuthenticationEntryPoint;
import com.it.security.LoginFailureHandler;
import com.it.security.LoginSuccessHandler;
import com.it.security.OutSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@EnableWebSecurity(debug = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder myPasswordEncoder;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private LoginSuccessHandler loginSuccessHandler;
    @Autowired
    private LoginFailureHandler loginFailureHandler;
    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    @Autowired
    private JwtAccessDeniedHandler jwtAccessDeniedHandler;
    @Autowired
    private OutSuccessHandler outSuccessHandler;

    public WebSecurityConfig(ObjectMapper objectMapper) {
        this.myPasswordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 基于token,不需要session
        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authenticationProvider(authenticationProvider())
                .httpBasic()
                //未登录时，进行json格式的提示，很喜欢这种写法，不用单独写一个又一个的类
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and()
                .authorizeRequests()
                .anyRequest().authenticated() //必须授权才能访问
                .and()
                .formLogin() //使用自带的登录
                .permitAll()
                //登录失败，返回json
                .failureHandler(loginFailureHandler)
                //登录成功，返回json
                .successHandler(loginSuccessHandler)
                .and()
                .exceptionHandling()
                //没有权限，返回json
                .accessDeniedHandler(jwtAccessDeniedHandler)
                .and()
                .logout()
                //退出成功，返回json
                .logoutSuccessHandler(outSuccessHandler)
                .permitAll();
        //开启跨域访问
        http.cors().disable();
        //开启模拟请求，比如API POST测试工具的测试，不开启时，API POST为报403错误
        http.csrf().disable();
    }

    @Override
    public void configure(WebSecurity web) {
        //对于在header里面增加token等类似情况，放行所有OPTIONS请求。
        // web.ignoring().antMatchers(HttpMethod.OPTIONS, "/**");
        web.ignoring()
                .antMatchers(
                        "/login", "/test",
                        "/logout",
                        "/css/**",
                        "/js/**",
                        "/index.html",
                        "favicon.ico",
                        "/doc.html",
                        "/webjars/**",
                        "/swagger-resources/**",
                        "/v2/api-docs/**"
                );
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        //对默认的UserDetailsService进行覆盖
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(myPasswordEncoder);
        return authenticationProvider;
    }

}