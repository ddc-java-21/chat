package edu.cnm.deepdive.chat.service;

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

  private static final long POLLING_TIMEOUT_MS = 25_000L;
  private static final long POLLING_INTERVAL_MS = 3_000L;
  private static final int POLLING_POOL_SIZE = 4;
  private static final List<Message> EMPTY_MESSAGE_LIST = List.of();

  private final MessageRepository messageRepository;
  private final ChannelRepository channelRepository;
  private final ScheduledExecutorService scheduler;

  @Autowired
  MessageService(MessageRepository messageRepository, ChannelRepository channelRepository) {
    this.messageRepository = messageRepository;
    this.channelRepository = channelRepository;
    this.scheduler = Executors.newScheduledThreadPool(POLLING_POOL_SIZE);
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
    DeferredResult<Iterable<Message>> result =
        new DeferredResult<>(POLLING_TIMEOUT_MS);
    ScheduledFuture<?>[] futurePolling = new ScheduledFuture[1];
    Runnable timeoutTask = () -> {
      futurePolling[0].cancel(true);
      result.setResult(EMPTY_MESSAGE_LIST);
    };
    result.onTimeout(timeoutTask);
      Channel channel = channelRepository
          .findByExternalKey(channelKey)
          .orElseThrow();
    Runnable pollingTask = checkForMessages(cutoff, channel, result, futurePolling);
    futurePolling[0] =
        scheduler.scheduleAtFixedRate(pollingTask, 0, POLLING_INTERVAL_MS, TimeUnit.MILLISECONDS);
    return result;
  }

  private Runnable checkForMessages(Instant cutoff, Channel channel,
      DeferredResult<Iterable<Message>> result, ScheduledFuture<?>[] futurePolling) {
    Runnable pollingTask = () -> messageRepository.findFirstByChannelAndPostedAfterOrderByPostedDesc(
            channel, cutoff)
        .ifPresent((posted) -> sendResult(cutoff, result, channel, futurePolling));
    return pollingTask;
  }

  private void sendResult(Instant cutoff, DeferredResult<Iterable<Message>> result, Channel channel,
      ScheduledFuture<?>[] futurePolling) {
    result.setResult(
        messageRepository.findByChannelAndPostedAfterOrderByPostedAsc(channel, cutoff));
    futurePolling[0].cancel(true);
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
