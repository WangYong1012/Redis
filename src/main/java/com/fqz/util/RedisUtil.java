package com.fqz.util;

import com.fqz.exception.RedisException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisException;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: qianzhong.fu
 * Date: 2015/2/9
 * Time: 15:44
 */
public class RedisUtil {
    @Value("${redis.host}")
    private static String host;
    @Value("${redis.port}")
    private static int port;

    public enum Day{
        MONDAY,TUESDAY,WEDNESDAY,THURSDAY,FRIDAY,SATURDAY,SUNDAY
    }
    private static Logger logger = LoggerFactory.getLogger(RedisUtil.class);

    //jedisPoolMap包含多个不同类型的jedisPool
    private static Map<Day,JedisPool> jedisPoolMap = new HashMap<Day,JedisPool>();
    static {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        //控制一个pool最多有多少个状态为idle(空闲的)的jedis实例。
        jedisPoolConfig.setMaxIdle(5);
        //控制一个pool可分配多少个jedis实例，通过pool.getResource()来获取；
        jedisPoolConfig.setMaxTotal(500);
        //表示当borrow(引入)一个jedis实例时，最大的等待时间，如果超过等待时间，则直接抛出JedisConnectionException；单位为ms，1s=1000ms
        jedisPoolConfig.setMaxWaitMillis(10000);

        JedisPool mondayPool = new JedisPool(jedisPoolConfig,host,port,10000,"1234");
        JedisPool tuesdayPool = new JedisPool(jedisPoolConfig,host,port,10000,"1234");

        jedisPoolMap.put(Day.MONDAY,mondayPool);
        jedisPoolMap.put(Day.TUESDAY,tuesdayPool);

    }

    public static void set(Day day,String key,String value)throws Exception {
        try{
            JedisPool jedisPool = jedisPoolMap.get(day);
            if(jedisPool == null)
                throw new RedisException(-404, "jedis pool not found");
            Jedis jedis = jedisPool.getResource();
            jedis.auth("12121212");
            jedis.set(key,value);
        }catch (JedisException ex){
            logger.error("set fail.");
            throw new RedisException(-500,"jedis internal error");
        }
    }
}
