package edu.cnm.deepdive.chat.service.dao;

import edu.cnm.deepdive.chat.model.entity.Channel;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

public interface ChannelRepository extends CrudRepository<Channel, Long> {

  Optional<Channel> findByExternalKey(UUID externalKey);

  Iterable<Channel> GetAllByOrderByTitleAsc();

}
