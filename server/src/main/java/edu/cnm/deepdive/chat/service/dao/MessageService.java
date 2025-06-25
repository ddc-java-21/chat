package edu.cnm.deepdive.chat.service.dao;

import edu.cnm.deepdive.chat.model.entity.Message;
import edu.cnm.deepdive.chat.model.entity.User;
import java.util.UUID;
import org.apache.logging.log4j.CloseableThreadContext.Instance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageService implements AbstractMessageService {

  private final MessageRepository messageRepository;
  private final ChannelRepository channelRepository;

  @Autowired
  public MessageService(MessageRepository repository, ChannelRepository channelRepository) {
    this.messageRepository = repository;
    this.channelRepository = channelRepository;
  }

  @Override
  public Iterable<Message> getAllInChannel(UUID channelKey) {
    return channelRepository
        .findByExternalKey(channelKey)
        .map(messageRepository::findByChannelOrderByPostedAsc)
            .orElseThrow();
  }

  @Override
  public Message getAllInChannelSince(UUID key, Instance cutoff) {
    //return getAllInChannelSince(channelKey, Instant.Min)
    return channelRepository
        .findByExternalKey(channelKey)
        .map((channel) ->
            messageRepository.findByChannelAndPostedAfterOrderByPostedAsc(channel, cutoff))
        .orElseThrow();
  }

  @Override
  public Message add(User author, UUID channelKey, Message message) {
    return channelRepository
        .findByExternalKey(channelKey)
        .map((channel) -> {
          message.setAuthor(author);
          message.setChannel(channel);
          return messageRepository.save(message);
        })
        .orElseThrow();
  }
}
