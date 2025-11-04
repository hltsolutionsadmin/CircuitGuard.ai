package com.circuitguard.ai.usermanagement.populator;

import com.circuitguard.ai.usermanagement.dto.TicketCommentDTO;
import com.circuitguard.ai.usermanagement.dto.TicketDTO;
import com.circuitguard.ai.usermanagement.dto.UserGroupDTO;
import com.circuitguard.ai.usermanagement.model.TicketModel;
import com.circuitguard.utils.Populator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class TicketPopulator implements Populator<TicketModel, TicketDTO> {

    private final TicketCommentPopulator commentPopulator;
    private final UserGroupPopulator userGroupPopulator;

    @Autowired
    public TicketPopulator(TicketCommentPopulator commentPopulator,
                           UserGroupPopulator userGroupPopulator) {
        this.commentPopulator = commentPopulator;
        this.userGroupPopulator = userGroupPopulator;
    }

    @Override
    public void populate(TicketModel source, TicketDTO target) {
        if (source == null || target == null) {
            return;
        }

        target.setId(source.getId());
        target.setTicketId(source.getTicketId());
//        target.setTicketNumber(source.getTicketNumber());
        target.setTitle(source.getTitle());
        target.setDescription(source.getDescription());
        target.setPriority(source.getPriority());
        target.setStatus(source.getStatus());
        target.setIssueType(source.getIssueType());
        target.setImpact(source.getImpact());
        target.setUrgency(source.getUrgency());
        target.setDueDate(source.getDueDate());
        target.setResolvedAt(source.getResolvedAt());
        target.setArchived(source.getArchived());

        if (source.getProject() != null) {
            target.setProjectId(source.getProject().getId());
        }

        if (source.getGroup() != null) {
            UserGroupDTO groupDTO = new UserGroupDTO();
            userGroupPopulator.populate(source.getGroup(), groupDTO);
            target.setUserGroupDTO(groupDTO);
        }

        if (source.getCreatedBy() != null) {
            target.setCreatedById(source.getCreatedBy().getId());
            target.setCreatedByName(source.getCreatedBy().getFullName());
        }

        if (source.getAssignedTo() != null) {
            target.setAssignedToId(source.getAssignedTo().getId());
            target.setAssignedToName(source.getAssignedTo().getFullName());
        }

        if (source.getCategory() != null) {
            target.setCategoryId(source.getCategory().getId());
            target.setCategoryName(source.getCategory().getName());
        }

        if (source.getSubCategory() != null) {
            target.setSubCategoryId(source.getSubCategory().getId());
            target.setSubCategoryName(source.getSubCategory().getName());
        }

        if (source.getComments() != null) {
            target.setComments(
                    source.getComments().stream()
                            .map(comment -> {
                                TicketCommentDTO dto = new TicketCommentDTO();
                                commentPopulator.populate(comment, dto);
                                return dto;
                            })
                            .collect(Collectors.toList())
            );
        }
    }

    public TicketDTO toDTO(TicketModel source) {
        TicketDTO dto = new TicketDTO();
        populate(source, dto);
        return dto;
    }
}
