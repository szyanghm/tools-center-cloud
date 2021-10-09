package com.tools.center.cache;

import com.tools.center.common.UserInfo;
import com.tools.center.service.impl.DictService;
import com.tools.center.service.impl.UserService;
import com.tools.center.utils.RedisUtil;
import com.tools.center.vo.DictVO;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class StartCache {

    @Autowired
    private DictService dictService;
    @Autowired
    private UserService userService;
    @Autowired
    private RedisUtil redisUtil;

    public static Map<String,String> map = new HashMap<>();

    @PostConstruct
    public void init(){
        log.info("系统启动中...加载字典配置");
        List<DictVO> list = dictService.getKey("root");
        for (DictVO dictVO:list){
            if(dictVO.getType().equals("mysql")&&dictVO.getStatus()==0){
                map.put(dictVO.getSysKey(),dictVO.getSysVal());
            }
        }
        List<UserInfo> allUserInfo = userService.findAllUserInfo();
        allUserInfo.forEach(userInfo -> {
            redisUtil.set(userInfo.getUserId(),userInfo);
        });
    }

    @PreDestroy
    public void destroy(){
        log.info("系统运行结束");
    }
}
