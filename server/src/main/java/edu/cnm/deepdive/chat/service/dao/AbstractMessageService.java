package edu.cnm.deepdive.chat.service.dao;

import edu.cnm.deepdive.chat.model.entity.Message;
import edu.cnm.deepdive.chat.model.entity.User;
import java.util.UUID;
import org.apache.logging.log4j.CloseableThreadContext.Instance;

public interface AbstractMessageService {

  Iterable<Message> getAllInChannel(UUID channelKey);

  Message getAllInChannelSince(UUID key, Instance cutoff);

  Message add(User author, UUID channelKey, Message message);


}
