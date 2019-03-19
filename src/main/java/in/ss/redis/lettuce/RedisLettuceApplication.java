package in.ss.redis.lettuce;

import in.ss.redis.lettuce.models.Note;
import in.ss.redis.lettuce.repositories.NoteRepository;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@Slf4j
public class RedisLettuceApplication implements CommandLineRunner {

    @Autowired
    private
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    private
    NoteRepository noteRepository;

    public static void main(String[] args) {
        SpringApplication.run(RedisLettuceApplication.class, args);
    }

    @Override
    @SneakyThrows
    public void run(String... args) {
        
        /*insertIncrementalNotesByMessagesCount(50000);
        readNotesByMessagesCount(50000);*/
        /*insertingInMap(100000);*/
        long startTime = System.currentTimeMillis();

        insertIncrementalNotesByMessagesCountByPipelining(0, 10000);
        insertIncrementalNotesByMessagesCountByPipelining(10001, 20000);
        insertIncrementalNotesByMessagesCountByPipelining(20001, 30000);
        insertIncrementalNotesByMessagesCountByPipelining(30001, 40000);
        insertIncrementalNotesByMessagesCountByPipelining(40001, 50000);
       
        readIncrementalNotesByMessagesCountByPipelining(0, 10000);
        readIncrementalNotesByMessagesCountByPipelining(10001, 20000);
        readIncrementalNotesByMessagesCountByPipelining(20001, 30000);
        readIncrementalNotesByMessagesCountByPipelining(30001, 40000);
        readIncrementalNotesByMessagesCountByPipelining(40001, 50000);

        long endTime = System.currentTimeMillis();
        long timeTaken = endTime - startTime;
        log.info("Total time taken " + timeTaken + " milliseconds.");

    }

    private void insertIncrementalNotesByMessagesCount(int limit) {
        log.info("Inserting Messages.");
        long startTime = System.currentTimeMillis();
        Note note = new Note();
        int count = 0;
        while (count < limit) {
            note.setId("mid:" + count);
            note.setMessage("message : " + count);
            noteRepository.save(note);
            count++;
        }
        long endTime = System.currentTimeMillis();
        long timeTaken = endTime - startTime;
        log.info(count + " messages inserted in redis in " + timeTaken + " milliseconds.");
    }

    private void insertIncrementalNotesByMessagesCountByPipelining(int startCounter, int limit) {
        log.info("Inserting Messages using pipeline.");
        long startTime = System.currentTimeMillis();
        stringRedisTemplate.executePipelined(
                (RedisCallback<Object>) connection -> {
                    StringRedisConnection stringRedisConn = (StringRedisConnection) connection;
                    int count = startCounter;
                    while (count < limit) {
                        Map<String, String> map = new HashMap<>();
                        map.put("mid:" + count, "message:" + count);
                        stringRedisConn.hMSet("hbr:rl:note:hash:" + count, map);
                        stringRedisConn.sAdd("hbr:rl:note:hash", "message:" + count);
                        count++;
                    }
                    return null;
                });
        long endTime = System.currentTimeMillis();
        long timeTaken = endTime - startTime;
        int messagesInserted = limit - startCounter;
        log.info(messagesInserted + " messages inserted in redis in " + timeTaken + " milliseconds.");
    }

    private void readIncrementalNotesByMessagesCountByPipelining(int startCounter, int limit) {
        log.info("Reading messages using pipeline.");
        long startTime = System.currentTimeMillis();
        /*List<Object> results = */
        stringRedisTemplate.executePipelined(
                (RedisCallback<Object>) connection -> {
                    StringRedisConnection stringRedisConn = (StringRedisConnection) connection;
                    int count = startCounter;
                    while (count < limit) {
                        stringRedisConn.hGetAll("hbr:rl:note:hash:" + count);
                        count++;
                    }
                    return null;
                });
        long endTime = System.currentTimeMillis();
        long timeTaken = endTime - startTime;
        int messagesInserted = limit - startCounter;
        log.info(messagesInserted + " messages read from redis in " + timeTaken + " milliseconds.");
    }

    private void readNotesByMessagesCount(int limit) {
        log.info("Records Reading started");
        long startTime = System.currentTimeMillis();
        int count = 0;
        while (count < limit) {
            noteRepository.findById("mid:" + count);
            count++;
        }
        long endTime = System.currentTimeMillis();
        long timeTaken = endTime - startTime;
        log.info(count + " messages read from redis in " + timeTaken + " milliseconds.");
    }

    private void insertingInMap(int limit) {
        log.info("Inserting Messages.");
        long startTime = System.currentTimeMillis();
        HashMap<Integer, Note> hash = new HashMap<>();
        int count = 0;
        while (count < limit) {
            Note note = new Note("mid:" + count, "message : " + count);
            hash.put(count, note);
            count++;
        }
        long endTime = System.currentTimeMillis();
        long timeTaken = endTime - startTime;
        log.info(count + " messages inserted in map in " + timeTaken + " milliseconds.");
    }
}

