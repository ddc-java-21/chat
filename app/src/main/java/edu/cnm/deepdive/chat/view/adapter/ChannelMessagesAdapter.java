package edu.cnm.deepdive.chat.view.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Picasso.LoadedFrom;
import com.squareup.picasso.Target;
import dagger.hilt.android.qualifiers.ActivityContext;
import dagger.hilt.android.scopes.FragmentScoped;
import edu.cnm.deepdive.chat.R;
import edu.cnm.deepdive.chat.databinding.ItemChannelMessageBinding;
import edu.cnm.deepdive.chat.model.dto.Message;
import jakarta.inject.Inject;
import java.net.URL;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;

@FragmentScoped
public class ChannelMessagesAdapter extends Adapter<ViewHolder> {

  private final LayoutInflater inflater;
  private final DateTimeFormatter formatter;
  private final Picasso picasso;
  private final List<Message> messages;

  @Inject
  public ChannelMessagesAdapter(@ActivityContext Context context, Picasso picasso) {
    inflater = LayoutInflater.from(context);
    formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);
    this.picasso = picasso;
    messages = new ArrayList<>();
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup container, int itemType) {
    return new MessageViewHolder(ItemChannelMessageBinding.inflate(inflater, container, false));
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
    ((MessageViewHolder) viewHolder).bind(position);
  }

  @Override
  public int getItemCount() {
    return messages.size();
  }
  
  public void setMessages(List<Message> messages) {
    int newSize = messages.size();
    int previousSize = this.messages.size();
    this.messages.addAll(messages.subList(previousSize, newSize));
    notifyItemRangeInserted(previousSize, newSize);
  }

  private class MessageViewHolder extends ViewHolder implements Target {

    private final ItemChannelMessageBinding binding;

    MessageViewHolder(ItemChannelMessageBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }

    void bind(int position) {
      Message message = messages.get(position);
      binding.author.setText(message.getAuthor().getDisplayName());
      binding.posted.setText(
          ZonedDateTime.ofInstant(message.getPosted(), ZoneId.systemDefault()).format(formatter));
      binding.text.setText(message.getText());
      URL avatar = message.getAuthor().getAvatar();
      if (avatar != null) {
        picasso
            .load(avatar.toString())
            .into(this);
      } else {
        binding.avatar.setImageResource(R.drawable.portrait);
      }
    }
    
    @Override
    public void onBitmapLoaded(Bitmap bitmap, LoadedFrom loadedFrom) {
      binding.avatar.setImageBitmap(bitmap);
    }

    @Override
    public void onBitmapFailed(Exception e, Drawable drawable) {
      binding.avatar.setImageResource(R.drawable.portrait);
    }

    @Override
    public void onPrepareLoad(Drawable drawable) {
      //Do nothing
    }
  }

}
