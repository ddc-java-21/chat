package edu.cnm.deepdive.chat.service;

import edu.cnm.deepdive.chat.model.dto.Channel;
import edu.cnm.deepdive.chat.model.dto.Message;
import edu.cnm.deepdive.chat.model.dto.User;
import io.reactivex.rxjava3.core.Single;
import java.util.List;
import java.util.UUID;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ChatServiceProxy {

  @GET("users/me")
  Single<User> getMyProfile(@Header("Authorization") String bearerToken);

  @GET("users/{userKey}")
  Single<User> getProfile(@Header("Authorization") String bearerToken, @Path("key") String userKey);

  @PUT("users/me")
  Single<User> updateProfile(@Header("Authorization") String bearerToken, @Body User user);

  @GET("channels")
  Single<List<Channel>> getChannels(@Header("Authorization") String bearerToken);

  @GET("channels/{channelKey}/messages")
  Single<List<Message>> getMessages(
      @Header("Authorization") String bearerToken, @Path("channelKey") UUID channelKey);

}
