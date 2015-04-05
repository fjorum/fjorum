package org.fjorum.models;

import org.fjorum.util.LocalDateTimeConverter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reply")
public class Reply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @ManyToOne
    @JoinColumn(name="topic_id")
    private Topic topic;
    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;
    @Column(name = "created")
    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime created;
    @Column(name = "modified")
    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime modified;
    @Column(name = "reply_content")
    private String content;

    public Reply(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public LocalDateTime getModified() {
        return modified;
    }

    public void setModified(LocalDateTime modified) {
        this.modified = modified;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
