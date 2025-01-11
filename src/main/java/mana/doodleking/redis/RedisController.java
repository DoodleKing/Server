package mana.doodleking.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/redis")
public class RedisController {

    private final RedisService redisService;

    @Autowired
    public RedisController(RedisService redisService) {
        this.redisService = redisService;
    }

    // Redis에 값 저장
    @PostMapping("/set")
    public ResponseEntity<String> setRedisValue(@RequestParam String key, @RequestParam String value) {
        redisService.setValue(key, value);
        return ResponseEntity.ok("Value saved to Redis.");
    }

    // Redis에서 값 조회
    @GetMapping("/get")
    public ResponseEntity<String> getRedisValue(@RequestParam String key) {
        String value = redisService.getValue(key);
        if (value != null) {
            return ResponseEntity.ok(value);
        } else {
            return ResponseEntity.status(404).body("Value not found.");
        }
    }

    // Redis에서 값 삭제
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteRedisValue(@RequestParam String key) {
        redisService.deleteValue(key);
        return ResponseEntity.ok("Value deleted from Redis.");
    }
}