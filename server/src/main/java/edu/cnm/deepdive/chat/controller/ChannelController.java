package edu.cnm.deepdive.chat.controller;

import edu.cnm.deepdive.chat.model.entity.Channel;
import edu.cnm.deepdive.chat.service.AbstractChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/channels")
@Validated
@Profile("service")
public class ChannelController {

  private final AbstractChannelService channelService;

  @Autowired
  ChannelController(AbstractChannelService channelService) {
    this.channelService = channelService;
  }

  @GetMapping(produces = MediaType. APPLICATION_JSON_VALUE)
  public Iterable<Channel> get() {
    return channelService.getAll();
  }

}
