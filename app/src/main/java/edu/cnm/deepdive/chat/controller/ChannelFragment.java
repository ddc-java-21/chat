package edu.cnm.deepdive.chat.controller;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import dagger.hilt.android.AndroidEntryPoint;
import edu.cnm.deepdive.chat.databinding.FragmentChannelBinding;
import edu.cnm.deepdive.chat.model.dto.Channel;
import edu.cnm.deepdive.chat.model.dto.Message;
import edu.cnm.deepdive.chat.view.adapter.ChannelMessagesAdapter;
import edu.cnm.deepdive.chat.viewmodel.ChatViewModel;
import javax.inject.Inject;

@AndroidEntryPoint
public class ChannelFragment extends Fragment {

  private static final String TAG = ChannelFragment.class.getSimpleName();

  private FragmentChannelBinding binding;
  private Channel channel;
  private ChatViewModel viewModel;

  @Inject
  ChannelMessagesAdapter adapter;

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
    ((AppCompatActivity) requireActivity())
        .getSupportActionBar()
        .setTitle(channel.getTitle());
    binding.send.setOnClickListener((v) -> {
      Message message = new Message();
      message.setText(binding.message.getText().toString().strip());
      viewModel.sendMessage(message);
      binding.message.setText("");
    });
    binding.messages.setAdapter(adapter);
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    viewModel = new ViewModelProvider(requireActivity()).get(ChatViewModel.class);
    viewModel.setSelectedChannel(channel);
    viewModel
        .getMessages()
        .observe(getViewLifecycleOwner(), (messages) -> {
          adapter.setMessages(messages);
          binding.messages.scrollToPosition(messages.size() - 1);
        });
  }

  @Override
  public void onDestroyView() {
    binding = null;
    super.onDestroyView();
  }

}
