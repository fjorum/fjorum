package org.fjorum.services;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.fjorum.models.Reply;
import org.fjorum.models.Topic;
import org.fjorum.models.User;

/**
 * @author Daniel Gronau <daniel.gronau@skillcert.de>
 */
@Singleton
public class ReplyService {
    @Inject
    private Provider<EntityManager> entitiyManagerProvider;

    public Reply createNewReply(Topic topic, User user, String content) {
        EntityManager entityManager = entitiyManagerProvider.get();
        Reply reply = new Reply(topic, user, content);
        entityManager.persist(reply);
        return reply;
    }

    public List<Reply> findRepliesByTopic(Topic topic) {
        EntityManager entityManager = entitiyManagerProvider.get();
        Query q = entityManager.createQuery(
                "SELECT r FROM Reply r WHERE r.topic=:topic ORDER BY r.created");
        q.setParameter("topic", topic);
        List<Reply> replies = q.getResultList();
        return replies;
    }

    public Optional<Reply> findReplyById(Long id) {
        EntityManager entityManager = entitiyManagerProvider.get();
        Reply reply = entityManager.find(Reply.class, id);
        return Optional.ofNullable(reply);
    }
}
