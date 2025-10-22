package com.circuitguard.ai.usermanagement.services.impl;

import com.circuitguard.ai.usermanagement.dto.OrganizationDTO;
import com.circuitguard.ai.usermanagement.model.OrganizationModel;
import com.circuitguard.ai.usermanagement.model.RoleModel;
import com.circuitguard.ai.usermanagement.model.UserModel;
import com.circuitguard.ai.usermanagement.populator.OrganizationPopulator;
import com.circuitguard.ai.usermanagement.repository.OrganizationRepository;
import com.circuitguard.ai.usermanagement.repository.RoleRepository;
import com.circuitguard.ai.usermanagement.services.OrganizationService;
import com.circuitguard.auth.exception.handling.ErrorCode;
import com.circuitguard.auth.exception.handling.HltCustomerException;
import com.circuitguard.commonservice.enums.ERole;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrganizationServiceImpl implements OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final OrganizationPopulator organizationPopulator;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;



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

            UserModel adminUser = new UserModel();
            adminUser.setUsername(dto.getAdminUsername());
            adminUser.setFullName(dto.getAdminFullName());
            adminUser.setPrimaryContact(dto.getAdminPrimaryContact());
            adminUser.setOrganization(model);

            String plainPassword = generateRandomPassword(10);
            adminUser.setPassword(passwordEncoder.encode(plainPassword));
            Set<ERole> rolesToAssign = Set.of(ERole.ROLE_BUSINESS_ADMIN);
            adminUser.setRoles(fetchRoles(rolesToAssign));

            model.getUsers().add(adminUser);

            dto.setGeneratedPassword(plainPassword);
            dto.setGeneratedUsername(adminUser.getUsername());
        }

        OrganizationModel saved = organizationRepository.save(model);

        OrganizationDTO response = new OrganizationDTO();
        organizationPopulator.populate(saved, response);

        if (dto.getId() == null) {
            response.setGeneratedUsername(dto.getGeneratedUsername());
            response.setGeneratedPassword(dto.getGeneratedPassword());
        }

        return response;
    }

    private Set<RoleModel> fetchRoles(Set<ERole> roles) {
        return roleRepository.findByNameIn(roles);
    }

    private String generateRandomPassword(int length) {
        String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
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
