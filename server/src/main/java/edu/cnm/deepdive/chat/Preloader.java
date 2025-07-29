package edu.cnm.deepdive.chat;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cnm.deepdive.chat.model.entity.Channel;
import edu.cnm.deepdive.chat.model.entity.User;
import edu.cnm.deepdive.chat.service.dao.ChannelRepository;
import edu.cnm.deepdive.chat.service.dao.UserRepository;
import java.io.InputStream;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
@Profile("preload")
public class Preloader implements CommandLineRunner {

  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;
  private final String preloadFile;

  @Autowired
  Preloader(UserRepository userRepository, ChannelRepository channelRepository,
      @Value("${chat.preload.file}") String preloadFile) {
    this.userRepository = userRepository;
    this.channelRepository = channelRepository;
    this.preloadFile = preloadFile;
  }

  @Override
  public void run(String... args) throws Exception {
    Resource channelData = new ClassPathResource(preloadFile);
    try (InputStream input = channelData.getInputStream()) {
      ObjectMapper mapper = new ObjectMapper();
      Channel[] channels = mapper.readValue(input, Channel[].class);
      channelRepository.saveAll(Arrays.asList(channels));
      User user = new User();
      user.setOauthKey("\u0000".repeat(User.MAX_OAUTH_KEY_LENGTH));
      user.setDisplayName("ChatBot");
      userRepository.save(user);
    }
  }

}
