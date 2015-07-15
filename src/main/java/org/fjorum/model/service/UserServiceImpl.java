package org.fjorum.model.service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.fjorum.controller.form.UserCreateForm;
import org.fjorum.controller.form.UserRightsForm;
import org.fjorum.model.entity.Role;
import org.fjorum.model.entity.User;
import org.fjorum.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends AbstractEntityServiceImpl<User> implements UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleService roleService) {
        this.userRepository = userRepository;
        this.roleService = roleService;
    }

    @Override
    public Optional<User> getUserByNameOrEmail(String nameOrEmail) {
        Optional<User> user = nameOrEmail.contains("@")
                ? userRepository.findOneByEmail(nameOrEmail)
                : userRepository.findOneByName(nameOrEmail);
        return user;
    }

    @Override
    public User create(UserCreateForm form) {
        String passwordHash = new BCryptPasswordEncoder().encode(form.getPassword());
        User user = new User(form.getName(), form.getEmail(), passwordHash, null);
        return userRepository.save(user);
    }

    @Override
    protected JpaRepository<User, Long> getRepository() {
        return userRepository;
    }

    @Override
    public void changeRights(UserRightsForm form) {
        Set<Role> roles = roleService.getAllRoles().stream()
                .filter(role -> form.getRoleId().contains(role.getId()))
                .collect(Collectors.toSet());
        User user = getById(form.getUserId())
                .orElseThrow(() -> new DataIntegrityViolationException("User not found"));
        user.setRoles(roles);
        userRepository.save(user);
    }

}
