package com.circuitguard.ai.usermanagement.services.impl;

import com.circuitguard.ai.usermanagement.dto.OrganizationDTO;
import com.circuitguard.ai.usermanagement.model.OrganizationModel;
import com.circuitguard.ai.usermanagement.populator.OrganizationPopulator;
import com.circuitguard.ai.usermanagement.repository.OrganizationRepository;
import com.circuitguard.ai.usermanagement.services.OrganizationService;
import com.circuitguard.auth.exception.handling.ErrorCode;
import com.circuitguard.auth.exception.handling.HltCustomerException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrganizationServiceImpl implements OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final OrganizationPopulator organizationPopulator;

    @Override
    @Transactional
    public OrganizationDTO saveOrUpdateOrganization(OrganizationDTO dto) {

        OrganizationModel model;

        if (dto.getId() != null) {
            model = organizationRepository.findById(dto.getId())
                    .orElseThrow(() -> new HltCustomerException(ErrorCode.BUSINESS_NOT_FOUND));
            model.setName(dto.getName());
            model.setDescription(dto.getDescription());
            model.setDomainName(dto.getDomainName());
            model.setActive(dto.getActive());
        } else {
            if (organizationRepository.existsByNameIgnoreCase(dto.getName())) {
                throw new HltCustomerException(ErrorCode.BUSINESS_CODE_ALREADY_EXISTS);
            }
            model = OrganizationModel.builder()
                    .name(dto.getName())
                    .description(dto.getDescription())
                    .domainName(dto.getDomainName())
                    .active(true)
                    .build();
        }

        OrganizationModel saved = organizationRepository.save(model);
        OrganizationDTO response = new OrganizationDTO();
        organizationPopulator.populate(saved, response);
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public OrganizationDTO getOrganizationById(Long organizationId) {
        OrganizationModel model = organizationRepository.findByIdAndActiveTrue(organizationId)
                .orElseThrow(() -> new HltCustomerException(ErrorCode.BUSINESS_NOT_FOUND));
        OrganizationDTO dto = new OrganizationDTO();
        organizationPopulator.populate(model, dto);
        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrganizationDTO> getAllOrganizations(Pageable pageable, String name, Boolean active) {

        Page<OrganizationModel> page = organizationRepository.findAll(pageable);
        List<OrganizationDTO> dtos = page.stream()
                .map(model -> {
                    OrganizationDTO dto = new OrganizationDTO();
                    organizationPopulator.populate(model, dto);
                    return dto;
                }).collect(Collectors.toList());

        return new PageImpl<>(dtos, pageable, page.getTotalElements());
    }

    @Override
    @Transactional
    public void deactivateOrganization(Long organizationId) {
        OrganizationModel model = organizationRepository.findByIdAndActiveTrue(organizationId)
                .orElseThrow(() -> new HltCustomerException(ErrorCode.BUSINESS_NOT_FOUND));
        model.setActive(false);
        organizationRepository.save(model);
    }
}
