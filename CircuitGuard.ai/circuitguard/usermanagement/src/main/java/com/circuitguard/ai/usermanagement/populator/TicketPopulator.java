
package com.circuitguard.ai.usermanagement.populator;

import com.circuitguard.ai.usermanagement.dto.TicketCommentDTO;
import com.circuitguard.ai.usermanagement.dto.TicketDTO;
import com.circuitguard.ai.usermanagement.model.TicketModel;
import com.circuitguard.utils.Populator;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class TicketPopulator implements Populator<TicketModel, TicketDTO> {

    private final TicketCommentPopulator commentPopulator;

    public TicketPopulator(TicketCommentPopulator commentPopulator) {
        this.commentPopulator = commentPopulator;
    }

    @Override
    public void populate(TicketModel source, TicketDTO target) {
        if (source == null || target == null) {
            return;
        }

        target.setId(source.getId());
        target.setTitle(source.getTitle());
        target.setDescription(source.getDescription());
        target.setPriority(source.getPriority());
        target.setStatus(source.getStatus());
        target.setDueDate(source.getDueDate());
        target.setResolvedAt(source.getResolvedAt());
        target.setArchived(source.getArchived());

        if (source.getProject() != null) {
            target.setProjectId(source.getProject().getId());
        }

        if (source.getCreatedBy() != null) {
            target.setCreatedById(source.getCreatedBy().getId());
//            target.setCreatedByName(source.getCreatedBy().getFullName());
        }

        if (source.getAssignedTo() != null) {
            target.setAssignedToId(source.getAssignedTo().getId());
//            target.setAssignedToName(source.getAssignedTo().getFullName());
        }

        // Map comments
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
