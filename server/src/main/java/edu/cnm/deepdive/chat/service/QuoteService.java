package edu.cnm.deepdive.chat.service;

import edu.cnm.deepdive.chat.model.dto.ZenQuote;
import edu.cnm.deepdive.chat.model.entity.Channel;
import edu.cnm.deepdive.chat.model.entity.Message;
import edu.cnm.deepdive.chat.service.dao.ChannelRepository;
import edu.cnm.deepdive.chat.service.dao.MessageRepository;
import edu.cnm.deepdive.chat.service.dao.UserRepository;
import java.time.Instant;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Profile("service")
public class QuoteService {

  private static final int IDLE_TIME_POLLING_INTERVAL_MS = 30_000;
  private static final int IDLE_TIME_THRESHOLD_MS = 300_000;
  private static final String ZEN_QUOTE_URL = "https://zenquotes.io/api/random";
  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;
  private final MessageRepository messageRepository;

  @Autowired
  public QuoteService(UserRepository userRepository, ChannelRepository channelRepository,
      MessageRepository messageRepository) {
    this.userRepository = userRepository;
    this.channelRepository = channelRepository;
    this.messageRepository = messageRepository;
  }

  @Scheduled(fixedDelay = IDLE_TIME_POLLING_INTERVAL_MS)
  public void addQuote() {
    Instant cutoff = Instant.now().minusMillis(IDLE_TIME_THRESHOLD_MS);
    List<Channel> channels = channelRepository.findByIdleTime(cutoff);
    if (!channels.isEmpty()) {
      RestTemplate restTemplate = new RestTemplate();
      ZenQuote[] quotes = restTemplate.getForObject(ZEN_QUOTE_URL, ZenQuote[].class);
      if (quotes != null && quotes.length > 0) {
        userRepository
            .findById(1L)
            .map((user) -> channels
                .stream()
                .map((channel) -> {
                  Message message = new Message();
                  message.setChannel(channel);
                  message.setAuthor(user);
                  message.setText(quotes[0].getQuote());
                  return message;
                })
                .toList()
            )
            .ifPresent(messageRepository::saveAll);
      }
    }
  }

}
