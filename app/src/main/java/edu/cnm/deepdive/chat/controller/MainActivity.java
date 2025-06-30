package edu.cnm.deepdive.chat.controller;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.ActivityNavigator.Extras;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.navigation.NavigationView;
import dagger.hilt.android.AndroidEntryPoint;
import edu.cnm.deepdive.chat.MainNavGraphDirections;
import edu.cnm.deepdive.chat.R;
import edu.cnm.deepdive.chat.databinding.ActivityMainBinding;
import edu.cnm.deepdive.chat.viewmodel.LoginViewModel;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

  private ActivityMainBinding binding;
  private NavController navController;
  private AppBarConfiguration appBarConfiguration;
  private LoginViewModel loginViewModel;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setupUI();
    setupNavigation();
    setupViewModel();
  }

  @Override
  public boolean onSupportNavigateUp() {
    return NavigationUI.navigateUp(navController, appBarConfiguration)
        || super.onSupportNavigateUp();
  }

  private void setupUI() {
    binding = ActivityMainBinding.inflate(getLayoutInflater());
    binding.navDrawer
        .getMenu()
        .findItem(R.id.sign_out)
        .setOnMenuItemClickListener((item) -> {
          loginViewModel.signOut();
          return true;
        });
    // TODO: 6/30/2025 Attach listeners to UI widgets.
    setContentView(binding.getRoot());
  }

  private void setupNavigation() {
    setSupportActionBar(binding.appBarMain.toolbar);
    NavigationView navigationView = binding.navDrawer;
    appBarConfiguration = new AppBarConfiguration.Builder(R.id.main_fragment)
        .setOpenableLayout(binding.getRoot())
        .build();
    navController = ((NavHostFragment) (binding.appBarMain.navHostFragment.getFragment()))
        .getNavController();
    NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
    NavigationUI.setupWithNavController(navigationView, navController);
  }

  private void setupViewModel() {
    loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
    loginViewModel
        .getAccount()
        .observe(this, this::handleAccount);
  }

  /** @noinspection deprecation*/
  private void handleAccount(GoogleSignInAccount account) {
    if(account ==null){
      Extras extras = new Extras.Builder()
          .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
          .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
          .build();
      navController.navigate(MainNavGraphDirections.showLogin());
    }
  }
}
