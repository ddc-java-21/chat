package edu.cnm.deepdive.chat.service;

import edu.cnm.deepdive.chat.model.entity.Message;
import edu.cnm.deepdive.chat.model.entity.User;
import java.time.Instant;
import java.util.UUID;
import org.springframework.web.context.request.async.DeferredResult;

public interface AbstractMessageService {

  Iterable<Message> getAllInChannel(UUID channelKey);

  DeferredResult<Iterable<Message>> getAllInChannelSince(UUID channelKey, Instant cutoff);

  Message add(User author, UUID channelKey, Message message);

  Message get(UUID channelKey, UUID messageKey);

}
