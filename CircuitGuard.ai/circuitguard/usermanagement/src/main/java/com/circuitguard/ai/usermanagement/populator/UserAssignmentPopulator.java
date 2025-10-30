package com.circuitguard.ai.usermanagement.populator;

import com.circuitguard.ai.usermanagement.dto.UserAssignmentDTO;
import com.circuitguard.ai.usermanagement.model.UserAssignmentModel;
import com.circuitguard.ai.usermanagement.model.UserGroupModel;
import com.circuitguard.utils.Populator;
import org.springframework.stereotype.Component;

import java.util.Collections;
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

        target.setId(source.getId());
        target.setTargetType(source.getTargetType());
        target.setTargetId(source.getTargetId());
        target.setActive(source.getActive());

        if (source.getRoles() != null && !source.getRoles().isEmpty()) {
            target.setRoles(source.getRoles().stream().toList());
        }

        if (source.getUser() != null) {
            target.setUserIds(List.of(source.getUser().getId()));
            target.setUsername(source.getUser().getUsername());
            target.setFullName(source.getUser().getFullName());
            target.setPrimaryContact(source.getUser().getPrimaryContact());
            target.setEmail(source.getUser().getEmail());
        }

        if (source.getGroups() != null && !source.getGroups().isEmpty()) {
            target.setGroupIds(
                    source.getGroups().stream()
                            .filter(g -> g != null && g.getId() != null)
                            .map(UserGroupModel::getId)
                            .toList()
            );
        }
    }
}
