package com.circuitguard.ai.usermanagement.populator;

import com.circuitguard.ai.usermanagement.dto.UserGroupDTO;
import com.circuitguard.ai.usermanagement.model.UserGroupModel;
import com.circuitguard.utils.Populator;
import org.springframework.stereotype.Component;

@Component
public class UserGroupPopulator implements Populator<UserGroupModel, UserGroupDTO> {

    @Override
    public void populate(UserGroupModel source, UserGroupDTO target) {
        if (source == null || target == null) return;

        target.setId(source.getId());
        target.setGroupName(source.getGroupName());
        target.setDescription(source.getDescription());
    }
}
