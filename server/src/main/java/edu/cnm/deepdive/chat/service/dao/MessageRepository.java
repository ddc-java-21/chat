package edu.cnm.deepdive.chat.service.dao;

import edu.cnm.deepdive.chat.model.entity.Channel;
import edu.cnm.deepdive.chat.model.entity.Message;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

public interface MessageRepository extends CrudRepository<Message, Long> {

  Optional<Message> findByExternalKey(UUID externalKey);

  Iterable<Message> findByChannelOrderByPostedAsc(Channel channel);

//  This is included to show a JPQL implementation of a Spring Data inferred query.
//  @Query("SELECT m FROM Message AS m WHERE m.channel = :channel AND m.posted > :cutoff")
  Iterable<Message> findByChannelAndPostedAfterOrderByPostedAsc(Channel channel, Instant cutoff);

}
