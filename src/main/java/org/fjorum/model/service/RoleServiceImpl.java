package org.fjorum.model.service;

import java.util.List;
import java.util.Optional;

import org.fjorum.model.entity.Role;
import org.fjorum.model.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }


    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public void addRole(Role role) {
        roleRepository.save(role);
    }

    @Override
    public Optional<Role> findRole(String name) {
        return roleRepository.findOneByName(name);
    }
}
