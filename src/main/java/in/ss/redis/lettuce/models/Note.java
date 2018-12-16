package in.ss.redis.lettuce.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

/**
 * @author Satyendra Singh
 * @project Redis-Lettuce
 * @package in.ss.redis.lettuce.models
 * @date 16-12-2018
 * @time 19:14
 */
@Data
@RedisHash("hbr:rl:note:hash")
@AllArgsConstructor
@NoArgsConstructor
public class Note {
    private String id;
    private String message;
}
