
package com.circuitguard.ai.usermanagement.services;


import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.circuitguard.commonservice.dto.BasicOnboardUserDTO;
import com.circuitguard.commonservice.dto.UserDTO;
import com.circuitguard.commonservice.enums.ERole;
import com.circuitguard.ai.usermanagement.dto.UserUpdateDTO;
import com.circuitguard.ai.usermanagement.model.UserModel;

import jakarta.validation.constraints.NotBlank;

/**
 * @author juvi
 */
public interface UserService {

    UserModel saveUser(UserModel userModel);

    Long onBoardUserWithCredentials(BasicOnboardUserDTO dto);

    void updateUser(final UserUpdateDTO details, final Long userId);

    Long onBoardUser(final String fullName, final String mobileNumber, final Set<ERole> userRoles);

    void addUserRole(final Long userId, final ERole userRole);

    void removeUserRole(final String mobileNumber, final ERole userRole);

    UserModel findById(Long id);

    UserDTO getUserById(Long userId);

    List<UserModel> findByIds(List<Long> ids);

    UserModel findByEmail(String email);

    Optional<UserModel> findByPrimaryContact(String primaryContact);

    Boolean existsByEmail(final String email, final Long userId);

    List<UserDTO> getUsersByRole(String roleName);

    void clearFcmToken(Long userId);


    Optional<UserModel> findByUsername(@NotBlank String username);
}
