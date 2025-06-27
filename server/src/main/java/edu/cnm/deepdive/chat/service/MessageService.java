package edu.cnm.deepdive.chat.service;

import edu.cnm.deepdive.chat.model.entity.Message;
import edu.cnm.deepdive.chat.model.entity.User;
import edu.cnm.deepdive.chat.service.dao.ChannelRepository;
import edu.cnm.deepdive.chat.service.dao.MessageRepository;
import java.time.Instant;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("service")
public class MessageService implements AbstractMessageService {

  private final MessageRepository messageRepository;
  private final ChannelRepository channelRepository;

  @Autowired
  MessageService(MessageRepository messageRepository, ChannelRepository channelRepository) {
    this.messageRepository = messageRepository;
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
  public Iterable<Message> getAllInChannelSince(UUID channelKey, Instant cutoff) {
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

  @Override
  public Message get(UUID channelKey, UUID messageKey) {
    return channelRepository
        .findByExternalKey(channelKey)
        .flatMap((channel) -> messageRepository.findByChannelAndExternalKey(channel, messageKey))
        .orElseThrow();

  }

}
