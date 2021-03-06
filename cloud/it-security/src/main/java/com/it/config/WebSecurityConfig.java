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
        // ??????token,?????????session
        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authenticationProvider(authenticationProvider())
                .httpBasic()
                //?????????????????????json??????????????????????????????????????????????????????????????????????????????
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and()
                .authorizeRequests()
                .anyRequest().authenticated() //????????????????????????
                .and()
                .formLogin() //?????????????????????
                .permitAll()
                //?????????????????????json
                .failureHandler(loginFailureHandler)
                //?????????????????????json
                .successHandler(loginSuccessHandler)
                .and()
                .exceptionHandling()
                //?????????????????????json
                .accessDeniedHandler(jwtAccessDeniedHandler)
                .and()
                .logout()
                //?????????????????????json
                .logoutSuccessHandler(outSuccessHandler)
                .permitAll();
        //??????????????????
        http.cors().disable();
        //???????????????????????????API POST???????????????????????????????????????API POST??????403??????
        http.csrf().disable();
    }

    @Override
    public void configure(WebSecurity web) {
        //?????????header????????????token??????????????????????????????OPTIONS?????????
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
        //????????????UserDetailsService????????????
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(myPasswordEncoder);
        return authenticationProvider;
    }

}