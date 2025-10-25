package com.circuitguard.ai.usermanagement.populator;

import com.circuitguard.ai.usermanagement.dto.UserAssignmentDTO;
import com.circuitguard.ai.usermanagement.model.UserAssignmentModel;
import com.circuitguard.utils.Populator;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UserAssignmentPopulator implements Populator<UserAssignmentModel, UserAssignmentDTO> {

    @Override
    public void populate(UserAssignmentModel source, UserAssignmentDTO target) {
        if (source == null || target == null) {
            return;
        }

        target.setId(source.getId());
        target.setUserId(source.getUser() != null ? source.getUser().getId() : null);
        target.setTargetType(source.getTargetType());
        target.setTargetId(source.getTargetId());
        target.setRole(source.getRole()); // now AssignmentRole enum
        target.setActive(source.getActive());

        if (source.getGroups() != null && !source.getGroups().isEmpty()) {
            target.setGroupIds(
                    source.getGroups()
                            .stream()
                            .map(g -> g.getId())
                            .collect(Collectors.toSet())
            );
        }
        if (source.getUser() != null) {
            target.setUsername(source.getUser().getUsername());
            target.setFullName(source.getUser().getFullName());
            target.setPrimaryContact(source.getUser().getPrimaryContact());
            target.setEmail(source.getUser().getEmail());
        }
    }
}
