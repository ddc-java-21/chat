package edu.cnm.deepdive.chat.service;

import edu.cnm.deepdive.chat.model.dto.User;
import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface ChatServiceProxy {

  @GET("users/me")
  Single<User> getMyProfile(@Header("Authorization") String bearerToken);

}
