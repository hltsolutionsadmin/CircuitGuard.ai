package com.circuitguard.ai.usermanagement.utils;

import com.circuitguard.ai.usermanagement.dto.enums.Impact;
import com.circuitguard.ai.usermanagement.dto.enums.TicketPriority;
import com.circuitguard.ai.usermanagement.dto.enums.Urgency;

import java.util.EnumMap;
import java.util.Map;

/**
 * Resolves {@link TicketPriority} based on {@link Impact} and {@link Urgency}.
 *
 * <pre>
 *              Impact
 * Urgency |  LOW     MEDIUM    HIGH     CRITICAL
 * -----------------------------------------------
 * LOW     |  P4      P4        P3        P2
 * MEDIUM  |  P4      P3        P3        P2
 * HIGH    |  P3      P3        P2        P1
 * CRITICAL|  P2      P2        P1        P1
 *
 * Where: P1->CRITICAL, P2->HIGH, P3->MEDIUM, P4->LOW
 * </pre>
 *
 * This implementation uses a precomputed lookup table for O(1) resolution.
 */
public final class PriorityResolver {

    private static final Map<Urgency, Map<Impact, TicketPriority>> PRIORITY_MATRIX = new EnumMap<>(Urgency.class);

    static {
        // Build the matrix row by row (urgency vs impact)
        PRIORITY_MATRIX.put(Urgency.LOW, createRow(
                TicketPriority.LOW,    // LOW impact
                TicketPriority.LOW,    // MEDIUM impact
                TicketPriority.MEDIUM, // HIGH impact
                TicketPriority.HIGH    // CRITICAL impact
        ));

        PRIORITY_MATRIX.put(Urgency.MEDIUM, createRow(
                TicketPriority.LOW,
                TicketPriority.MEDIUM,
                TicketPriority.MEDIUM,
                TicketPriority.HIGH
        ));

        PRIORITY_MATRIX.put(Urgency.HIGH, createRow(
                TicketPriority.MEDIUM,
                TicketPriority.MEDIUM,
                TicketPriority.HIGH,
                TicketPriority.CRITICAL
        ));

        PRIORITY_MATRIX.put(Urgency.CRITICAL, createRow(
                TicketPriority.HIGH,
                TicketPriority.HIGH,
                TicketPriority.CRITICAL,
                TicketPriority.CRITICAL
        ));
    }

    private PriorityResolver() {
        // Utility class
    }

    /**
     * Resolves a ticket's priority using the predefined matrix.
     *
     * @param impact  The impact level (can be null)
     * @param urgency The urgency level (can be null)
     * @return The computed {@link TicketPriority}, defaults to {@link TicketPriority#LOW} if inputs are invalid.
     */
    public static TicketPriority resolve(Impact impact, Urgency urgency) {
        if (impact == null || urgency == null) {
            return TicketPriority.LOW; // safe fallback
        }
        return PRIORITY_MATRIX
                .getOrDefault(urgency, PRIORITY_MATRIX.get(Urgency.LOW))
                .getOrDefault(impact, TicketPriority.LOW);
    }

    /**
     * Builds an impact-to-priority map row for a given urgency level.
     */
    private static Map<Impact, TicketPriority> createRow(
            TicketPriority low, TicketPriority medium, TicketPriority high, TicketPriority critical) {

        Map<Impact, TicketPriority> row = new EnumMap<>(Impact.class);
        row.put(Impact.LOW, low);
        row.put(Impact.MEDIUM, medium);
        row.put(Impact.HIGH, high);
        row.put(Impact.CRITICAL, critical);
        return row;
    }
}
