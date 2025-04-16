package com.vefuture.big_bottle.common.config.auth;

import com.vefuture.big_bottle.common.filter.JwtAuthenticationFilter;
import com.vefuture.big_bottle.web.auth.service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletResponse;

/**
 * @author wangb
 * @date 2025/4/16
 * @description Spring Security配置类
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) //开启方法级别的权限注解支持
public class SecurityConfig {
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @Autowired
    private MyUserDetailsService myUserDetailsService;

    /*
        new AntPathRequestMatcher("/auth/**"),
        new AntPathRequestMatcher("/public/**"),
        new AntPathRequestMatcher("/vefuture/**")
    */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth.requestMatchers("/auth/**", "/public/**", "/vefuture/**")
                                                .permitAll()
                                                .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin(Customizer.withDefaults()) //formLogin() 和 logout() 如果不需要，也可以禁用（前后端分离时常这样做）。
                //.logout(Customizer.withDefaults())
                .logout(logout -> logout
                        .logoutUrl("/auth/logout")         // ✅ 你自定义的登出地址
                        .clearAuthentication(true)         // ✅ 清除认证
                        .invalidateHttpSession(true)       // ✅ 让 HttpSession 失效
                        .deleteCookies("JSESSIONID")       // ✅ 删除 cookie（默认登录会种 JSESSIONID）
                        .logoutSuccessHandler((request, response, authentication) -> {
                                                response.setStatus(HttpServletResponse.SC_OK);
                                                response.setContentType("application/json;charset=UTF-8");
                                                response.getWriter().write("{\"code\":200,\"message\":\"退出成功\"}");
                                            })
                )
                .exceptionHandling(ex -> ex
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            response.setContentType("application/json;charset=UTF-8");
                            response.getWriter().write("{\"code\":403,\"message\":\"权限不足，请联系管理员\"}");
                        })
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType("application/json;charset=UTF-8");
                            response.getWriter().write("{\"code\":401,\"message\":\"未登录或Token失效\"}");
                        })
                )
                .csrf(AbstractHttpConfigurer::disable); // 开发阶段可关闭 CSRF
                // --- 修改这里 ---
                //.csrf().disable(); // 使用 5.x 的标准方式禁用 CSRF
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(myUserDetailsService)
                .passwordEncoder(passwordEncoder())  // ✅ 这一行必须加！！
                .and()
                .build();
    }




}
