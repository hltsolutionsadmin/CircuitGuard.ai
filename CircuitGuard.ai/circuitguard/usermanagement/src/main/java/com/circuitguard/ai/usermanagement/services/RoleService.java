package com.circuitguard.ai.usermanagement.services;


import com.circuitguard.commonservice.enums.ERole;
import com.circuitguard.ai.usermanagement.model.RoleModel;

public interface RoleService {

    RoleModel findByErole(ERole eRole);
}
