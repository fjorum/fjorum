package org.fjorum.controller.form;

import org.fjorum.model.entity.Topic;
import org.fjorum.model.entity.User;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

public class ReplyCreateForm {

    public final static String NAME = "replyCreateForm";

    @NotNull
    private User user;

    @NotNull
    private Topic topic;

    @NotEmpty
    private String content = "";

    ReplyCreateForm(){}

    public ReplyCreateForm(User user, Topic topic) {
        this.user = user;
        this.topic = topic;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
