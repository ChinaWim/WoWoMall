package com.ming.wowomall.common;

import com.ming.wowomall.util.PropertiesUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.config.Config;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**Redisson不支持一致性hash算法,和SpringSession一样，不能使用分布式redis
 * @author m969130721@163.com
 * @date 18-9-17 下午11:28
 */
@Component
@Slf4j
public class RedissonManager {

    private Config config = new Config();

    private Redisson redisson = null;

    private static String redis1Host = PropertiesUtil.getProperty("redis1.host");

    private static Integer redis1Port = PropertiesUtil.getPropertyAsInt("redis1.port",6379);

    private static String redis2Host = PropertiesUtil.getProperty("redis2.host");

    private static Integer redis2Port = PropertiesUtil.getPropertyAsInt("redis2.port",6380);

    /**
     * 在构造器完成后执行方法
     */
    @PostConstruct
    private void init() {
        try{
            config.useSingleServer().setAddress(new StringBuilder().append("http://"+redis1Host).append(":").append(redis1Port).toString());
            redisson = (Redisson) Redisson.create(config);
            log.info("初始化Redisson结束");
        }catch (Exception e){
            log.error("初始化Redisson异常",e);
        }
    }

    public Redisson getRedisson() {
        return redisson;
    }


}
