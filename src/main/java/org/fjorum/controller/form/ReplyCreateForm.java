package org.fjorum.controller.form;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

public class ReplyCreateForm {

    public final static String NAME = "replyCreateForm";

    @NotNull
    private Long topicId = 0L;

    @NotNull
    private Long userId = 0L;

    @NotEmpty
    private String content = "";

    public Long getTopicId() {
        return topicId;
    }

    public void setTopicId(Long topicId) {
        this.topicId = topicId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
