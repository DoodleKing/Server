package mana.doodleking.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {

    private final StringRedisTemplate redisTemplate;

    @Autowired
    public RedisService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // Redis에 데이터 저장
    public void setValue(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    // Redis에서 데이터 가져오기
    public String getValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    // Redis에서 데이터 삭제
    public void deleteValue(String key) {
        redisTemplate.delete(key);
    }
}
