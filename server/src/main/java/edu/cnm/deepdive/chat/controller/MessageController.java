package edu.cnm.deepdive.chat.controller;

import edu.cnm.deepdive.chat.model.entity.Message;
import edu.cnm.deepdive.chat.service.AbstractMessageService;
import edu.cnm.deepdive.chat.service.AbstractUserService;
import jakarta.validation.Valid;
import java.net.URI;
import java.time.Instant;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

@RestController
@RequestMapping("/channels/{channelKey}/messages")
@Validated
@Profile("service")
public class MessageController {

  private final AbstractMessageService messageService;
  private final AbstractUserService userService;

  @Autowired
  MessageController(AbstractMessageService messageService, AbstractUserService userService) {
    this.messageService = messageService;
    this.userService = userService;
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Iterable<Message> get(@PathVariable UUID channelKey) {
    return messageService.getAllInChannel(channelKey);
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, params = "since")
  public DeferredResult<Iterable<Message>> get(@PathVariable UUID channelKey, @RequestParam Instant since) {
    return messageService.getAllInChannelSince(channelKey, since);
  }

  @GetMapping(path = "/{messageKey}", produces = MediaType.APPLICATION_JSON_VALUE)
  public Message get(@PathVariable UUID channelKey, @PathVariable UUID messageKey) {
    return messageService.get(channelKey, messageKey);
  }

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Message> post(@PathVariable UUID channelKey, @Valid @RequestBody Message message) {
    Message created = messageService.add(userService.getCurrentUser(), channelKey, message);
    URI location = WebMvcLinkBuilder.linkTo(
            WebMvcLinkBuilder.methodOn(getClass())
                .get(channelKey, created.getExternalKey())
        )
        .toUri();
    return ResponseEntity.created(location).body(created);
  }

}
