package org.fjorum.model.service;

import org.fjorum.controller.form.UserCreateForm;
import org.fjorum.model.entity.User;
import org.fjorum.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> getUserById(long id) {
        return Optional.ofNullable(userRepository.findOne(id));
    }

    @Override
    public Optional<User> getUserByNameOrEmail(String nameOrEmail) {
        Optional<User> user = nameOrEmail.contains("@")
                ? userRepository.findOneByEmail(nameOrEmail)
                : userRepository.findOneByName(nameOrEmail);
        return user;
    }

    @Override
    public Collection<User> getAllUsers() {
        return userRepository.findAll(new Sort("name"));
    }

    @Override
    public User create(UserCreateForm form) {
        String passwordHash = new BCryptPasswordEncoder().encode(form.getPassword());
        User user = new User(form.getName(), form.getEmail(), passwordHash, null);
        return userRepository.save(user);
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

}
