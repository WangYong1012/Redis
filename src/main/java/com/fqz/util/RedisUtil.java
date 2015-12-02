package com.datayes.bdb.rrp.business.cache.Redis;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datayes.bdb.rrp.common.util.Config;
import com.datayes.paas.utility.StringUtil;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Transaction;

/**
 * Author: qianzhong.fu
 * Date: 2015/11/17
 * Time: 12:44
 */
public class RedisUtil {

    public static final String TYPE_GET = "get";
    public static final String TYPE_SAVE = "save";
    public static final String TYPE_DELETE = "delete";

    public static final Integer DEFAULT_DB = Config.REDIS.getAsInt("redis.default.db");
    public static final Integer CHANNEL_DB = Config.REDIS.getAsInt("redis.channel.db");
    public static final Integer CHANNEL_INFO_DB = Config.REDIS.getAsInt("redis.channel.info.db");
    public static final Integer STATISTICS_DB = Config.REDIS.getAsInt("redis.statistics.db");

    private static final Integer EXPIRE_SECONDS = Config.REDIS.getAsInt("redis.key.expired.time");

    private static Logger logger = LoggerFactory.getLogger(RedisUtil.class);

    private static final Map<Integer, JedisPool> jedisPools = new HashMap<>();

    static {
        try {
            JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
            jedisPoolConfig.setMaxIdle(Config.REDIS.getAsInt("jedis.pool.maxIdle"));
            jedisPoolConfig.setMaxTotal(Config.REDIS.getAsInt("jedis.pool.maxTotal"));
            jedisPoolConfig.setMaxWaitMillis(Config.REDIS.getAsInt("jedis.pool.maxWaitMillis"));

            String host = Config.REDIS.get("redis.host");
            int port = Config.REDIS.getAsInt("redis.port");
            int timeout = Config.REDIS.getAsInt("jedis.timeout");
            String password = Config.REDIS.get("redis.pwd");
            if (StringUtil.isBlank(password)) password = null;

            JedisPool jedisPool = new JedisPool(jedisPoolConfig, host, port, timeout, password, DEFAULT_DB);
            jedisPools.put(DEFAULT_DB, jedisPool);

            jedisPool = new JedisPool(jedisPoolConfig, host, port, timeout, password, CHANNEL_DB);
            jedisPools.put(CHANNEL_DB, jedisPool);

            jedisPool = new JedisPool(jedisPoolConfig, host, port, timeout, password, CHANNEL_INFO_DB);
            jedisPools.put(CHANNEL_INFO_DB, jedisPool);

            jedisPool = new JedisPool(jedisPoolConfig, host, port, timeout, password, STATISTICS_DB);
            jedisPools.put(STATISTICS_DB, jedisPool);

        } catch (Exception ex) {
            logger.error("Redis Util : initialize jedis pool fail." + ex);
        }

    }

