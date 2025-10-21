package com.circuitguard.ai.usermanagement.services.impl;


import com.skillrat.commonservice.enums.ERole;
import com.circuitguard.ai.usermanagement.model.RoleModel;
import com.circuitguard.ai.usermanagement.repository.RoleRepository;
import com.circuitguard.ai.usermanagement.services.RoleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public RoleModel findByErole(ERole eRole) {
        Optional<RoleModel> role = roleRepository.findByName(eRole);
        return role.orElse(null);
    }
}
