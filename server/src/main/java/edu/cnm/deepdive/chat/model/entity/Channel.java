package edu.cnm.deepdive.chat.model.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import org.hibernate.annotations.CreationTimestamp;

@SuppressWarnings("JpaDataSourceORMInspection")
@Entity
public class Channel {

  @Id
  @GeneratedValue
  @Column(name = "channel_id", nullable = false, updatable = false)
  private long id;

  @Column(nullable = false, updatable = false, unique = true)
  private UUID externalKey;

  @Column(nullable = false, updatable = true, unique = true, length = 30)
  private String title;

  @CreationTimestamp
  @Temporal(TemporalType.TIMESTAMP)
  @Column(nullable = false, updatable = false)
  private Instant created;

  @OneToMany(mappedBy = "channel", fetch = FetchType.LAZY,
      cascade = CascadeType.ALL, orphanRemoval = true)
  private final List<Message> messages = new LinkedList<>();

  public long getId() {
    return id;
  }

  public UUID getExternalKey() {
    return externalKey;
  }

  public String getTitle() {
    return title;
  }

  public Channel setTitle(String title) {
    this.title = title;
    return this;
  }

  public Instant getCreated() {
    return created;
  }

  public List<Message> getMessages() {
    return messages;
  }


  @Override
  public int hashCode() {
    return Long.hashCode(id);
  }

  @Override
  public boolean equals(Object obj) {
    boolean comparison;
    if (this == obj) {
      comparison = true;
    } else if (obj instanceof Channel other) {
      comparison = (this.id != 0 && this.id == other.id);
    } else {
      comparison = false;
    }
    return comparison;
  }


  @PrePersist
  void generateFieldValues() {
    externalKey = UUID.randomUUID();
  }

}
