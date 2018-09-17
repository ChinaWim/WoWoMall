package com.ming.wowomall.task;

import com.ming.wowomall.common.Const;
import com.ming.wowomall.common.RedisShardedPool;
import com.ming.wowomall.service.OrderService;
import com.ming.wowomall.util.PropertiesUtil;
import com.ming.wowomall.util.RedisPoolUtil;
import com.ming.wowomall.util.RedisShardedPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author m969130721@163.com
 * @date 18-9-17 上午11:13
 */
@Component
@Slf4j
public class ClosedOrderTask {

    @Autowired
    private OrderService orderService;

    /**
     * 每一分钟执行
     */
    @Scheduled(cron = "0 0/1 * * * ?")
    public void closedOrderTaskV2() {
        log.info("关闭订单任务执行开始");
        Long timeout = PropertiesUtil.getPropertyAsLong("lock.timeout", 5000L);
        Long setNxResult = RedisShardedPoolUtil.setNx(Const.RedisLockKey.CLOSE_ORDER_TASK_LOCK, String.valueOf(System.currentTimeMillis() + timeout));
        if (setNxResult != null && setNxResult.intValue() == 1) {
            closedOrder(Const.RedisLockKey.CLOSE_ORDER_TASK_LOCK);
        } else {
            String lockValue = RedisPoolUtil.get(Const.RedisLockKey.CLOSE_ORDER_TASK_LOCK);
            if (lockValue != null && (Long.parseLong(lockValue) < System.currentTimeMillis())) {
                String getSetLockValue = RedisPoolUtil.getSet(Const.RedisLockKey.CLOSE_ORDER_TASK_LOCK, String.valueOf(System.currentTimeMillis() + timeout));
                if (getSetLockValue == null || (StringUtils.equals(getSetLockValue,lockValue))) {
                    //真正获取到锁
                    closedOrder(Const.RedisLockKey.CLOSE_ORDER_TASK_LOCK);
                } else {
                    log.info("没有获取到分布式锁:{}", Const.RedisLockKey.CLOSE_ORDER_TASK_LOCK);
                }
            } else {
                log.info("没有获取到分布式锁:{}",Const.RedisLockKey.CLOSE_ORDER_TASK_LOCK);
            }
        }
        log.info("关闭订单任务执行结束");
    }


    private void closedOrder(String lockName) {
        log.info("获取分布式锁:{}",lockName);
        RedisShardedPoolUtil.expire(lockName, 5);
        orderService.closedOrder(PropertiesUtil.getPropertyAsInt("closed.order.task.time.hour", 2));
        RedisShardedPoolUtil.del(lockName);
        log.info("释放分布式锁:{},ThreadName:{}",lockName,Thread.currentThread().getName());
        log.info("================================");
    }


}
