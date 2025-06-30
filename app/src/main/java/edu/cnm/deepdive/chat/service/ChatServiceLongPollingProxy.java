package edu.cnm.deepdive.chat.service;

import edu.cnm.deepdive.chat.model.dto.Channel;
import edu.cnm.deepdive.chat.model.dto.Message;
import edu.cnm.deepdive.chat.model.dto.User;
import io.reactivex.rxjava3.core.Single;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ChatServiceLongPollingProxy {

  @GET("channels/{channelKey}/messages")
  Single<List<Message>> getMessagesSince(@Header("Authorization") String bearerToken,
      @Path("channelKey") UUID channelKey, @Query("since") Instant since);

}
