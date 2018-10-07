package com.ming.wowomall.common;

import com.ming.wowomall.util.PropertiesUtil;
import redis.clients.jedis.*;
import redis.clients.util.Hashing;
import redis.clients.util.Sharded;

import java.util.ArrayList;
import java.util.List;

/**分布式Redis
 * @author m969130721@163.com
 * @date 18-9-15 下午11:19
 */
public class RedisShardedPool {

    private static ShardedJedisPool pool;
    /**
     *最大连接数
     */
    private static Integer maxTotal = PropertiesUtil.getPropertyAsInt("redis.max.total",30);
    /**
     *最大空闲数
     */
    private static Integer maxIdle = PropertiesUtil.getPropertyAsInt("redis.max.idle",10);
    /**
     *最小的空闲数
     */
    private static Integer minIdle = PropertiesUtil.getPropertyAsInt("redis.min.idle",3);
    /**
     *在borrow一个jedis是否要进行验证操作
     *如果赋值为true.则得到的jedis肯定是可用的
     */
    private static Boolean testOnBorrow = Boolean.parseBoolean(PropertiesUtil.getProperty("redis.test.borrow","true"));

    /**
     *在释放一个jedis是否要进行验证操作
     *如果赋值为true.则放回jedis肯定是可用的
     */
    private static Boolean testOnReturn = Boolean.parseBoolean(PropertiesUtil.getProperty("redis.test.return","true"));


    private static String redis1Host = PropertiesUtil.getProperty("redis1.host");

    private static Integer redis1Port = PropertiesUtil.getPropertyAsInt("redis1.port",6379);

    private static String redis2Host = PropertiesUtil.getProperty("redis2.host");

    private static Integer redis2Port = PropertiesUtil.getPropertyAsInt("redis2.port",6380);

    private  RedisShardedPool(){

    }

    static {
        initPool();
    }

    private static void initPool(){
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setMinIdle(minIdle);
        config.setTestOnBorrow(testOnBorrow);
        config.setTestOnReturn(testOnReturn);
        //连接耗尽的时候，是否阻塞，true阻塞直到超时，false抛异常，默认true
        config.setBlockWhenExhausted(true);

        JedisShardInfo jedisShardInfo1 = new JedisShardInfo(redis1Host,redis1Port,1000*2);
        JedisShardInfo jedisShardInfo2 = new JedisShardInfo(redis2Host,redis2Port,1000*2);

        List<JedisShardInfo> jedisShardInfoList = new ArrayList<>(2);
        jedisShardInfoList.add(jedisShardInfo1);
        jedisShardInfoList.add(jedisShardInfo2);
        //使用一致性hash算法分配key
        pool = new ShardedJedisPool(config,jedisShardInfoList, Hashing.MURMUR_HASH, Sharded.DEFAULT_KEY_TAG_PATTERN);

    }

    public static ShardedJedis getResource(){
        return pool.getResource();
    }


    public static void returnResource(ShardedJedis jedis){
        pool.returnResource(jedis);
    }

    public static void returnBrokenResource(ShardedJedis jedis){
        pool.returnBrokenResource(jedis);
    }


    public static void main(String[] args) {

    }


}
