package com.skillrat.commonservice.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.skillrat.commonservice.enums.FilterOperator;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

@Getter
@EqualsAndHashCode(of = {"field"})
public class FilterInformation {

    @NonNull
    private final String field;
    private final Object value;
    private final FilterOperator operator;

    @JsonCreator
    public FilterInformation(
            @JsonProperty("field") @NonNull String field,
            @JsonProperty("value") Object value,
            @JsonProperty("operator") FilterOperator operator) {
        this.field = field;
        this.value = value;
        this.operator = operator != null ? operator : FilterOperator.EQUAL_TO;
    }
}
