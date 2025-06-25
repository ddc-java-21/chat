package edu.cnm.deepdive.chat.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.CreationTimestamp;

@SuppressWarnings("JpaDataSourceORMInspection")
@Entity
public class Message {

  @Id
  @GeneratedValue
  @Column(name = "message_id", nullable = false, updatable = false)
  private long id;

  @Column(nullable = false, updatable = false, unique = true)
  private UUID externalKey;

  @Column(nullable = false, updatable = false) // TODO: 6/24/25 Investigate appropriate length.
  private String text;

  @CreationTimestamp
  @Temporal(TemporalType.TIMESTAMP)
  @Column(nullable = false, updatable = false)
  private Instant posted;

  @ManyToOne(fetch = FetchType.EAGER, optional = false)
  @JoinColumn(name = "author_id", nullable = false, updatable = false)
  private User author;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "channel_id", nullable = false, updatable = false)
  private Channel channel;

  public long getId() {
    return id;
  }

  public UUID getExternalKey() {
    return externalKey;
  }

  public String getText() {
    return text;
  }

  public Message setText(String text) {
    this.text = text;
    return this;
  }

  public Instant getPosted() {
    return posted;
  }

  public User getAuthor() {
    return author;
  }

  public Message setAuthor(User author) {
    this.author = author;
    return this;
  }

  public Channel getChannel() {
    return channel;
  }

  public Message setChannel(Channel channel) {
    this.channel = channel;
    return this;
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
    } else if (obj instanceof Message other) {
      comparison = (this.id != 0 && this.id == other.id);
    } else {
      comparison = false;
    }
    return comparison;
  }

  // TODO: 6/24/25 Implement hashCode and equals.

  @PrePersist
  void generateFieldValues() {
    externalKey = UUID.randomUUID();
  }

}
