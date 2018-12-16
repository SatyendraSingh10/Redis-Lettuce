package in.ss.redis.lettuce.repositories;

import in.ss.redis.lettuce.models.Note;
import org.springframework.data.repository.CrudRepository;

/**
 * @author Satyendra Singh
 * @project Redis-Lettuce
 * @package in.ss.redis.lettuce.repositories
 * @date 16-12-2018
 * @time 19:17
 */
public interface NoteRepository extends CrudRepository<Note, String> {
}
