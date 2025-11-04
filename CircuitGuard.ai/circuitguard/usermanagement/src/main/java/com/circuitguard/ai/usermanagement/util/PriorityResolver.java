package com.circuitguard.ai.usermanagement.util;

import com.circuitguard.ai.usermanagement.dto.enums.Impact;
import com.circuitguard.ai.usermanagement.dto.enums.TicketPriority;
import com.circuitguard.ai.usermanagement.dto.enums.Urgency;

/**
 * Resolves ticket priority based on Impact and Urgency using the defined matrix.
 *
 * Matrix:
 *              Impact
 * Urgency |  LOW     MEDIUM    HIGH     CRITICAL
 * -----------------------------------------------
 * LOW     |  P4      P4        P3        P2
 * MEDIUM  |  P4      P3        P3        P2
 * HIGH    |  P3      P3        P2        P1
 * CRITICAL|  P2      P2        P1        P1
 *
 * Where: P1->CRITICAL, P2->HIGH, P3->MEDIUM, P4->LOW
 */
public final class PriorityResolver {

    private PriorityResolver() {}

    public static TicketPriority resolve(Impact impact, Urgency urgency) {
        if (impact == null || urgency == null) {
            // Sensible fallback if inputs are missing
            return TicketPriority.LOW; // P4
        }

        switch (urgency) {
            case LOW:
                switch (impact) {
                    case LOW:
                    case MEDIUM:
                        return TicketPriority.LOW; // P4
                    case HIGH:
                        return TicketPriority.MEDIUM; // P3
                    case CRITICAL:
                        return TicketPriority.HIGH; // P2
                }
                break;
            case MEDIUM:
                switch (impact) {
                    case LOW:
                        return TicketPriority.LOW; // P4
                    case MEDIUM:
                    case HIGH:
                        return TicketPriority.MEDIUM; // P3
                    case CRITICAL:
                        return TicketPriority.HIGH; // P2
                }
                break;
            case HIGH:
                switch (impact) {
                    case LOW:
                    case MEDIUM:
                        return TicketPriority.MEDIUM; // P3
                    case HIGH:
                        return TicketPriority.HIGH; // P2
                    case CRITICAL:
                        return TicketPriority.CRITICAL; // P1
                }
                break;
            case CRITICAL:
                switch (impact) {
                    case LOW:
                    case MEDIUM:
                        return TicketPriority.HIGH; // P2
                    case HIGH:
                    case CRITICAL:
                        return TicketPriority.CRITICAL; // P1
                }
                break;
            default:
                return TicketPriority.LOW;
        }
        return TicketPriority.LOW;
    }
}
