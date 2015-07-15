package org.fjorum.model.service;

import org.fjorum.controller.form.ReplyCreateForm;
import org.fjorum.controller.form.TopicCreateForm;
import org.fjorum.model.entity.Category;
import org.fjorum.model.entity.Reply;
import org.fjorum.model.entity.Topic;
import org.fjorum.model.entity.User;

import java.util.Optional;

public interface ReplyService extends EntityService<Reply> {

    Reply createNewReply(ReplyCreateForm form, User user);

    Reply createNewReply(Topic topic, User user, String content);

}
