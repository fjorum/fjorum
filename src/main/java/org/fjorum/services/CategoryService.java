package org.fjorum.services;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import org.fjorum.models.Category;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

@Singleton
public class CategoryService {
    @Inject
    private Provider<EntityManager> entitiyManagerProvider;

    public Category createNewCategory(String name) {
        EntityManager entityManager = entitiyManagerProvider.get();
        Query q = entityManager.createQuery("SELECT max(c.sortOrder) FROM Category c");
        Integer maxSortOrder = (Integer) q.getSingleResult();
        Category category = new Category(name);
        category.setSortOrder(maxSortOrder == null ? 0 : maxSortOrder + 1);
        entityManager.persist(category);
        return category;
    }

    public void removeCategory(Category category) {
        EntityManager entityManager = entitiyManagerProvider.get();
        entityManager.remove(category);
    }

    public void up(Category category) {
        EntityManager entityManager = entitiyManagerProvider.get();
        Query q = entityManager.createQuery(
                "SELECT c FROM Category c WHERE c.sortOrder<:sortOrder ORDER BY c.sortOrder DESC");
        q.setParameter("sortOrder", category.getSortOrder());
        q.setMaxResults(1);
        List<Category> smallerCategories = q.getResultList();
        if (smallerCategories.isEmpty()) {
            return;
        }
        Category smaller = smallerCategories.get(0);
        int temp = smaller.getSortOrder();
        smaller.setSortOrder(category.getSortOrder());
        category.setSortOrder(temp);
        entityManager.persist(category);
        entityManager.persist(smaller);
    }

    public void down(Category category) {
        EntityManager entityManager = entitiyManagerProvider.get();
        Query q = entityManager.createQuery(
                "SELECT c FROM Category c WHERE c.sortOrder>:sortOrder ORDER BY c.sortOrder");
        q.setParameter("sortOrder", category.getSortOrder());
        q.setMaxResults(1);
        List<Category> biggerCategories = q.getResultList();
        if (biggerCategories.isEmpty()) {
            return;
        }
        Category bigger = biggerCategories.get(0);
        int temp = bigger.getSortOrder();
        bigger.setSortOrder(category.getSortOrder());
        category.setSortOrder(temp);
        entityManager.persist(category);
        entityManager.persist(bigger);
    }

    public List<Category> findAllCategories(){
        EntityManager entityManager = entitiyManagerProvider.get();
        Query q = entityManager.createQuery(
                "SELECT c FROM Category c ORDER BY c.sortOrder");
        return q.getResultList();
    }

    public Optional<Category> findCategory(Long id) {
        EntityManager entityManager = entitiyManagerProvider.get();
        Category category = entityManager.find(Category.class, id);
        return Optional.ofNullable(category);
    }
}
