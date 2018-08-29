package com.ming.wowomall.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author m969130721@163.com
 * @date 18-8-28 下午10:22
 */
public class TokenCache {

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenCache.class);
    //LRU
    private static LoadingCache<String,String> localCache = CacheBuilder.newBuilder().
            initialCapacity(1000).maximumSize(10000).expireAfterAccess(12,TimeUnit.HOURS).build(new CacheLoader<String, String>() {
                //默认的数据加载实现，当调用get取值时候，如果key没有命中，就调用这个方法加载
        @Override
        public String load(String s) throws Exception {
            return "null";
        }
    });


    public static final String TOKEN_PREFIX = "token_";

    public static void setKey(String key,String value){
        localCache.put(key,value);
    }

    public static String getKey(String key){
        try {
            return localCache.get(key);
        } catch (ExecutionException e) {
            LOGGER.error("localCache get error",e);
            return "null";
        }

    }

}
