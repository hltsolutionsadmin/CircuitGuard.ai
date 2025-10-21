package com.skillrat.auth;

import com.skillrat.commonservice.dto.UserDTO;

public interface UserServiceAdapter {
    UserDTO getUserById(Long userId);
}
