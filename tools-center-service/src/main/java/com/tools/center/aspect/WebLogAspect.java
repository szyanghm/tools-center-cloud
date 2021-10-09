package com.tools.center.aspect;

import com.alibaba.fastjson.JSONArray;
import com.google.gson.Gson;
import com.tools.center.utils.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * @author yhm
 * @date 2021/6/17
 * @time 14:03
 * @discription
 **/
@Aspect
@Component
@Slf4j
public class WebLogAspect {

    ThreadLocal<Long> startTime = new ThreadLocal<>();

    /**
     * 以 controller 包下定义的所有请求为切入点
     */
    @Pointcut("execution(public * com.tools.center.controller.*.*(..))")
    public void webLog() {
    }

    /**
     * 在切点之前织入
     *
     * @param joinPoint
     * @throws Throwable
     */
    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String createTime = sdf.format(timestamp);
        // 开始打印请求日志
        startTime.set(System.currentTimeMillis());
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String ip = HttpUtils.getIpAddr(request);
        log.info("========================================= Start " + createTime + " =========================================");
        log.info("请求ip         :{}", ip);
        log.info("请求接口地址   :{}", request.getRequestURL());
        log.info("请求方式       :{}", request.getMethod());
        log.info("执行类         :{}", joinPoint.getSignature().getDeclaringTypeName());
        log.info("执行方法       :{}", joinPoint.getSignature().getName());
        //入参不允许为空和对象空属性值，不然此处为报错即请求失败
        //log.info("Request params :{}", new Gson().toJson(joinPoint.getArgs()));
    }

    /**
     * 在切点之后织入
     *
     * @throws Throwable
     */
    @After("webLog()")
    public void doAfter() {

    }


    /**
     * 环绕
     *
     * @param joinPoint
     * @return
     */
    @Around("webLog()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = joinPoint.proceed();
        log.info("耗时           :{}毫秒", (System.currentTimeMillis() - startTime.get()));
        log.info("Response params:{}", JSONArray.toJSONString(result));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String endTime = sdf.format(timestamp);
        log.info("========================================= end " + endTime + " ===========================================");
        return result;
    }

}

