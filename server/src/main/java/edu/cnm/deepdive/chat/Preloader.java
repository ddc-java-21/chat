package edu.cnm.deepdive.chat;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cnm.deepdive.chat.model.entity.Channel;
import edu.cnm.deepdive.chat.service.dao.ChannelRepository;
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

  private final ChannelRepository repository;
  private final String preloadFile;

  @Autowired
  Preloader(ChannelRepository repository,
      @Value("${chat.preload.file}") String preloadFile) {
    this.repository = repository;
    this.preloadFile = preloadFile;
  }

  @Override
  public void run(String... args) throws Exception {
    Resource channelData = new ClassPathResource(preloadFile);
    try (InputStream input = channelData.getInputStream()) {
      ObjectMapper mapper = new ObjectMapper();
      Channel[] channels = mapper.readValue(input, Channel[].class);
      repository.saveAll(Arrays.asList(channels));
    }
  }
}
