package edu.cnm.deepdive.chat.service;

import edu.cnm.deepdive.chat.configuration.ChatConfiguration;
import edu.cnm.deepdive.chat.configuration.ChatConfiguration.Polling;
import edu.cnm.deepdive.chat.model.entity.Channel;
import edu.cnm.deepdive.chat.model.entity.Message;
import edu.cnm.deepdive.chat.model.entity.User;
import edu.cnm.deepdive.chat.service.dao.ChannelRepository;
import edu.cnm.deepdive.chat.service.dao.MessageRepository;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

@Service
@Profile("service")
public class MessageService implements AbstractMessageService {

  private static final List<Message> EMPTY_MESSAGE_LIST = List.of();

  private final MessageRepository messageRepository;
  private final ChannelRepository channelRepository;
  private final ScheduledExecutorService scheduler;
  private final long pollingInterval;
  private final long pollingTimeout;

  @Autowired
  MessageService(MessageRepository messageRepository, ChannelRepository channelRepository,
      ChatConfiguration configuration) {
    this.messageRepository = messageRepository;
    this.channelRepository = channelRepository;
    Polling polling = configuration.getPolling();
    scheduler = Executors.newScheduledThreadPool(polling.getPoolSize());
    pollingInterval = polling.getInterval().toMillis();
    pollingTimeout = polling.getTimeout().toMillis();
  }

  @Override
  public Iterable<Message> getAllInChannel(UUID channelKey) {
    return channelRepository
        .findByExternalKey(channelKey)
        .map(messageRepository::findByChannelOrderByPostedAsc)
        .orElseThrow();
  }

  @Override
  public DeferredResult<Iterable<Message>> getAllInChannelSince(UUID channelKey, Instant cutoff) {
    DeferredResult<Iterable<Message>> result = new DeferredResult<>(pollingTimeout);
    ScheduledFuture<?>[] futurePolling = new ScheduledFuture<?>[1];
    Runnable timeoutTask = () -> sendResult(futurePolling, result, EMPTY_MESSAGE_LIST);
    result.onTimeout(timeoutTask);
    Channel channel = channelRepository
        .findByExternalKey(channelKey)
        .orElseThrow();
    Runnable pollingTask = () -> checkForMessages(cutoff, channel, futurePolling, result);
    futurePolling[0] =
        scheduler.scheduleAtFixedRate(pollingTask, 0, pollingInterval, TimeUnit.MILLISECONDS);
    return result;
  }

  private void checkForMessages(Instant cutoff, Channel channel, ScheduledFuture<?>[] futurePolling,
      DeferredResult<Iterable<Message>> result) {
    messageRepository
        .findFirstByChannelAndPostedAfterOrderByPostedDesc(channel, cutoff)
        .ifPresent((posted) -> sendResult(futurePolling, result,
              messageRepository.findByChannelAndPostedAfterOrderByPostedAsc(channel, cutoff)));
  }

  private void sendResult(ScheduledFuture<?>[] futurePolling,
      DeferredResult<Iterable<Message>> result, Iterable<Message> messages) {
    futurePolling[0].cancel(true);
    result.setResult(messages);
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
