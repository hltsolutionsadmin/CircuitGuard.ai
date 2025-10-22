package com.circuitguard.commonservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class SearchCriteria {
    private String fullText;
    private Set<FilterInformation> filters;

    public SearchCriteria(String fullText) {
        this.fullText = fullText;
    }

    public SearchCriteria(Set<FilterInformation> filters) {
        this.filters = CollectionUtils.isEmpty(filters) ? filters : new HashSet<>(filters);
    }

    public SearchCriteria(String fullText, Set<FilterInformation> filters) {
        this.fullText = fullText;
        this.filters = CollectionUtils.isEmpty(filters) ? filters : new HashSet<>(filters);
    }

    @JsonIgnore
    public void addFilters(final FilterInformation filterInformation) {
        if (null == this.filters) {
            this.filters = new HashSet<>();
        }
        filters.add(filterInformation);
    }
}
