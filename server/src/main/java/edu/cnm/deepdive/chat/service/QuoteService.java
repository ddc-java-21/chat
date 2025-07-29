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
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Profile("service")
public class QuoteService {

  private static final long POLLING_INTERVAL_MS = 30_000;
  private static final long IDLE_TIME_THRESHOLD_MS = 90_000;
  private static final String ZEN_QUOTE_URL = "https://zenquotes.io/api/random";
  private static final String QUOTE_FORMAT = "\"%1$s\"—%2$s";

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

  @Scheduled(fixedRate = POLLING_INTERVAL_MS)
  public void addQuote() {
    Instant cutoff = Instant.now().minusMillis(IDLE_TIME_THRESHOLD_MS);
    List<Channel> channels = channelRepository.findByIdleTime(cutoff);
    if (!channels.isEmpty()) {
      RestTemplate template = new RestTemplate();
      ResponseEntity<ZenQuote[]> response = template.getForEntity(ZEN_QUOTE_URL, ZenQuote[].class);
      ZenQuote[] quotes;
      if (
          response.getStatusCode().is2xxSuccessful()
              && (quotes = response.getBody()) != null
              && quotes.length > 0
      ) {
        ZenQuote quote = quotes[0];
        userRepository
            .findById(1L) // must use L here, b/c Integer is not a subclass of Long (even though int can be widened to long, Integer cannot to Long)
            .map((user) -> channels
                .stream()
                .map((channel) -> {
                  Message message = new Message();
                  message.setChannel(channel);
                  message.setAuthor(user);
                  message.setText(QUOTE_FORMAT.formatted(quote.getQuote(), quote.getAuthor()));
                  return message;
                })
                .toList()
            )
            .ifPresent(messageRepository::saveAll);

      }
    }
  }

}
