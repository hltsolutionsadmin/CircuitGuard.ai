package com.circuitguard.ai.usermanagement.populator;

import com.circuitguard.ai.usermanagement.dto.TicketCommentDTO;
import com.circuitguard.ai.usermanagement.model.TicketCommentModel;
import com.skillrat.utils.Populator;
import org.springframework.stereotype.Component;

@Component
public class TicketCommentPopulator implements Populator<TicketCommentModel, TicketCommentDTO> {

    @Override
    public void populate(TicketCommentModel source, TicketCommentDTO target) {
        if (source == null || target == null) {
            return;
        }

        target.setId(source.getId());
        target.setComment(source.getComment());
        target.setCreatedAt(source.getCreatedAt());

        if (source.getCreatedBy() != null) {
            target.setCreatedById(source.getCreatedBy().getId());
//            target.setCreatedByName(source.getCreatedBy().getFullName());
        }

        if (source.getTicket() != null) {
            target.setTicketId(source.getTicket().getId());
        }
    }
}
