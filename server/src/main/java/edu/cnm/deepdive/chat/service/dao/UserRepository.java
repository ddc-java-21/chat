package edu.cnm.deepdive.chat.service.dao;

import edu.cnm.deepdive.chat.model.entity.User;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {

  Optional<User> findByOauthKey(String oauthKey);

  Optional<User> findByExternalKey(UUID key);

  User findById(long id);

  Optional<User> findByDisplayName(String displayName);
}
