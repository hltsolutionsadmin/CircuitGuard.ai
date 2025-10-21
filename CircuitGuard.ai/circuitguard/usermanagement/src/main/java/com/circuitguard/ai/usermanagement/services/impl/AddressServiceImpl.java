package com.circuitguard.ai.usermanagement.services.impl;

import com.skillrat.auth.exception.handling.ErrorCode;
import com.skillrat.auth.exception.handling.HltCustomerException;
import com.skillrat.commonservice.user.UserDetailsImpl;
import com.circuitguard.ai.usermanagement.dto.AddressDTO;
import com.circuitguard.ai.usermanagement.model.AddressModel;
import com.circuitguard.ai.usermanagement.model.UserModel;
import com.circuitguard.ai.usermanagement.populator.AddressPopulator;
import com.circuitguard.ai.usermanagement.repository.AddressRepository;
import com.circuitguard.ai.usermanagement.services.AddressService;
import com.circuitguard.ai.usermanagement.services.UserService;
import com.skillrat.utils.SRBaseEndpoint;
import com.skillrat.utils.SecurityUtils;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AddressServiceImpl extends SRBaseEndpoint implements AddressService {

    private final AddressRepository addressRepository;
    private final UserService userService;
    private final AddressPopulator addressPopulator;

    private AddressDTO convertToDTO(AddressModel addressModel) {
        AddressDTO addressDTO = new AddressDTO();
        addressPopulator.populate(addressModel, addressDTO);
        return addressDTO;
    }

    @Override
    public AddressDTO saveOrUpdateAddress(AddressDTO addressDTO) {
        UserDetailsImpl loggedInUser = SecurityUtils.getCurrentUserDetails();
        Long userId = addressDTO.getUserId() != null ? addressDTO.getUserId() : loggedInUser.getId();
        UserModel userModel = userService.findById(userId);
        if (userModel == null) {
            throw new HltCustomerException(ErrorCode.USER_NOT_FOUND);
        }
        if (addressDTO.getAddressLine1() == null || addressDTO.getCity() == null || addressDTO.getCountry() == null) {
            throw new HltCustomerException(ErrorCode.INVALID_ADDRESS);
        }
        AddressModel addressEntity;
        if (addressDTO.getId() != null) {
            addressEntity = addressRepository.findById(addressDTO.getId())
                    .orElseThrow(() -> new HltCustomerException(ErrorCode.ADDRESS_NOT_FOUND));
            if (!addressEntity.getUser().getId().equals(userId)) {
                throw new HltCustomerException(ErrorCode.ACCESS_DENIED);
            }
        } else {
            addressEntity = new AddressModel();
        }
        if (addressDTO.getIsDefault() != null && addressDTO.getIsDefault()) {
            AddressModel currentDefault = addressRepository.findByUserIdAndIsDefaultTrue(userId);
            if (currentDefault != null && !currentDefault.getId().equals(addressDTO.getId())) {
                currentDefault.setIsDefault(false);
                addressRepository.save(currentDefault);
            }
        }
        addressEntity.setAddressLine1(addressDTO.getAddressLine1());
        addressEntity.setAddressLine2(addressDTO.getAddressLine2());
        addressEntity.setStreet(addressDTO.getStreet());
        addressEntity.setCity(addressDTO.getCity());
        addressEntity.setState(addressDTO.getState());
        addressEntity.setCountry(addressDTO.getCountry());
        addressEntity.setPostalCode(addressDTO.getPostalCode());
        addressEntity.setLatitude(addressDTO.getLatitude());
        addressEntity.setLongitude(addressDTO.getLongitude());
        addressEntity.setIsDefault(Boolean.TRUE.equals(addressDTO.getIsDefault()));
        UserModel user = new UserModel();
        user.setId(userId);
        addressEntity.setUser(user);
        AddressModel saved = addressRepository.save(addressEntity);
        return convertToDTO(saved);
    }

    @Override
    public AddressDTO setDefaultAddress(Long userId, Long addressId) {
        if (addressId == null) {
            throw new HltCustomerException(ErrorCode.INVALID_ADDRESS);
        }
        UserModel userModel = userService.findById(userId);
        if (userModel == null) {
            throw new HltCustomerException(ErrorCode.USER_NOT_FOUND);
        }
        AddressModel addressToSet = addressRepository.findById(addressId)
                .orElseThrow(() -> new HltCustomerException(ErrorCode.ADDRESS_NOT_FOUND));
        if (!addressToSet.getUser().getId().equals(userId)) {
            throw new HltCustomerException(ErrorCode.ACCESS_DENIED);
        }
        AddressModel currentDefault = addressRepository.findByUserIdAndIsDefaultTrue(userId);
        if (currentDefault != null && !currentDefault.getId().equals(addressId)) {
            currentDefault.setIsDefault(false);
            addressRepository.save(currentDefault);
        }
        addressToSet.setIsDefault(true);
        AddressModel saved = addressRepository.save(addressToSet);
        return convertToDTO(saved);
    }

    @Override
    public AddressDTO getAddressById(Long id) {
        AddressModel addressModel = addressRepository.findById(id)
                .orElseThrow(() -> new HltCustomerException(ErrorCode.ADDRESS_NOT_FOUND));
        return convertToDTO(addressModel);
    }

    @Override
    public void deleteAddressById(Long id) {
        AddressModel addressModel = addressRepository.findById(id)
                .orElseThrow(() -> new HltCustomerException(ErrorCode.ADDRESS_NOT_FOUND));
        addressRepository.delete(addressModel);
    }

    @Override
    public AddressDTO getDefaultAddress(Long userId) {
        UserModel userModel = userService.findById(userId);
        if (userModel == null) {
            throw new HltCustomerException(ErrorCode.USER_NOT_FOUND);
        }
        AddressModel defaultAddress = addressRepository.findByUserIdAndIsDefaultTrue(userId);
        if (defaultAddress == null) {
            throw new HltCustomerException(ErrorCode.ADDRESS_NOT_FOUND);
        }
        return convertToDTO(defaultAddress);
    }

    @Override
    public Page<AddressDTO> getAllAddresses(Long userId, Pageable pageable) {
        Page<AddressModel> addressPage;
        if (userId != null) {
            addressPage = addressRepository.findByUserId(userId, pageable);
        } else {
            UserDetailsImpl loggedInUser = SecurityUtils.getCurrentUserDetails();
            addressPage = addressRepository.findByUserId(loggedInUser.getId(), pageable);
        }
        return addressPage.map(this::convertToDTO);
    }
//
//    @Override
//    public Page<AddressDTO> getAddressesByBusinessId(Long businessId, Pageable pageable) {
//        Page<AddressModel> page = addressRepository.findAddressesByBusinessId(businessId, pageable);
//        return page.map(this::convertToDTO);
//    }
}