package com.tools.center.filter;

import cn.hutool.core.util.ObjectUtil;
import com.tools.center.common.UserInfo;
import com.tools.center.provider.LocalProvider;
import com.tools.center.utils.AESUtil;
import com.tools.center.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Pattern;

@Slf4j
@Order(1)
@WebFilter(urlPatterns = "/**")
@Configuration
public class AuthFilter implements Filter {

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        UserInfo user = LocalProvider.getUser();
        if(!checkUri(request.getRequestURI())&&user==null){
            String token = request.getHeader("token");
            if(StringUtils.isBlank(token)){
                response.getWriter().write("token为空");
                return;
            }
            String redisKey = AESUtil.decrypt(token, AESUtil.password);
            UserInfo userInfo = (UserInfo) redisUtil.get(redisKey);
            if(ObjectUtil.isNull(userInfo)){
                response.getWriter().write("redis用户信息为空");
                return;
            }
            if(userInfo!=null){
                LocalProvider.init(request,response,userInfo);
            }
            filterChain.doFilter(request,servletResponse);
            LocalProvider.destroy();
        }else {
            log.info("接口url:{}请求",request.getRequestURI());
            filterChain.doFilter(request,servletResponse);
        }
    }

    private boolean checkUri(String uri){
        String pattern = "|(/user/v1/getUserByName)"
                + "|(/roles/v1/findList)"
        + "|(/sse/sendMsg)";
        return Pattern.matches(pattern,uri);
    }
}
