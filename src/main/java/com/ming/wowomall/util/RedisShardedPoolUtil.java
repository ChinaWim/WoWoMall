package com.ming.wowomall.util;

import com.ming.wowomall.common.RedisPool;
import com.ming.wowomall.common.RedisShardedPool;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ShardedJedis;

/**分布式Redis
 * @author m969130721@163.com
 * @date 18-9-13 下午12:13
 */
@Slf4j
public class RedisShardedPoolUtil {
    /**
     * 设置key的有效期
     *
     * @param key
     * @param seconds
     * @return
     */
    public static Long expire(String key, int seconds) {
        ShardedJedis jedis = null;
        Long result = null;
        try {
            jedis = RedisShardedPool.getResource();
            result = jedis.expire(key, seconds);
            RedisShardedPool.returnResource(jedis);
        } catch (Exception e) {
            log.error("expire key:{},error:", key, e);
            RedisShardedPool.returnBrokenResource(jedis);
        }
        return result;
    }


    public static String set(String key, String value) {
        ShardedJedis jedis = null;
        String result = null;
        try {
            jedis = RedisShardedPool.getResource();
            result = jedis.set(key, value);
            RedisShardedPool.returnResource(jedis);
        } catch (Exception e) {
            log.error("set key:{} value:{},error:", key, value, e);
            RedisShardedPool.returnBrokenResource(jedis);
        }
        return result;
    }

    public static String get(String key) {
        ShardedJedis jedis = null;
        String value = null;
        try {
            jedis = RedisShardedPool.getResource();
            value = jedis.get(key);
            RedisShardedPool.returnResource(jedis);
        } catch (Exception e) {
            log.error("get key:{},error:", key, e);
            RedisShardedPool.returnBrokenResource(jedis);
        }
        return value;
    }

    public static String getSet(String key, String value) {
        ShardedJedis jedis = null;
        String result = null;
        try {
            jedis = RedisShardedPool.getResource();
            result = jedis.getSet(key, value);
            RedisShardedPool.returnResource(jedis);
        } catch (Exception e) {
            log.error("getSet key:{},value:{}.error:", key, value, e);
            RedisShardedPool.returnBrokenResource(jedis);
        }
        return result;
    }

    public static String setEx(String key, String value, int seconds) {
        ShardedJedis jedis = null;
        String result = null;
        try {
            jedis = RedisShardedPool.getResource();
            result = jedis.setex(key, seconds, value);
            RedisShardedPool.returnResource(jedis);
        } catch (Exception e) {
            log.error("setEx key:{},value:{},seconds:{},error:", key, value, seconds, e);
            RedisShardedPool.returnBrokenResource(jedis);
        }
        return result;
    }

    public static Long setNx(String key, String value) {
        ShardedJedis jedis = null;
        Long result = null;
        try {
            jedis = RedisShardedPool.getResource();
            result = jedis.setnx(key, value);
            RedisShardedPool.returnResource(jedis);
        } catch (Exception e) {
            log.error("setNx key:{},value:{},error:", key, value, e);
            RedisShardedPool.returnBrokenResource(jedis);
        }
        return result;
    }


    public static Long del(String key) {
        ShardedJedis jedis = null;
        Long result = null;
        try {
            jedis = RedisShardedPool.getResource();
            result = jedis.del(key);
            RedisShardedPool.returnResource(jedis);
        } catch (Exception e) {
            log.error("del key:{},error:",key,e);
            RedisShardedPool.returnBrokenResource(jedis);
        }
        return result;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i ++) {
            set("hello"+i,"world"+i);
        }


    }


}
