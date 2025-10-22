package com.circuitguard.auth;

import com.circuitguard.commonservice.dto.UserDTO;

public interface UserServiceAdapter {
    UserDTO getUserById(Long userId);
}
