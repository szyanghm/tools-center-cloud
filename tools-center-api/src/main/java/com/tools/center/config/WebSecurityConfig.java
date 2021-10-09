package com.tools.center.config;

import com.tools.center.hander.MyAuthenctiationFailureHandler;
import com.tools.center.hander.MyAuthenctiationSuccessHandler;
import com.tools.center.security.MyUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * SpringSecurity的配置类
 */
@Component
@EnableGlobalMethodSecurity(prePostEnabled = true, proxyTargetClass = true)//启用方法级的权限认证
@EnableWebSecurity

public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private MyUserDetailsService myUserDetailsService;
    @Resource
    private PasswordEncoder encoder;
    @Resource
    private MyAuthenctiationSuccessHandler myAuthSuccessHandler;
    @Resource
    private MyAuthenctiationFailureHandler myAuthFailureHandler;
    /**
     * 用户授权
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //使用自定义的认证类实现授权
        auth.userDetailsService(myUserDetailsService).passwordEncoder(encoder);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * 配置放行的请求
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/static/diagram-viewer/**");
        web.ignoring().antMatchers("/static/editor-app/**");
        web.ignoring().antMatchers("/static/**");
        web.ignoring().antMatchers("/sse/sendMsg");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().cors() //开启跨域
                .and()
            .authorizeRequests()
                .antMatchers("/login").permitAll()
                .antMatchers("/**").
                access("hasRole('USER')")
            .anyRequest().authenticated()
            .and()
            .formLogin()
            .loginProcessingUrl("/login") //指定处理登录的请求地址

            // 登录成功
            .successHandler(myAuthSuccessHandler).permitAll() //登录成功的回调
            // 登录失败
            .failureHandler(myAuthFailureHandler).permitAll() //登录失败的回调

            .and()
            // 注销成功
            .logout().logoutUrl("/logout").           //登出地址为/logout
            invalidateHttpSession(true);

            //.antMatchers("/test/a01").permitAll(); //注意：放行的url不用加项目根目录/api
            //.and()
            //.exceptionHandling().accessDeniedPage("/error.html");//异常处理页面，例如没有权限访问等
          ;
        //.loginPage("http://127.0.0.1:8080/#/login")   //指定登录地址
        //        //设置用户只允许在一处登录，在其他地方登录则挤掉已登录用户，被挤掉的已登录用户则需要返回/login.html重新登录
//      //  http.sessionManagement().maximumSessions(1).expiredUrl("http://127.0.0.1:8080/#/login");
    }

}
