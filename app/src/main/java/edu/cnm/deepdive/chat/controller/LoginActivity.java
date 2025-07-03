package edu.cnm.deepdive.chat.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.ActivityNavigator.Extras;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import dagger.hilt.android.AndroidEntryPoint;
import edu.cnm.deepdive.chat.LoginNavGraphDirections;
import edu.cnm.deepdive.chat.R;
import edu.cnm.deepdive.chat.databinding.ActivityLoginBinding;
import edu.cnm.deepdive.chat.viewmodel.LoginViewModel;

@AndroidEntryPoint
public class LoginActivity extends AppCompatActivity {

  private static final String TAG = LoginActivity.class.getSimpleName();

  private ActivityLoginBinding binding;
  private AppBarConfiguration appBarConfig;
  private NavController navController;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setupUI();
    setupNavigation();
    setupViewModel();
  }

  @Override
  public boolean onSupportNavigateUp() {
    return NavigationUI.navigateUp(navController, appBarConfig);
  }

  private void setupUI() {
    binding = ActivityLoginBinding.inflate(getLayoutInflater());
    EdgeToEdge.enable(this);
    ViewCompat.setOnApplyWindowInsetsListener(binding.navHostFragment, LoginActivity::adjustInsets);
    setContentView(binding.getRoot());
  }

  private void setupNavigation() {
    setSupportActionBar(binding.toolbar);
    appBarConfig = new AppBarConfiguration.Builder(R.id.pre_login_fragment, R.id.login_fragment)
        .build();
    NavHostFragment host = binding.navHostFragment.getFragment();
    navController = host.getNavController();
    NavigationUI.setupActionBarWithNavController(this, navController, appBarConfig);
  }

  private void setupViewModel() {
    LoginViewModel viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
    getLifecycle().addObserver(viewModel);
    viewModel
        .getAccount()
        .observe(this, this::handleAccount);
  }

  @NonNull
  private static WindowInsetsCompat adjustInsets(
      @NonNull View view, @NonNull WindowInsetsCompat windowInsets) {
    Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
    MarginLayoutParams mlp = (MarginLayoutParams) view.getLayoutParams();
    mlp.leftMargin = insets.left;
    mlp.bottomMargin = insets.bottom;
    mlp.rightMargin = insets.right;
    view.setLayoutParams(mlp);
    return WindowInsetsCompat.CONSUMED;
  }

  /** @noinspection deprecation*/
  private void handleAccount(GoogleSignInAccount account) {
    if (account != null) {
      Extras extras = new Extras.Builder()
          .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK)
          .build();
      navController.navigate(LoginNavGraphDirections.showMain(), extras);
    }
  }

}