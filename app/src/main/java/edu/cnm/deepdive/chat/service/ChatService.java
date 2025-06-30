package edu.cnm.deepdive.chat.service;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import dagger.hilt.android.qualifiers.ApplicationContext;
import edu.cnm.deepdive.chat.R;
import edu.cnm.deepdive.chat.model.dto.Channel;
import edu.cnm.deepdive.chat.model.dto.User;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

/** @noinspection deprecation*/
@Singleton
public class ChatService {

  private static final String BEARER_TOKEN_FORMAT = "Bearer %s";

  private final GoogleSignInService signInService;
  private final ChatServiceProxy proxy;
  private final ChatServiceLongPollingProxy longPollingProxy;
  private final SharedPreferences preferences;
  private final String historyCutoffKey;
  private final int historyCutoffDefault;
  private final Scheduler scheduler;

  @Inject
  public ChatService(@ApplicationContext Context context,
      @NonNull GoogleSignInService signInService,
      @NonNull ChatServiceProxy proxy,
      @NonNull ChatServiceLongPollingProxy longPollingProxy) {
    this.signInService = signInService;
    this.proxy = proxy;
    this.longPollingProxy = longPollingProxy;
    preferences = PreferenceManager.getDefaultSharedPreferences(context);
    historyCutoffKey = context.getString(R.string.history_cutoff_key);
    historyCutoffDefault = context.getResources().getInteger(R.integer.history_cutoff_default);
    scheduler = Schedulers.single();
  }

  public Single<User> getMyProfile() {
    return  getBearerToken()
        .flatMap(proxy::getMyProfile);
  }

  public Single<List<Channel>> getChannels() {
    return   getBearerToken()
        .flatMap(proxy::getChannels);
  }

  // TODO: 6/30/2025 Add methods for sending and receiving messages

  /** @noinspection ReactiveStreamsNullableInLambdaInTransform*/
  private Single<String> getBearerToken() {
    return  signInService
        .refresh()
        .map(GoogleSignInAccount::getIdToken)
        .map((token) -> String.format(BEARER_TOKEN_FORMAT, token))
        .observeOn(scheduler);
  }

}
