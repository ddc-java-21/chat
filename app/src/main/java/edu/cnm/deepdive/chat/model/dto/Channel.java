package edu.cnm.deepdive.chat.model.dto;

import com.google.gson.annotations.Expose;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

public class Channel implements Serializable {

  @Serial
  private static final long serialVersionUID = -7654672701579076188L;

  @Expose(serialize = false)
  private final UUID key = null;

  @Expose(serialize = false)
  private final String title = null;

  @Expose(serialize = false)
  private final Instant created = null;

  public UUID getKey() {
    return key;
  }

  public String getTitle() {
    return title;
  }

  public Instant getCreated() {
    return created;
  }
}
