package edu.cnm.deepdive.chat.service;

import android.content.Context;
import android.content.Intent;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import dagger.hilt.android.qualifiers.ApplicationContext;
import edu.cnm.deepdive.chat.R;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleEmitter;
import io.reactivex.rxjava3.schedulers.Schedulers;
import javax.inject.Inject;
import javax.inject.Singleton;

/** @noinspection deprecation*/
@Singleton
public class GoogleSignInService {

  private final GoogleSignInClient client;

  @Inject
  GoogleSignInService(@ApplicationContext Context context) {
    GoogleSignInOptions options = new GoogleSignInOptions.Builder()
        .requestEmail()
        .requestProfile()
        .requestId() // gets oauthKey --> see if they are already in our database
        .requestIdToken(context.getString(R.string.client_id))
        .build();
    client = GoogleSignIn.getClient(context, options);
  }

  public Single<GoogleSignInAccount> refresh() { // silent refresh
    return Single.create((SingleEmitter<GoogleSignInAccount> emitter) -> client
          .silentSignIn()
          .addOnSuccessListener(emitter::onSuccess)
          .addOnFailureListener(emitter::onError)
        )
        .observeOn(Schedulers.io());
  }

  public void startSignIn(@NonNull ActivityResultLauncher<Intent> launcher) {
    launcher.launch(client.getSignInIntent()); // we don't create intent; we got it from GoogleSignInClient object and use it to launch
  }

  public Single<GoogleSignInAccount> completeSignIn(ActivityResult result) { // ActivityResult = a contract, built on top of intent
    return Single.create((SingleEmitter<GoogleSignInAccount> emitter) -> {
          try {
            GoogleSignInAccount account =
                GoogleSignIn.getSignedInAccountFromIntent(result.getData())
                    .getResult(ApiException.class);
            emitter.onSuccess(account); // if we get that account, we give it to our emitter
          } catch (ApiException e) {
            emitter.onError(e);
          }
        })
        .subscribeOn(Schedulers.io());
  }

  public Completable signOut() {
    return Completable.create((emitter) ->
            client
                .signOut()
                .addOnSuccessListener((ignored) -> emitter.onComplete()) // onComplete: pass success flag downstream; onSuccess: pass data downstream
                .addOnFailureListener(emitter::onError)
        )
        .observeOn(Schedulers.io());
  } // if sign-out fails, we're pretending it succeeded (go on as if it did)

}
