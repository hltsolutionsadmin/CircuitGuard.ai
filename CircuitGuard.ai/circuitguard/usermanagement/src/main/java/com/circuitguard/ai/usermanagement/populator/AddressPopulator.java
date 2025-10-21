package com.circuitguard.ai.usermanagement.populator;

import com.circuitguard.ai.usermanagement.dto.AddressDTO;
import com.circuitguard.ai.usermanagement.model.AddressModel;
import com.skillrat.utils.Populator;

import org.springframework.stereotype.Component;

@Component
public class AddressPopulator implements Populator<AddressModel, AddressDTO> {

    @Override
    public void populate(AddressModel source, AddressDTO target) {
        if (source == null || target == null) {
            return;
        }

        target.setId(source.getId());
        target.setAddressLine1(source.getAddressLine1());
        target.setAddressLine2(source.getAddressLine2());
        target.setStreet(source.getStreet());
        target.setCity(source.getCity());
        target.setState(source.getState());
        target.setCountry(source.getCountry());
        target.setPostalCode(source.getPostalCode());
        target.setLatitude(source.getLatitude());
        target.setLongitude(source.getLongitude());
        target.setIsDefault(source.getIsDefault());

        if (source.getUser() != null) {
            target.setUserId(source.getUser().getId());
        }
    }
}
