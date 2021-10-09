package com.tools.center.hander;

import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSONObject;
import com.tools.center.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component("myAuthSuccessHandler")
public class MyAuthenctiationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request,
                                      HttpServletResponse response,
                                      Authentication authentication) throws IOException {
    log.info("###########登录成功");
    String token = SecureUtil.md5(request.getSession().getId());
    response.setContentType("application/json;charset=UTF-8");
    Map<String,String> map = new HashMap<>();
    map.put("token",token);
    response.getWriter().write(JSONObject.toJSONString(ResultVO.success(map)));
  }

}

