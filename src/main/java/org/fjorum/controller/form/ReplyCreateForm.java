package org.fjorum.controller.form;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

public class ReplyCreateForm {

    @NotNull
    private Long topicId = 0L;

    @NotEmpty
    private String content = "";

    public Long getTopicId() {
        return topicId;
    }

    public void setTopicId(Long topicId) {
        this.topicId = topicId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
