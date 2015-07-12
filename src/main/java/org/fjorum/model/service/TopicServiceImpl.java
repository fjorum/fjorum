package org.fjorum.model.service;

import org.fjorum.controller.form.TopicCreateForm;
import org.fjorum.model.entity.Category;
import org.fjorum.model.entity.Topic;
import org.fjorum.model.entity.User;
import org.fjorum.model.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TopicServiceImpl implements TopicService {

    private final TopicRepository topicRepository;
    private final CategoryService categoryService;
    private final UserService userService;

    @Autowired
    public TopicServiceImpl(TopicRepository topicRepository,
                            CategoryService categoryService,
                            UserService userService) {
        this.topicRepository = topicRepository;
        this.categoryService = categoryService;
        this.userService = userService;
    }


    @Override
    public Topic createNewTopic(TopicCreateForm form) {
        return categoryService.findCategoryById(form.getCategoryId()).
                flatMap(category -> userService.getUserById(form.getUserId()).
                        map(user -> createNewTopic(category, user, form.getName()))).
                orElseThrow(() -> new DataIntegrityViolationException("Category or user not found"));
    }

    @Override
    public Topic createNewTopic(Category category, User user, String name) {
        Topic topic = new Topic(category, user, name);
        return topicRepository.save(topic);
    }

    @Override
    public Optional<Topic> findTopicById(Long id) {
        return Optional.ofNullable(topicRepository.findOne(id));
    }
}
