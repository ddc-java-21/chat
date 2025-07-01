package edu.cnm.deepdive.chat.hilt;

import com.google.gson.JsonDeserializer;
import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import dagger.multibindings.IntoSet;
import edu.cnm.deepdive.chat.view.serialization.InstantDeserializer;
import java.time.Instant;

@Module
@InstallIn(SingletonComponent.class)
public interface DeserializerModule {
  
  @Binds
  JsonDeserializer<Instant> bindInstantDeserializer(InstantDeserializer deserializer);
  
}
