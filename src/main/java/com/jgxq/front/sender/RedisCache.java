package com.jgxq.front.sender;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jgxq.core.enums.CommonErrorCode;
import com.jgxq.core.exception.SmartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author LuCong
 * @since 2020-12-07
 **/
@Component
public class RedisCache {

    @Autowired
    private StringRedisTemplate redisTemplate;


    public void set(String key, Object value) {
        String obj = JSON.toJSONString(value);
        redisTemplate.opsForValue().set(key, obj);
    }

    public void set(String key, Object value, long timeout, TimeUnit unit) {
        String obj = JSON.toJSONString(value);
        redisTemplate.opsForValue().set(key, obj, timeout, unit);
    }

    public <T> T get(String key) {
        return (T) redisTemplate.opsForValue().get(key);
    }

    public void hset(String key, String propName, Object value) {
        redisTemplate.opsForHash().put(key, propName, value);
    }

    public <T> T hget(String key, String propName) {
        return (T) redisTemplate.opsForHash().get(key, propName);
    }


    public void sadd(String key, String value) {
        redisTemplate.opsForSet().add(key, value);
    }

    public Long scard(String key){
        return redisTemplate.opsForSet().size(key);
    }

    public void hsetMap(String key, Map value, long timeout, TimeUnit unit) {
        redisTemplate.opsForHash().putAll(key, value);
        redisTemplate.expire(key, timeout, unit);
    }

    public Byte hgetMap(String key, String hash) {
        Object o = redisTemplate.opsForHash().get(key, hash);
        return (o == null ? null : Byte.valueOf(o.toString()));
//        return redisTemplate.opsForHash().entries(key);
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }

    public int keySize(String key) {
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(key);
        return entries.size();
    }

    public <T> Cursor<T> sscan(String key, ScanOptions options) {
        return (Cursor<T>) redisTemplate.opsForSet().scan(key, options);
    }

    public void zremoveRange(String key, long start, long stop) {
        redisTemplate.opsForZSet().removeRange(key, start, stop);
    }

    public void lladd(String key, Object json) {
        redisTemplate.opsForList().leftPush(key, JSONObject.toJSONString(json));
    }

    public List<Integer> lrangeInt(String key){
        List<String> stringList = redisTemplate.opsForList().range(key, 0, -1);
        List<Integer> res = stringList.stream().map(s -> Integer.parseInt(s)).collect(Collectors.toList());
        return res;
    }

    public void setExpired(String key, Object value, long timeout, TimeUnit unit) {
        if (!(value instanceof String)) {
            String obj = JSON.toJSONString(value);
            redisTemplate.opsForValue().set(key, obj, timeout, unit);
        } else {
            redisTemplate.opsForValue().set(key, (String) value, timeout, unit);
        }

    }

    public <T> T get(String key, Class<T> clazz) throws SmartException {
        T obj = null;
        try {
            obj = clazz.newInstance();
        } catch (Exception e) {
            throw new SmartException(CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorCode(),"instance failed");
        }
        if (obj == null) {
            return null;
        }
        String value = (String) redisTemplate.opsForValue().get(key);
        obj = JSONObject.parseObject(value, clazz);
        return obj;
    }

}
