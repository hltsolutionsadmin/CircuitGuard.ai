package com.circuitguard.ai.usermanagement.populator;

import com.circuitguard.ai.usermanagement.dto.UserOTPDTO;
import com.circuitguard.ai.usermanagement.model.UserOTPModel;
import com.circuitguard.utils.Populator;

import org.springframework.stereotype.Component;


@Component
public class UserOtpPopulator implements Populator<UserOTPModel, UserOTPDTO> {

    @Override
    public void populate(UserOTPModel source, UserOTPDTO target) {
        target.setId(source.getId());
        target.setCreationTime(source.getCreationTime());
        target.setOtpType(source.getOtpType());
        target.setOtp(source.getOtp());
    }

}
