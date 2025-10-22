package com.circuitguard.ai.usermanagement.populator;

import com.circuitguard.ai.usermanagement.dto.ProjectTechDTO;
import com.circuitguard.ai.usermanagement.model.ProjectTechModel;
import com.circuitguard.utils.Populator;
import org.springframework.stereotype.Component;

@Component
public class ProjectTechPopulator implements Populator<ProjectTechModel, ProjectTechDTO> {

    @Override
    public void populate(ProjectTechModel source, ProjectTechDTO target) {
        if (source == null || target == null) {
            return;
        }

        target.setId(source.getId());
        target.setTechnologyName(source.getTechnologyName());
        target.setVersion(source.getVersion());
    }
}
