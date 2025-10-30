package com.circuitguard.ai.usermanagement.populator;

import com.circuitguard.ai.usermanagement.dto.ProjectDTO;
import com.circuitguard.ai.usermanagement.dto.UserDTO;
import com.circuitguard.ai.usermanagement.dto.UserGroupDTO;
import com.circuitguard.ai.usermanagement.model.UserGroupModel;
import com.circuitguard.utils.Populator;
import org.springframework.stereotype.Component;

@Component
public class UserGroupPopulator implements Populator<UserGroupModel, UserGroupDTO> {

    private final ProjectPopulator projectPopulator;
    private final UserPopulator userPopulator;

    public UserGroupPopulator(ProjectPopulator projectPopulator, UserPopulator userPopulator) {
        this.projectPopulator = projectPopulator;
        this.userPopulator = userPopulator;
    }

    @Override
    public void populate(UserGroupModel source, UserGroupDTO target) {
        if (source == null || target == null) {
            return;
        }

        target.setId(source.getId());
        target.setGroupName(source.getGroupName());
        target.setDescription(source.getDescription());
        target.setPriority(source.getPriority());

        if (source.getProject() != null) {
            ProjectDTO projectDTO = new ProjectDTO();

//            projectPopulator.populate(source.getProject(), projectDTO);
            projectDTO.setId(source.getProject().getId());
            projectDTO.setName(source.getProject().getName());
            target.setProject(projectDTO);
        }

        if (source.getGroupLead() != null) {
            UserDTO leadDTO = new UserDTO();
            userPopulator.populate(source.getGroupLead(), leadDTO);
            target.setGroupLead(leadDTO);
        }
    }

    public UserGroupDTO toDTO(UserGroupModel source) {
        UserGroupDTO dto = new UserGroupDTO();
        populate(source, dto);
        return dto;
    }
}
