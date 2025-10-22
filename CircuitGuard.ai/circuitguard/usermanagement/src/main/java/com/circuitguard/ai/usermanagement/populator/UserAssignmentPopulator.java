package com.circuitguard.ai.usermanagement.populator;

import com.circuitguard.ai.usermanagement.dto.UserAssignmentDTO;
import com.circuitguard.ai.usermanagement.model.UserAssignmentModel;
import com.circuitguard.utils.Populator;
import org.springframework.stereotype.Component;

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
        target.setRole(source.getRole());
        target.setActive(source.getActive());
    }
}
