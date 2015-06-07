package org.fjorum.model.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.fjorum.controller.form.UserCreateForm;
import org.fjorum.controller.form.UserRightsForm;
import org.fjorum.model.entity.Role;
import org.fjorum.model.entity.User;
import org.fjorum.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleService roleService) {
        this.userRepository = userRepository;
        this.roleService = roleService;
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
    public List<User> getAllUsers() {
        return userRepository.findAll(new Sort("name"));
    }

    @Override
    public User create(UserCreateForm form) {
        String passwordHash = new BCryptPasswordEncoder().encode(form.getPassword());
        User user = new User(form.getName(), form.getEmail(), passwordHash, null);
        return userRepository.save(user);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public void delete(User user) {
        userRepository.delete(user);
    }

    @Override
    public void changeRights(UserRightsForm form) {
        Set<Role> roles = roleService.getAllRoles().stream().
                filter(role -> form.getRoleId().contains(role.getId())).
                collect(Collectors.toSet());
        User user = getUserById(form.getUserId()).orElseThrow(RuntimeException::new);
        user.setRoles(roles);
        userRepository.save(user);
    }

}
