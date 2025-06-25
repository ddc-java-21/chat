package edu.cnm.deepdive.chat.service;

import edu.cnm.deepdive.chat.model.entity.Channel;
import java.util.UUID;

public interface AbstractChannelService {


  Iterable<Channel> getAll();

  Channel get(UUID key);

}
