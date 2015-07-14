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

@Service
public class ReplyServiceImpl extends AbstractEntityServiceImpl<Reply> implements ReplyService {

    private final ReplyRepository replyRepository;
    private final TopicService topicService;
    private final UserService userService;

    @Autowired
    public ReplyServiceImpl(ReplyRepository replyRepository,
                            TopicService topicService,
                            UserService userService) {
        this.replyRepository = replyRepository;
        this.topicService = topicService;
        this.userService = userService;
    }


    @Override
    public Reply createNewReply(ReplyCreateForm form) {
        return topicService.getById(form.getTopicId()).
                flatMap(category -> userService.getById(form.getUserId()).
                        map(user -> createNewReply(category, user, form.getContent()))).
                orElseThrow(() -> new DataIntegrityViolationException("Topic or user not found"));
    }

    @Override
    public Reply createNewReply(Topic topic, User user, String name) {
        Reply reply = new Reply(topic, user, name);
        return replyRepository.save(reply);
    }

    @Override
    protected JpaRepository<Reply, Long> getRepository() {
        return replyRepository;
    }
}
