package org.fjorum.services;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import org.fjorum.models.Category;
import org.fjorum.models.Topic;
import org.fjorum.models.User;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

@Singleton
public class TopicService {

    @Inject
    private Provider<EntityManager> entitiyManagerProvider;

    public Topic createNewTopic(Category category, User user, String name) {
        EntityManager entityManager = entitiyManagerProvider.get();
        Topic topic = new Topic(category, user, name);
        entityManager.persist(topic);
        return topic;
    }

    public List<Topic> findTopicByCategory(Category category) {
        EntityManager entityManager = entitiyManagerProvider.get();
        Query q = entityManager.createQuery(
                "SELECT t FROM Topic t WHERE t.category=:category ORDER BY t.sticky, t.created DESC");
        q.setParameter("category", category);
        List<Topic> topics = q.getResultList();
        return topics;
    }

    public Optional<Topic> findTopicById(Long id) {
        EntityManager entityManager = entitiyManagerProvider.get();
        Topic topic = entityManager.find(Topic.class, id);
        return Optional.ofNullable(topic);
    }
}
