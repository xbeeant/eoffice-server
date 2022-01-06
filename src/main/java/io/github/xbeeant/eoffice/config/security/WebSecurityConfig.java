package io.github.xbeeant.eoffice.config.security;

import io.github.xbeeant.spring.security.filter.MoreParameterAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.session.security.web.authentication.SpringSessionRememberMeServices;

/**
 * @author xiaobiao
 * @version 2021/6/30
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        String loginApi = "/api/auth/login";
        // 允许跨域访问
        http.cors();

        http
                .authorizeRequests()
                .antMatchers("/api/auth/**",
                        "/api/user/register",
                        "/api/socket/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/")
                .loginProcessingUrl(loginApi)
                .and()
                .logout().logoutUrl("/api/login/outLogin")
                .logoutSuccessHandler(new LogoutSuccessHandlerExt())
                .invalidateHttpSession(true).deleteCookies("SESSION");

        http.rememberMe().rememberMeServices(rememberMeServices());

        // 自定义的token参数会话认证服务

        // 未登录异常处理
        http.exceptionHandling().authenticationEntryPoint(new EofficeUnauthorizedAuthenticationEntryPoint());
        // 自定义登录求参数获取
        MoreParameterAuthenticationFilter authenticationFilter = new MoreParameterAuthenticationFilter(authenticationManagerBean(), loginApi);
        // 自定义失败处理器
        authenticationFilter.setAuthenticationFailureHandler(new AuthenticationFailureHandlerExt());
        // 自定义成功处理器
        authenticationFilter.setAuthenticationSuccessHandler(new AuthenticationSuccessHandlerExt());


        http.addFilterAt(authenticationFilter, UsernamePasswordAuthenticationFilter.class);

        http.csrf().disable();

        http.headers().frameOptions().disable();
    }

    @Bean
    DaoAuthenticationProvider daoAuthenticationProvider() {
        return new DaoAuthenticationProvider();
    }

    @Bean
    RememberMeServices rememberMeServices() {
        SpringSessionRememberMeServices rememberMeServices = new SpringSessionRememberMeServices();
        // optionally customize
        rememberMeServices.setAlwaysRemember(true);
        return rememberMeServices;
    }
}