    public static void setObject(Integer db, String key, Object object) {
        if (key == null || key.isEmpty() || object == null)
            return;
        JedisPool jedisPool = jedisPools.get(db);
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            byte[] objBytes = serialize(object);
            jedis.set(key.getBytes(), objBytes);
            jedis.expire(key, EXPIRE_SECONDS);
            jedisPool.returnResource(jedis);
        } catch (Exception ex) {
            logger.error("Redis Util : set object fail." + ex);
            if (jedisPool != null)
                jedisPool.returnBrokenResource(jedis);
        }finally {
            if(jedisPool!=null&&jedis!=null){
                jedisPool.returnResource(jedis);
            }
        }
    }

    public static Object getObject(Integer db, String key) {
        if (key == null || key.isEmpty())
            return null;
        JedisPool jedisPool = jedisPools.get(db);
        Jedis jedis = null;
        Object result = null;
        try {
            jedis = jedisPool.getResource();
            byte[] objBytes = jedis.get(key.getBytes());
            result = unserialize(objBytes);
            jedisPool.returnResource(jedis);
        } catch (Exception ex) {
            logger.error("Redis Util : get object fail." + ex);
            if (jedisPool != null)
                jedisPool.returnBrokenResource(jedis);
        }finally {
            if(jedisPool!=null&&jedis!=null){
                jedisPool.returnResource(jedis);
            }
        }

        return result;
    }

    public static void delete(Integer db, String key) {
        if (key == null || key.isEmpty())
            return;
        JedisPool jedisPool = jedisPools.get(db);
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.del(key);
            jedisPool.returnResource(jedis);
        } catch (Exception ex) {
            logger.error("Redis Util : delete fail." + ex);
            if (jedisPool != null)
                jedisPool.returnBrokenResource(jedis);
        }finally {
            if(jedisPool!=null&&jedis!=null){
                jedisPool.returnResource(jedis);
            }
        }
    }

    private static byte[] serialize(Object object) {
        ObjectOutputStream outputStream = null;
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            outputStream = new ObjectOutputStream(baos);
            outputStream.writeObject(object);
            byte[] bytes = baos.toByteArray();
            return bytes;
        } catch (Exception e) {
            logger.error("Redis Util : serialize fail." + e);
        }
        return null;
    }

    private static Object unserialize(byte[] bytes) {
        ByteArrayInputStream bais = null;
        try {
            bais = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            return ois.readObject();
        } catch (Exception e) {
            logger.error("Redis Util : unserialize fail." + e);
        }
        return null;
    }

    public static void setString(Integer db, String key, String value) {
        if (key == null || key.isEmpty() || value == null)
            return;
        JedisPool jedisPool = jedisPools.get(db);
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.set(key, value);
            jedis.expire(key, EXPIRE_SECONDS);
            jedisPool.returnResource(jedis);
        } catch (Exception ex) {
            logger.error("Redis Util : set string fail." + ex);
            if (jedisPool != null)
                jedisPool.returnBrokenResource(jedis);
        }finally {
            if(jedisPool!=null&&jedis!=null){
                jedisPool.returnResource(jedis);
            }
        }
    }

    /**
     * key: redis 中的key
     * map: 待插入redis的hash表
     *
     * @param key
     */
    public static void setHash(Integer db, String key, Map<String, String> map) {
        if (key == null || key.isEmpty() || map == null || map.size() == 0)
            return;
        JedisPool jedisPool = jedisPools.get(db);
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            for (String field : map.keySet()) {
                jedis.hset(key, field, map.get(field));
                jedis.expire(key, EXPIRE_SECONDS);
            }
            jedisPool.returnResource(jedis);
        } catch (Exception ex) {
            logger.error("Redis Util : set hash fail." + ex);
            if (jedisPool != null)
                jedisPool.returnBrokenResource(jedis);
        }finally {
            if(jedisPool!=null&&jedis!=null){
                jedisPool.returnResource(jedis);
            }
        }
    }

    /**
     * key: redis 中的key
     *
     * @param key
     * @param list
     */
    public static void setList(Integer db, String key, List<String> list) {
        if (key == null || key.isEmpty() || list == null || list.size() == 0)
            return;
        JedisPool jedisPool = jedisPools.get(db);
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            for (String value : list) {
                jedis.rpush(key, value);
                jedis.expire(key, EXPIRE_SECONDS);
            }
            jedisPool.returnResource(jedis);
        } catch (Exception ex) {
            logger.error("Redis Util : set list fail." + ex);
            if (jedisPool != null)
                jedisPool.returnBrokenResource(jedis);
        }finally {
            if(jedisPool!=null&&jedis!=null){
                jedisPool.returnResource(jedis);
            }
        }
    }

    public static String getString(Integer db, String key) {
        if (key == null || key.isEmpty())
            return null;
        JedisPool jedisPool = jedisPools.get(db);
        Jedis jedis = null;
        String result = null;
        try {
            jedis = jedisPool.getResource();
            result = jedis.get(key);
            jedisPool.returnResource(jedis);
        } catch (Exception ex) {
            logger.error("Redis Util : set string fail." + ex);
            if (jedisPool != null)
                jedisPool.returnBrokenResource(jedis);
        }finally {
            if(jedisPool!=null&&jedis!=null){
                jedisPool.returnResource(jedis);
            }
        }

        return result;
    }

    public static List<String> getList(Integer db, String key) {
        if (key == null || key.isEmpty())
            return null;
        JedisPool jedisPool = jedisPools.get(db);
        Jedis jedis = null;
        List<String> result = null;
        try {
            jedis = jedisPool.getResource();
            result = jedis.lrange(key, 0, -1);
            jedisPool.returnResource(jedis);
        } catch (Exception ex) {
            logger.error("Redis Util : set string fail." + ex);
            if (jedisPool != null)
                jedisPool.returnBrokenResource(jedis);
        }finally {
            if(jedisPool!=null&&jedis!=null){
                jedisPool.returnResource(jedis);
            }
        }

        return result;
    }

    public static Map<String, String> getHash(Integer db, String key) {
        if (key == null || key.isEmpty())
            return null;
        JedisPool jedisPool = jedisPools.get(db);
        Jedis jedis = null;
        Map<String, String> result = null;
        try {
            jedis = jedisPool.getResource();
            result = jedis.hgetAll(key);
            jedisPool.returnResource(jedis);
        } catch (Exception ex) {
            logger.error("Redis Util : set string fail." + ex);
            if (jedisPool != null)
                jedisPool.returnBrokenResource(jedis);
        }finally {
            if(jedisPool!=null&&jedis!=null){
                jedisPool.returnResource(jedis);
            }
        }

        return result;
    }

    public static boolean connectRedis(Integer db) {
        JedisPool jedisPool = jedisPools.get(db);
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.get("");
            jedisPool.returnResource(jedis);
            return true;
        } catch (Exception ex) {
            logger.error("Redis Util : connection fail." + ex);
            if (jedisPool != null)
                jedisPool.returnBrokenResource(jedis);
            throw ex;
        }finally {
            if(jedisPool!=null&&jedis!=null){
                jedisPool.returnResource(jedis);
            }
        }

    }

    public static Set<String> zrevrange(int db, String key, int start, int stop) {
        JedisPool jedisPool = jedisPools.get(db);
        Jedis jedis = null;
        Set<String> result = null;
        try {
            jedis = jedisPool.getResource();
            result = jedis.zrevrange(key, start, stop);
            jedisPool.returnResource(jedis);
        } catch (Exception ex) {
            logger.error("Redis Util : set string fail." + ex);
            if (jedisPool != null)
                jedisPool.returnBrokenResource(jedis);
        }finally {
            if(jedisPool!=null&&jedis!=null){
                jedisPool.returnResource(jedis);
            }
        }

        return result;
    }

    public static void zadd(int db, String key, long score, String member) {
        JedisPool jedisPool = jedisPools.get(db);
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.zadd(key, score, member);
            jedisPool.returnResource(jedis);
        } catch (Exception ex) {
            logger.error("Redis Util : set string fail." + ex);
            if (jedisPool != null)
                jedisPool.returnBrokenResource(jedis);
        }finally {
            if(jedisPool!=null&&jedis!=null){
                jedisPool.returnResource(jedis);
            }
        }
    }

    public static void delete(int db, String... key) {
        JedisPool jedisPool = jedisPools.get(db);
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.del(key);
            jedisPool.returnResource(jedis);
        } catch (Exception ex) {
            logger.error("Redis Util : set string fail." + ex);
            if (jedisPool != null)
                jedisPool.returnBrokenResource(jedis);
        }finally {
            if(jedisPool!=null&&jedis!=null){
                jedisPool.returnResource(jedis);
            }
        }
    }

    public static void setValueToMap(int db, String key, String field, String value) {
        JedisPool jedisPool = jedisPools.get(db);
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.hset(key, field, value);
            jedisPool.returnResource(jedis);
        } catch (Exception ex) {
            logger.error("Redis Util : set string fail." + ex);
            if (jedisPool != null)
                jedisPool.returnBrokenResource(jedis);
        }finally {
            if(jedisPool!=null&&jedis!=null){
                jedisPool.returnResource(jedis);
            }
        }

    }

    public static String hget(int db, String key, String field) {
        JedisPool jedisPool = jedisPools.get(db);
        Jedis jedis = null;
        String result = null;
        try {
            jedis = jedisPool.getResource();
            result = jedis.hget(key, field);
            jedisPool.returnResource(jedis);
        } catch (Exception ex) {
            logger.error("Redis Util : set string fail." + ex);
            if (jedisPool != null)
                jedisPool.returnBrokenResource(jedis);
        }finally {
            if(jedisPool!=null&&jedis!=null){
                jedisPool.returnResource(jedis);
            }
        }
        return result;
    }

    public static void incr(int db, String key) {
        JedisPool jedisPool = jedisPools.get(db);
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.incr(key);
            jedisPool.returnResource(jedis);
        } catch (Exception ex) {
            logger.error("Redis Util : set string fail." + ex);
            if (jedisPool != null)
                jedisPool.returnBrokenResource(jedis);
        }finally {
            if(jedisPool!=null&&jedis!=null){
                jedisPool.returnResource(jedis);
            }
        }
    }

    public static void zincrBy(int db, String key, int score, String member) {
        JedisPool jedisPool = jedisPools.get(db);
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            Transaction tx = jedis.multi();
            tx.zincrby(key, score, member);
            tx.zremrangeByScore(key, Integer.MIN_VALUE, 0);
            tx.exec();
            jedisPool.returnResource(jedis);
        } catch (Exception ex) {
            logger.error("Redis Util : set string fail." + ex);
            if (jedisPool != null)
                jedisPool.returnBrokenResource(jedis);
        }
    }


}
