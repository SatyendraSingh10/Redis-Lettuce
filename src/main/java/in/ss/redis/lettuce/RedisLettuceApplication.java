package in.ss.redis.lettuce;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.core.StringRedisTemplate;

@SpringBootApplication
@Slf4j
public class RedisLettuceApplication implements CommandLineRunner {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    public static void main(String[] args) {
        SpringApplication.run(RedisLettuceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("test key value : " + stringRedisTemplate.opsForValue().get("test"));
    }
}

