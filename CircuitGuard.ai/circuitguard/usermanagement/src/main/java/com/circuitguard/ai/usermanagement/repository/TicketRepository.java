package com.circuitguard.ai.usermanagement.repository;



import com.circuitguard.ai.usermanagement.model.TicketModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<TicketModel, Long> {
}
