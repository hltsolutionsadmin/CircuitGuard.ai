package com.circuitguard.ai.usermanagement.populator;

import com.circuitguard.ai.usermanagement.dto.UserAssignmentDTO;
import com.circuitguard.ai.usermanagement.model.UserAssignmentModel;
import com.circuitguard.utils.Populator;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserAssignmentPopulator implements Populator<UserAssignmentModel, UserAssignmentDTO> {

    @Override
    public void populate(UserAssignmentModel source, UserAssignmentDTO target) {
        if (source == null || target == null) {
            return;
        }

        // Basic fields
        target.setId(source.getId());
        target.setTargetType(source.getTargetType());
        target.setTargetId(source.getTargetId());
        target.setRole(source.getRole());
        target.setActive(source.getActive());

        // User info
        if (source.getUser() != null) {
                target.setUserIds(List.of(source.getUser().getId()));

            target.setUsername(source.getUser().getUsername());
            target.setFullName(source.getUser().getFullName());
            target.setPrimaryContact(source.getUser().getPrimaryContact());
            target.setEmail(source.getUser().getEmail());
        }

        if (source.getGroups() != null && !source.getGroups().isEmpty()) {
            Set<Long> groupIds = source.getGroups()
                    .stream()
                    .filter(g -> g != null && g.getId() != null)
                    .map(g -> g.getId())
                    .collect(Collectors.toSet());
            target.setGroupIds(groupIds);
        } else {
            target.setGroupIds(null);
        }

    }
}
