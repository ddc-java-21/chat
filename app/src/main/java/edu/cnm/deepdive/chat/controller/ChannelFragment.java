package edu.cnm.deepdive.chat.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import dagger.hilt.android.AndroidEntryPoint;
import edu.cnm.deepdive.chat.databinding.FragmentChannelBinding;
import edu.cnm.deepdive.chat.model.dto.Channel;

@AndroidEntryPoint
public class ChannelFragment extends Fragment {

  private FragmentChannelBinding binding;
  private Channel channel;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    channel = ChannelFragmentArgs.fromBundle(getArguments()).getChannel();
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    binding = FragmentChannelBinding.inflate(inflater, container, false);
    //noinspection DataFlowIssue
    ((AppCompatActivity)requireActivity())
        .getSupportActionBar()
        .setTitle(channel.getTitle());
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    // TODO: 7/1/25 Attach to viewmodel(s) and observe livedata.
  }

  @Override
  public void onDestroyView() {
    binding = null;
    super.onDestroyView();
  }
}
