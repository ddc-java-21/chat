package edu.cnm.deepdive.chat.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import com.google.android.material.snackbar.Snackbar;
import dagger.hilt.android.AndroidEntryPoint;
import edu.cnm.deepdive.notes.R;
import edu.cnm.deepdive.notes.databinding.FragmentLoginBinding;
import edu.cnm.deepdive.notes.viewmodel.LogInViewModel;

@AndroidEntryPoint
public class LoginFragment extends Fragment {

  private FragmentLoginBinding binding;
  private LogInViewModel viewModel;
  private ActivityResultLauncher<Intent> launcher;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    binding = FragmentLoginBinding.inflate(inflater, container, false);
    binding.signIn.setOnClickListener((v) -> viewModel.startSignIn(launcher));
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    viewModel = new ViewModelProvider(requireActivity()).get(LogInViewModel.class);
    LifecycleOwner owner = getViewLifecycleOwner();
    viewModel
        .getAccount()
        .observe(owner, (account) -> {
          if (account != null) {
            Navigation.findNavController(binding.getRoot())
                .navigate(LoginFragmentDirections.showList());
          }
        });
    viewModel
        .getRefreshThrowable()
        .observe(owner, (throwable -> {
          if (throwable != null) {
            Snackbar.make(binding.getRoot(), getString(R.string.sign_in_failure_message), Snackbar.LENGTH_LONG)
                .show();
          }
        }));
    launcher = registerForActivityResult(new StartActivityForResult(), viewModel ::completeSignIn);
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
  }
}
