package com.circuitguard.ai.usermanagement.populator;

import com.circuitguard.ai.usermanagement.dto.OrganizationDTO;
import com.circuitguard.ai.usermanagement.model.OrganizationModel;
import org.springframework.stereotype.Component;

@Component
public class OrganizationPopulator {

    public void populate(OrganizationModel source, OrganizationDTO target) {
        if (source == null || target == null) {
            return;
        }

        target.setId(source.getId());
        target.setName(source.getName());
        target.setDescription(source.getDescription());
        target.setDomainName(source.getDomainName());
        target.setActive(source.getActive());
    }
}
