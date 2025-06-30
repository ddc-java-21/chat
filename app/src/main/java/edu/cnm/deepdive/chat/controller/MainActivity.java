package edu.cnm.deepdive.chat.controller;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.navigation.NavigationView;
import dagger.hilt.android.AndroidEntryPoint;
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
    binding = ActivityMainBinding.inflate(getLayoutInflater());
    loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
    binding.navDrawer
        .getMenu()
        .findItem(R.id.sign_out)
        .setOnMenuItemClickListener((item) -> {
          loginViewModel.signOut();
          return true;
        });
    // TODO: 6/30/25 Attach listeners to UI widgets.
    setContentView(binding.getRoot());
    setupNavigation();
  }

  @Override
  public boolean onSupportNavigateUp() {
    return NavigationUI.navigateUp(navController, appBarConfiguration)
        || super.onSupportNavigateUp();
  }

  private void setupNavigation() {
    setSupportActionBar(binding.appBarMain.toolbar);
    NavigationView navigationView = binding.navDrawer;
    appBarConfiguration = new AppBarConfiguration.Builder(R.id.main_fragment)
        .setOpenableLayout(binding.getRoot())
        .build();
    NavHostFragment host = binding.appBarMain.navHostFragment.getFragment();
    navController = host.getNavController();
    NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
    NavigationUI.setupWithNavController(navigationView, navController);
  }



}
