package edu.cnm.deepdive.chat.service.dao;

import edu.cnm.deepdive.chat.model.entity.Channel;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ChannelRepository extends CrudRepository<Channel, Long> {

  Optional<Channel> findByExternalKey(UUID externalKey);

  Iterable<Channel> getAllByOrderByTitleAsc();

  @Query("SELECT c FROM Channel AS c WHERE c NOT IN (SELECT DISTINCT c from Channel as c JOIN c.messages AS m where m.posted > :cutoff)")
  List<Channel> findAByIdleTime(Instant cutoff);

}
