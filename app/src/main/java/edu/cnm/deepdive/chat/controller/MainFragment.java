package edu.cnm.deepdive.chat.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import dagger.hilt.android.AndroidEntryPoint;
import edu.cnm.deepdive.chat.databinding.FragmentMainBinding;
import edu.cnm.deepdive.chat.viewmodel.LoginViewModel;

@AndroidEntryPoint
public class MainFragment extends Fragment {

  private FragmentMainBinding binding;
  private LoginViewModel viewModel;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    binding = FragmentMainBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    FragmentActivity activity = requireActivity();
    viewModel = new ViewModelProvider(activity).get(LoginViewModel.class);
    LifecycleOwner owner = getViewLifecycleOwner();
    viewModel
        .getAccount()
        .observe(owner, this::handleAccount);
  }

  @Override
  public void onDestroyView() {
    binding = null;
    super.onDestroyView();
  }


  /** @noinspection deprecation*/
  private void handleAccount(GoogleSignInAccount account) {
    if (account == null) {
      Navigation.findNavController(binding.getRoot())
          .navigate(MainFragmentDirections.showPreLogin());
    } else {
      binding.bearerToken.setText(account.getIdToken());
    }
  }

}
