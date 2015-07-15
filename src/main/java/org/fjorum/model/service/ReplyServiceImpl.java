package org.fjorum.model.service;

import org.fjorum.controller.form.ReplyCreateForm;
import org.fjorum.model.entity.Reply;
import org.fjorum.model.entity.Topic;
import org.fjorum.model.entity.User;
import org.fjorum.model.repository.ReplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.Objects;

@Service
public class ReplyServiceImpl extends AbstractEntityServiceImpl<Reply> implements ReplyService {

    private final ReplyRepository replyRepository;
    private final TopicService topicService;

    @Autowired
    public ReplyServiceImpl(ReplyRepository replyRepository,
                            TopicService topicService) {
        this.replyRepository = replyRepository;
        this.topicService = topicService;
    }


    @Override
    public Reply createNewReply(ReplyCreateForm form, User user) {
        return topicService.getById(form.getTopicId())
                .map(topic -> createNewReply(topic, user, form.getContent()))
                .orElseThrow(() -> new DataIntegrityViolationException("Topic not found"));
    }

    @Override
    public Reply createNewReply(Topic topic, User user, String content) {
        Reply reply = new Reply(topic, user, content);
        return replyRepository.save(reply);
    }

    @Override
    protected JpaRepository<Reply, Long> getRepository() {
        return replyRepository;
    }
}
