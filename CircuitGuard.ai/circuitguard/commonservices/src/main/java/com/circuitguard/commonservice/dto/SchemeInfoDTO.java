package com.circuitguard.commonservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor 
@AllArgsConstructor 
public class SchemeInfoDTO {
	private String schemeName;
	private String status;
	private boolean isApproved;
}
