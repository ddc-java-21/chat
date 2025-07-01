package edu.cnm.deepdive.chat.model.dto;

import com.google.gson.annotations.Expose;
import java.time.Instant;
import java.util.UUID;

public class Message {

  @Expose(serialize = false)
  private final UUID key = null;

  @Expose(serialize = false)
  private final User author = null;

  @Expose(serialize = false)
  private final Instant posted = null;

  @Expose
  private String text;

  public UUID getKey() {
    return key;
  }

  public User getAuthor() {
    return author;
  }

  public Instant getPosted() {
    return posted;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

}
