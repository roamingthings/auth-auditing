package de.roamingthings.authaudit.authauditing.service;

import de.roamingthings.authaudit.authauditing.domain.Role;
import de.roamingthings.authaudit.authauditing.repository.RoleRepository;
import org.springframework.stereotype.Service;

/**
 * @author Alexander Sparkowsky [info@roamingthings.de]
 * @version 2017/05/07
 */
@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public void addRoleIfNotExists(String role) {
        if (!roleRepository.existsByRole(role)) {
            Role roleToCreate = new Role(role);
            roleRepository.save(roleToCreate);
        }
    }

    @Override
    public Role findByRole(String role) {
        return roleRepository.findByRole(role);
    }
}
