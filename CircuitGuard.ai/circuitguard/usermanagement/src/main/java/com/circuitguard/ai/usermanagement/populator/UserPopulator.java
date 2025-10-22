package com.circuitguard.ai.usermanagement.populator;

import com.circuitguard.ai.usermanagement.dto.AddressDTO;
import com.circuitguard.ai.usermanagement.dto.UserDTO;
import com.circuitguard.ai.usermanagement.model.UserModel;
import com.circuitguard.utils.Populator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UserPopulator implements Populator<UserModel, UserDTO> {

    @Autowired
    private AddressPopulator addressPopulator;

    @Override
    public void populate(UserModel source, UserDTO target) {
        populate(source, target, true);
    }

    public void populate(UserModel source, UserDTO target, boolean includeAddresses) {
        if (source == null || target == null) {
            return;
        }

        target.setId(source.getId());
        target.setFullName(source.getFullName());
        target.setUsername(source.getUsername());
        target.setEmail(source.getEmail());
        target.setPrimaryContact(source.getPrimaryContact());
        target.setGender(source.getGender());

        target.setFcmToken(source.getFcmToken());
        target.setRecentActivityDate(source.getRecentActivityDate());



        if (includeAddresses && source.getAddresses() != null && !source.getAddresses().isEmpty()) {
            target.setAddresses(
                    source.getAddresses().stream().map(address -> {
                        AddressDTO dto = new AddressDTO();
                        addressPopulator.populate(address, dto);
                        return dto;
                    }).collect(Collectors.toList())
            );
        }

        if (source.getRoles() != null && !source.getRoles().isEmpty()) {
            target.setRoles(
                    source.getRoles().stream()
                            .map(role -> role.getName().name())
                            .collect(Collectors.toSet())
            );
        }
    }
    public UserDTO toDTO(UserModel source) {
        if (source == null) return null;
        UserDTO dto = new UserDTO();
        populate(source, dto);
        return dto;
    }
}
