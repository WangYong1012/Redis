package com.datayes.bdb.rrp.business.util;

import com.datayes.bdb.rrp.common.util.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisException;

import java.util.List;
import java.util.Map;

/**
 * Author: qianzhong.fu
 * Date: 2015/11/17
 * Time: 12:44
 */
public class RedisUtil {

    private static Logger logger = LoggerFactory.getLogger(RedisUtil.class);

    private static JedisPool jedisPool;

    static {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(6);
        jedisPoolConfig.setMaxTotal(500);
        jedisPoolConfig.setMaxWaitMillis(10000);

        jedisPool = new JedisPool("...",6379,10000,"...");

    }

    public static void setString(String key,String value) {
        Jedis jedis = jedisPool.getResource();
        try{
            jedis.set(key,value);
        }catch (JedisException ex){
            logger.error("set string fail.");
            jedisPool.returnBrokenResource(jedis);
        }
    }

    /**
     * key: redis 中的key
     * map: 待插入redis的hash表
     * @param key
     */
    public static void setHash(String key,Map<String,String> map){
        if(map == null || map.size() == 0)
            return;
        Jedis jedis = jedisPool.getResource();
        try{
            for(String field : map.keySet())
                jedis.hset(key, field, map.get(field));
        }catch (JedisException ex){
            logger.error("set hash fail.");
            jedisPool.returnBrokenResource(jedis);
        }
    }

    /**
     * key: redis 中的key
     * @param key
     * @param list
     */
    public static void setList(String key,List<String> list){
        if(list == null || list.size() == 0)
            return;
        Jedis jedis = jedisPool.getResource();
        try{
            for(String value : list)
                jedis.rpush(key, value);
        }catch (JedisException ex){
            logger.error("set list fail.");
            jedisPool.returnBrokenResource(jedis);
        }
    }

    public static String getString(String key){
        Jedis jedis = jedisPool.getResource();
        try{
            return jedis.get(key);
        }catch (JedisException ex){
            logger.error("set string fail.");
            jedisPool.returnBrokenResource(jedis);
            return null;
        }
    }

    public static List<String> getList(String key){
        Jedis jedis = jedisPool.getResource();
        try{
            return jedis.lrange(key,0,-1);
        }catch (JedisException ex){
            logger.error("set string fail.");
            jedisPool.returnBrokenResource(jedis);
            return null;
        }
    }

    public static Map<String,String> getHash(String key){
        Jedis jedis = jedisPool.getResource();
        try{
            return jedis.hgetAll(key);
        }catch (JedisException ex){
            logger.error("set string fail.");
            jedisPool.returnBrokenResource(jedis);
            return null;
        }
    }

    public static void delete(String key)throws Exception{
        Jedis jedis = jedisPool.getResource();
        try{
            jedis.del(key);
        }catch (JedisException ex){
            logger.error("set fail.");
            jedisPool.returnBrokenResource(jedis);
        }
    }

}
