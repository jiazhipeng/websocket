package com.qczl.websocket.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.Tuple;
import redis.clients.jedis.exceptions.JedisException;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * redis工具类
 */
public class RedisUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisUtil.class);

    /**
     * jedis 连接池
     **/
    private JedisPool pool;

    /**
     * 将hash表key中的field设置为value，如果hash表不存则生成一个
     *
     * @param key
     * @param field
     * @param value
     */
    public  void hset(String key, String field, String value) {
        Jedis jedis = null;
        try {
            jedis = this.pool.getResource();
            jedis.hset(key, field, value);
        } catch (Exception e) {
            LOGGER.error("RedisUtil.hset exception {}", e);
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /***
     * 逆向返回score在min~max中的值
     * @param key
     * @param max
     * @param min
     * @return
     */
    public Set<String> zrevrangeByScore(final String key, final double max, final double min){
        Jedis jedis = null;
        try {
            jedis = this.pool.getResource();
            return jedis.zrevrangeByScore(key,max,min);
        } catch (Exception e) {
            LOGGER.error("RedisUtil.zrevrangeByScore exception {}", e);
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /***
     * 逆向返回score在min~max中的值 并分页 从offset开始取count条
     * @param key
     * @param max
     * @param min
     * @return
     */
    public Set<String> zrevrangeByScore(final String key, final double max, final double min,final int offset, final int count){
        Jedis jedis = null;
        try {
            jedis = this.pool.getResource();
            return jedis.zrevrangeByScore(key,max,min,offset,count);
        } catch (Exception e) {
            LOGGER.error("RedisUtil.zrevrangeByScore exception {}", e);
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }


    public Jedis getJedis(){
        return this.pool.getResource();
    }

    /**
     * 把对象放入Hash中
     */
    public  void hset(String key, String field, String value, int seconds) {
        this.hset(key, field, value);
        this.expire(key, seconds);
    }

    /**
     * 存储string对象，并且设置其过期值
     *
     * @param key
     * @param value
     * @param seconds
     */
    public  void set(String key, String value, int seconds) {
        this.set(key, value);
        this.expire(key, seconds);
    }

    /**
     * 删除一个或多个key
     *
     * @param key
     */
    public  void del(String... key) {
        Jedis jedis = null;
        try {
            jedis = this.pool.getResource();
            jedis.del(key);
        } catch (Exception e) {
            LOGGER.error("RedisUtil.del exception:{}" , e);
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }


    /**
     * 根据前缀删除key
     * @param prefix
     */
    public  void delByPrefix(String prefix) {
        Jedis jedis = null;
        try {
            jedis = this.pool.getResource();

            Set<String> set = jedis.keys(prefix + "*");

            if(!CollectionUtils.isEmpty(set)){
                jedis.del(set.toArray(new String[]{}));
            }
        } catch (Exception e) {
            LOGGER.error("RedisUtil.del exception:{}" ,e);
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }


    /**
     * 重命名rediskey值
     *
     * @param oldKey
     * @param newKey
     */
    public  void rename(String oldKey, String newKey) {
        Jedis jedis = null;
        try {
            jedis = this.pool.getResource();
            jedis.rename(oldKey, newKey);
        } catch (Exception e) {
            LOGGER.error("RedisUtil.rename oldKey:[{}],newKey:[{}],exception:[{}]", oldKey , newKey , e);
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 判断redis中是否有指定key
     *
     * @param key
     * @return
     */
    public  Set<String> keys(String key) {
        Jedis jedis = null;
        try {
            jedis = this.pool.getResource();
            return jedis.keys(key);
        } catch (Exception e) {
            LOGGER.error("RedisUtil.keys key:[{}] exception:[{}]",key, e);
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
            	jedis.close();
            }
        }
    }

    /**
     * 存储序列化对象到数组中
     *
     * @param key
     * @param value
     */
    public  void lpush(byte[] key, byte[] value) {
        Jedis jedis = null;
        try {
            jedis = this.pool.getResource();
            jedis.lpush(key, value);
        } catch (Exception e) {
            LOGGER.error("RedisUtil.lpush exception {}", e);
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 将一个值value插入到列表key的表头
     *
     * @param key
     * @param value
     */
    public  void lpush(String key, String ... value) {
        Jedis jedis = null;
        try {
            jedis = this.pool.getResource();
            jedis.lpush(key, value);
        } catch (Exception e) {
            LOGGER.error("RedisUtil.lpush exception {}", e);
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 移除并获取列表最后一个元素
     *
     * @param key
     * @return
     */
    public  String rpop(String key) {
        Jedis jedis = null;
        try {
            jedis = this.pool.getResource();
            return jedis.rpop(key);
        } catch (Exception e) {
            LOGGER.error("RedisUtil.lpop exception {}", e);
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 移除并返回列表key的头元素
     *
     * @param key
     * @return
     */
    public  String lpop(String key) {
        Jedis jedis = null;
        try {
            jedis = this.pool.getResource();
            return jedis.lpop(key);
        } catch (Exception e) {
            LOGGER.error("RedisUtil.lpop exception {}", e);
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public  List<String> blpop(int timeout,String key) {
        Jedis jedis = null;
        try {
            jedis = this.pool.getResource();
            return jedis.blpop(timeout,key);
        } catch (Exception e) {
            LOGGER.error("RedisUtil.blpop exception {}", e);
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }
    
    /**
     * 将 key 中储存的数字值增加1。如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 INCR 操作。
     * @param key
     */
    public Long incr(String key) {
        Jedis jedis = null;
        try {
            jedis = this.pool.getResource();
            return jedis.incr(key);
        } catch (Exception e) {
            LOGGER.error("RedisUtil.incr exception {}", e);
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }


    public  void incr(String key, int count) {
        Jedis jedis = null;
        try {
            jedis = this.pool.getResource();
            for (int i = 0; i < count; i++) {
                jedis.incr(key);
            }
        } catch (Exception e) {
            LOGGER.error("RedisUtil.incr exception {}", e);
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }


    /**
     * 将hash 中储存的数字值减一,并且保证不为负数
     *
     * @param key
     * @param field
     * @return true=成功 fasle=剩余的值为零 或 key不存在
     */
    public  boolean hdecr(String key, String field) {
        Jedis jedis = null;
        try {
            jedis = this.pool.getResource();
            if (jedis.hexists(key, field)) {
                String res = jedis.hget(key, field);
                if (Integer.valueOf(res) > 0) {
                    jedis.hincrBy(key, field, -1);
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            LOGGER.error("RedisUtil.incr exception {}", e);
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 将hash 中储存的数字值加一,并且保证不为负数
     *
     * @param key
     * @param field
     */
    public  Long hincr(String key, String field) {
        Jedis jedis = null;
        try {
            jedis = this.pool.getResource();
            return jedis.hincrBy(key, field, 1);
        } catch (Exception e) {
            LOGGER.error("RedisUtil.incr exception {}", e);
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 删除哈希表 key 中的一个或多个指定field，不存在的field将被忽略
     *
     * @param key
     * @param fields
     */
    public  void hdel(String key, String... fields) {
        Jedis jedis = null;
        try {
            jedis = this.pool.getResource();
            jedis.hdel(key, fields);
        } catch (Exception e) {
            LOGGER.error("RedisUtil.hdel exception {}", e);
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 将字符串值 value 关联到 key
     *
     * @param key
     * @param value
     */
    public  void set(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = this.pool.getResource();
            jedis.set(key, value);
        } catch (Exception e) {
            LOGGER.error("RedisUtil.set exception {}", e);
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 将字符串值 value 关联到 key
     *
     * @param key
     * @param value
     */
    public  void set(byte[] key, byte[] value) {

        Jedis jedis = null;
        try {
            jedis = this.pool.getResource();
            jedis.set(key, value);
        } catch (Exception e) {
            LOGGER.error("RedisUtil.set exception {}", e);
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 返回 key 所关联的字符串值。
     *
     * @param key
     * @return
     */

    public  String get(String key) {
        Jedis jedis = null;
        try {
            jedis = this.pool.getResource();
            String value = jedis.get(key);
            return value;
        } catch (Exception e) {
            LOGGER.error("RedisUtil.get exception {}", e);
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * Description: 获取hash缓存内容，如果key/field不存在则返回null
     *
     * @param key
     * @param field
     * @return key-field对应的value
     */
    public  String get(String key, String field) {
        return this.hget(key, field);
    }

    /**
     * 返回 key 所关联的字符串值。
     *
     * @param key
     * @return
     */

    public  byte[] get(byte[] key) {
        Jedis jedis = null;
        try {
            jedis = this.pool.getResource();
            byte[] value = jedis.get(key);
            return value;
        } catch (Exception e) {
            LOGGER.error("RedisUtil.get exception {}", e);
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 将多个field - value(域-值)对设置到哈希表key中。
     *
     * @param key
     * @param map
     */
    public  void hmset(String key, Map<String, String> map) {
        Jedis jedis = null;
        try {
            jedis = this.pool.getResource();
            jedis.hmset(key, map);
        } catch (Exception e) {
            LOGGER.error("RedisUtil.hmset exception {}", e);
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 返回哈希表 key 中，一个或多个给定field的值
     *
     * @param key
     * @param fields
     * @return
     */
    public   List<String> hmget(String key, String... fields) {
        Jedis jedis = null;
        try {
            jedis = this.pool.getResource();
            return jedis.hmget(key, fields);
        } catch (Exception e) {
            LOGGER.error("RedisUtil.hmset exception {}", e);
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 将值 value 关联到 key ，并将 key 的生存时间设为 seconds (以秒为单位)
     *
     * @param key
     * @param seconds
     * @param value
     */

    public  void setex(String key, int seconds, String value) {
        Jedis jedis = null;
        try {
            jedis = this.pool.getResource();
            jedis.setex(key, seconds, value);
        } catch (Exception e) {
            LOGGER.error("RedisUtil.setex exception {}", e);
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 为给定 key 设置生存时间，当 key 过期时/秒(生存时间为 0 )，它会被自动删除
     *
     * @param key
     * @param seconds
     */
    public  void expire(String key, int seconds) {
        Jedis jedis = null;
        try {
            jedis = this.pool.getResource();
            jedis.expire(key, seconds);
        } catch (Exception e) {
            LOGGER.error("RedisUtil.expire exception {}", e);
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 是否存在哈希域
     * @param key
     * @param field
     * @return
     */
    public  boolean hexists(String key, String field) {
        Jedis jedis = null;
        try {
            jedis = this.pool.getResource();
            return jedis.hexists(key, field);
        } catch (Exception e) {
            LOGGER.error("RedisUtil.hexists(String key, String field) exception {}", e);
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * key是否存在
     * @param  key
     * @return boolean    返回类型
     * @throws
     * @Title: exists
     */
    public  boolean exists(String key) {
        Jedis jedis = null;
        try {
            jedis = this.pool.getResource();
            return jedis.exists(key);
        } catch (Exception e) {
            LOGGER.error("RedisUtil.exists(String key) exception{}" , e);
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }


    /**
     * 从哈希表key中获取field的value
     *
     * @param key
     * @param field
     */

    public  String hget(String key, String field) {
        Jedis jedis = null;
        try {
            jedis = this.pool.getResource();
            String value = jedis.hget(key, field);
            return value;
        } catch (Exception e) {
            LOGGER.error("RedisUtil.hget(String key, String field) exception {}", e);
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 从哈希表key中获取field的value
     *
     * @param key
     */

    public  Map<String, String> hgetAll(String key) {
        Jedis jedis = null;
        try {
            jedis = this.pool.getResource();
            Map<String, String> map = jedis.hgetAll(key);
            return map;
        } catch (Exception e) {
            LOGGER.error("RedisUtil.hgetAll(String key) exception {}", e);
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 返回哈希表 key 中给定域 field 指定索引的值
     *
     * @param key
     * @param field
     * @param dbIndex
     * @return
     */
    public  String hget(String key, String field, int dbIndex) {
        Jedis jedis = null;
        try {
            jedis = this.pool.getResource();
            jedis.select(dbIndex);
            String value = jedis.hget(key, field);
            return value;
        } catch (Exception e) {
            LOGGER.error("RedisUtil.hget(String key, String field, int dbIndex) exception:{}" , e);
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 从Hash中获取对象,转换成制定类型
     */
    @SuppressWarnings("unchecked")
    public <T> T hget(String key, String field, Type clazz) {

        String jsonContext = this.get(key, field);

        return (T) JSONObject.parseObject(jsonContext, clazz);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key, Type clazz) {
        String jsonContext = this.get(key);
        if (StringUtils.isEmpty(jsonContext)) {
            return null;
        }
        return (T) JSONObject.parseObject(jsonContext, clazz);
    }

    /**
     * 从哈希表key中获取field的value MAP
     *
     * @param key
     */

    public  Map<String, String> hget(String key) {
        Jedis jedis = null;
        try {
            jedis = this.pool.getResource();
            Map<String, String> value = jedis.hgetAll(key);
            return value;
        } catch (Exception e) {
            LOGGER.error("RedisUtil.hget(String key) exception {}", e);
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 将一个或多个 member 元素加入到集合 key 当中，已经存在于集合的 member 元素将被忽略
     *
     * @param key
     * @param member
     * @return
     */
    public  Long setAdd(String key, String... member) {
        Jedis jedis = null;
        try {
            jedis = this.pool.getResource();
            return jedis.sadd(key, member);
        } catch (Exception e) {
            LOGGER.error("RedisUtil.setAdd(String key, String... member) exception:{}" , e);
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 将数据放入zset中
     *
     * @param key
     * @param scoreMembers
     */
    public  void zadd(String key, Map<String,Double> scoreMembers) {
        Jedis jedis = null;
        try {
            jedis = this.pool.getResource();
            jedis.zadd(key, scoreMembers);
        } catch (Exception e) {
            LOGGER.error("RedisUtil.zadd(String key, Map<Double, String> scoreMembers) exception :{}" , e);
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 将zset中的指定元素的score增长score
     */
    public  void zincrby(String key, double score, String member) {
        Jedis jedis = null;
        try {
            jedis = this.pool.getResource();
            jedis.zincrby(key, score, member);
        } catch (Exception e) {
            LOGGER.error("RedisUtil.zadd(String key, Map<Double, String> scoreMembers) exception:{}", e);
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 将zset中的指定元素的score增长score
     */
    public  double zincrbyReturnScore(String key, double score, String member) {
        Jedis jedis = null;
        double result;
        try {
            jedis = this.pool.getResource();
            result = jedis.zincrby(key, score, member);
        } catch (Exception e) {
            LOGGER.error("RedisUtil.zadd(String key, Map<Double, String> scoreMembers) exception:{}" , e);
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return result;
    }

    /**
     * 反向遍历zset（score大的在前）
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public   Set<String> zrevrange(String key, long start, long end) {
        Jedis jedis = null;
        Set<String> result = null;
        try {
            jedis = this.pool.getResource();
            result = jedis.zrevrange(key, start, end);
        } catch (Exception e) {
            LOGGER.error("exception:{}", e);
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return result;
    }

    /**
     * 反向遍历zset（score大的在前）
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public  Set<Tuple> zrevrangewithscore(String key, long start, long end) {
        Jedis jedis = null;
        Set<Tuple> result = null;
        try {
            jedis = this.pool.getResource();
            result = jedis.zrevrangeWithScores(key, start, end);
        } catch (Exception e) {
            LOGGER.error("RedisUtil.zrevrangewithscore exception {}", e);
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return result;
    }

    /**
     * 返回指定member的score，当member不存在时，返回Null
     *
     * @param key
     * @param member
     * @return
     */
    public  Double zscore(String key, String member) {
        Jedis jedis = null;
        Double result = null;
        try {
            jedis = this.pool.getResource();
            result = jedis.zscore(key, member);
        } catch (Exception e) {
            LOGGER.error("RedisUtil.zrevrangewithscore exception {}", e);
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return result;
    }


    /**
     * <p>通过key获取set中的差集</p>
     * <p>以第一个set为标准</p>
     *
     * @param keys 可以使一个string 则返回set中所有的value 也可以是string数组
     * @return
     */
    public  Set<String> sdiff(String... keys) {
        Jedis jedis = null;
        Set<String> res = null;
        try {
            jedis = this.pool.getResource();
            res = jedis.sdiff(keys);
        } catch (Exception e) {
            LOGGER.error("RedisUtil.sdiff(String... keys) exception {}", e);
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return res;
    }

    /**
     * 返回集合 key 中的成员。
     * @param key
     * @return
     */
    public  Set<String> smembers(String key) {
        Jedis jedis = null;
        Set<String> res = null;
        try {
            jedis = this.pool.getResource();
            res = jedis.smembers(key);
        } catch (Exception e) {
            LOGGER.error("RedisUtil.smembers(String key) exception {}", e);
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return res;
    }

    /**
     * 判断制定成员是否存在。
     *
     * @param key
     * @return
     */
    public   boolean sismembers(String key, String member) {
        Jedis jedis = null;
        boolean res = false;
        try {
            jedis = this.pool.getResource();
            res = jedis.sismember(key, member);
        } catch (Exception e) {
            LOGGER.error("RedisUtil.sismembers(String key, String member) exception:{}",e);
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return res;
    }

    /**
     * 返回列表 key 中指定区间内的元素，区间以偏移量 start 和 stop 指定。
     */
    public  List<String> lrange(String key, long start, long stop) {
        Jedis jedis = null;
        List<String> res = null;
        try {
            jedis = this.pool.getResource();
            res = jedis.lrange(key, start, stop);
        } catch (Exception e) {
            LOGGER.error("RedisUtil.lrange(String key, long start, long stop) exception:{}" , e);
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return res;
    }

    /**
     * 移除并返回集合中的一个元素。
     *
     * @param key
     * @return
     */
    public  String spop(String key) {
        Jedis jedis = null;
        try {
            jedis = this.pool.getResource();
            return jedis.spop(key);
        } catch (Exception e) {
            LOGGER.error("RedisUtil.spop(String key) exception {}", e);
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 返回集合key的元素数量
     *
     * @param key
     * @return
     */
    public   long scard(String key) {
        Jedis jedis = null;
        try {
            jedis = this.pool.getResource();
            return jedis.scard(key);
        } catch (Exception e) {
            LOGGER.error("RedisUtil.scard(String key) exception {}", e);
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 移除集合 key 中的一个或多个 member 元素，不存在的 member 元素会被忽略。
     *
     * @param key
     * @param member
     */
    public   void srem(String key, String... member) {
        Jedis jedis = null;
        try {
            jedis = this.pool.getResource();
            jedis.srem(key, member);
        } catch (Exception e) {
            LOGGER.error("RedisUtil.srem(String key, String... member) exception{}" , e);
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 将 key 中储存的数字值增加count。如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 INCR 操作。
     */
    public  Long incrWithExpire(String key, int expireTime) {
        Jedis jedis = null;
        Long incr = 0L;
        try {
            jedis = this.pool.getResource();
            incr = jedis.incr(key);
            jedis.expire(key, expireTime);
        } catch (Exception e) {
            incr = 0L;
            LOGGER.error("RedisUtil.incr exception {}", e);
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return incr;
    }

    public  Set<String> hkeys(String key) {
        Jedis jedis = null;
        try {
            jedis = this.pool.getResource();
            Set<String> set = jedis.hkeys(key);
            return set;
        } catch (Exception e) {
            LOGGER.error("RedisUtil.hkeys exception {}", e);
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public  Long zcard(String key) {
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = this.pool.getResource();
            result = jedis.zcard(key);
        } catch (Exception e) {
            LOGGER.error("RedisUtil.zrevrangewithscore exception {}", e);
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return result;
    }

    public  Long zrem(String key, String member) {
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = this.pool.getResource();
            result = jedis.zrem(key, member);
        } catch (Exception e) {
            LOGGER.error("RedisUtil.zrevrangewithscore exception {}", e);
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return result;
    }

    public  Long dbsize() {
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = this.pool.getResource();
            result = jedis.dbSize();
        } catch (Exception e) {
            LOGGER.error("RedisUtil.zrevrangewithscore exception {}", e);
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return result;
    }

    public  void clear() {
        Jedis jedis = null;
        try {
            jedis = this.pool.getResource();
            jedis.flushDB();
        } catch (Exception e) {
            LOGGER.error("RedisUtil.zrevrangewithscore exception {}", e);
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public  void subscribe(JedisPubSub jedisPubSub, String channel) {
        Jedis jedis = null;
        try {
            jedis = this.pool.getResource();
            jedis.subscribe(jedisPubSub, channel);
        } catch (Exception e) {
            LOGGER.error("RedisUtil.zrevrangewithscore exception {}", e);
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public  void publish(String channel, String message) {
        Jedis jedis = null;
        try {
            jedis = this.pool.getResource();
            jedis.publish(channel, message);
        } catch (Exception e) {
            LOGGER.error("RedisUtil.zrevrangewithscore exception {}", e);
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public  Long setnx(String key,String value) {
        Jedis jedis = null;
        try {
            jedis = this.pool.getResource();
            return jedis.setnx(key,value);
        } catch (Exception e) {
            LOGGER.error("RedisUtil.setnx exception:{}" , e);
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }
    
    public JedisPool getPool() {
        return this.pool;
    }

    public void setPool(JedisPool pool) {
        this.pool = pool;
    }

}
