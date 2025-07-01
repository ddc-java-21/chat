package edu.cnm.deepdive.chat.model.dto;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.gson.annotations.Expose;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class Channel implements Serializable {

  @Serial
  private static final long serialVersionUID = -431214265427305503L;
  private static final String TO_STRING_FORMAT = "%1$s{key=%2$s, title=%3$s, created=%4$s}";
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

  @Override
  public int hashCode() {
    //noinspection DataFlowIssue
    return key.hashCode();
  }

  @Override
  public boolean equals(@Nullable Object obj) {
    boolean comparison;
    if (this == obj) {
      comparison = true;
    } else if (obj instanceof Channel other) {
      //noinspection ConstantValue,DataFlowIssue
      comparison = this.key.equals(other.key);
    } else {
      comparison = false;
    }
    return comparison;
  }

  @NonNull
  @Override
  public String toString() {
    return String.format(TO_STRING_FORMAT, getClass().getSimpleName(), key, title, created);
  }
}
