package org.fjorum.services;

import ninja.session.Session;
import ninja.utils.NinjaProperties;
import org.fjorum.models.User;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static com.google.common.base.Strings.isNullOrEmpty;

@Singleton
public class UserService {

    private Logger logger = LoggerFactory.getLogger(UserService.class);

    private final int NUMBER_OF_BCRYPT_HASH_ROUNDS;

    @Inject
    private Provider<EntityManager> entitiyManagerProvider;

    @Inject
    public UserService(NinjaProperties ninjaProperties) {
        NUMBER_OF_BCRYPT_HASH_ROUNDS = ninjaProperties.getIntegerWithDefault(
                UserConstants.HASH_ROUNDS_KEY,
                UserConstants.HASH_ROUNDS_DEFAULT_VALUE);

    }

    public List<User> findAllUsers() {
        logger.info("findAllUsers");
        EntityManager entityManager = entitiyManagerProvider.get();
        Query q = entityManager.createQuery("SELECT u FROM User u ORDER BY u.name");
        @SuppressWarnings("unchecked") List<User> users =
                (List<User>) q.getResultList();
        return users;
    }

    public void removeUser(Long id) {
        logger.info("removeUser");
        EntityManager entityManager = entitiyManagerProvider.get();
        User user = entityManager.find(User.class, id);
        entityManager.remove(user);
    }


    public boolean createNewUser(
            String name,
            String email,
            String password,
            String confirmationCode) {
        logger.info("createNewUser");

        Optional<User> existingUser = findUserByEmail(email);

        if (existingUser.isPresent()) {
            return false;
        }

        String passwordHash = createPasswordHashFromPassword(password);

        User user = new User(name, email, passwordHash, confirmationCode);
        save(user);
        return true;

    }

    public void save(User user) {
        logger.info("save");
        EntityManager entityManager = entitiyManagerProvider.get();
        entityManager.persist(user);
    }


    public boolean isUserActivated(String email) {

        Optional<User> existingUser = findUserByEmail(email);

        // the email should be there and the user must have confirmed the name
        return existingUser.isPresent() &&
                isNullOrEmpty(existingUser.get().getConfirmationCode());
    }

    /*@Override
    public boolean hasRole(String email, String role) {

        Objectify objectify = objectifyProvider.get();
        User user = objectify.load().type(User.class).filter(User.EMAIL, email).first().now();

        if (user == null) {
            return false;
        }

        return user.hasRole(role);

    }

    @Override
    public void addRole(String email, String role) {

        Objectify objectify = objectifyProvider.get();
        User user = objectify.load().type(User.class).filter(User.EMAIL, email).first().now();

        if (user == null) {
            return;
        }

        user.addRole(role);

        objectify.save().entity(user).now();

    }

    @Override
    public void removeRole(String email, String role) {

        Objectify objectify = objectifyProvider.get();
        User user = objectify.load().type(User.class).filter(User.EMAIL, email).first().now();

        if (user == null) {
            return;
        }

        user.removeRole(role);
        objectify.save().entity(user).now();

    } */

    public boolean doesUserExist(String email) {

        return findUserByEmail(email).isPresent();
    }

    public Optional<User> findUserByEmail(String email) {
        return findUser(em -> {
            Query q = em.createQuery("SELECT u FROM User u WHERE u.email=:email");
            q.setParameter("email", email);
            return q;
        });
    }

    public Optional<User> findUserById(Long id) {
        EntityManager entityManager = entitiyManagerProvider.get();
        return Optional.ofNullable(entityManager.find(User.class, id));
    }

    public Optional<User> findUserByEmailOrName(String emailOrName) {
        return findUser(em -> {
            Query q = em.createQuery("SELECT u FROM User u WHERE u.email=:emailOrName OR u.name=:emailOrName");
            q.setParameter("emailOrName", emailOrName);
            return q;
        });
    }

    public Optional<User> findUserByConfirmationCode(String confirmationCode) {
        return findUser(em -> {
            Query q = em.createQuery("SELECT u FROM  User u WHERE u.confirmationCode=:confirmationCode");
            q.setParameter("confirmationCode", confirmationCode);
            return q;
        });
    }

    public Optional<User> findUserByRecoveryPasswordCode(String recoverPasswordCode) {
        return findUser(em -> {
            Query q = em.createQuery("SELECT u FROM  User u WHERE u.recoverPasswordCode=:recoverPasswordCode");
            q.setParameter("recoverPasswordCode", recoverPasswordCode);
            return q;
        });
    }

    private Optional<User> findUser(Function<EntityManager, Query> fn) {
        EntityManager entityManager = entitiyManagerProvider.get();
        Query q = fn.apply(entityManager);
        q.setMaxResults(1);
        @SuppressWarnings("unchecked") List<User> resultList = (List<User>) q.getResultList();
        return resultList.isEmpty()
                ? Optional.empty()
                : Optional.of(resultList.get(0));
    }


    public boolean isPasswordValid(User user, String passwordPlainText) {
        return user.isActive() && BCrypt.checkpw(passwordPlainText, user.getPasswordHash());
    }

    public void setNewPassword(User user, String password) {
        String passwordHash = createPasswordHashFromPassword(password);
        user.setPasswordHash(passwordHash);
    }

    private String createPasswordHashFromPassword(String passwordPlainText) {
        return BCrypt.hashpw(
                passwordPlainText,
                BCrypt.gensalt(NUMBER_OF_BCRYPT_HASH_ROUNDS));
    }

    public Optional<User> findUserBySession(Session session) {
        String idString = session.get(UserMessages.USER_ID);
        if (!isNullOrEmpty(idString)) try {
            return findUserById(Long.valueOf(idString));
        } catch (NumberFormatException ex) {
            /* ignore */
        }
        return Optional.<User>empty();
    }
}
