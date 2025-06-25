package edu.cnm.deepdive.chat.service;

import edu.cnm.deepdive.chat.model.entity.Channel;
import edu.cnm.deepdive.chat.service.dao.ChannelRepository;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("service")
public class ChannelService implements AbstractChannelService {

  private final ChannelRepository repository;

  @Autowired
  public ChannelService(ChannelRepository repository) {
    this.repository = repository;
  }

  @Override
  public Iterable<Channel> getAll() {
    return repository.getAllByOrderByTitleAsc();
  }

  @Override
  public Channel get(UUID key) {
    return repository
        .findByExternalKey(key)
        .orElseThrow();
  }
}
