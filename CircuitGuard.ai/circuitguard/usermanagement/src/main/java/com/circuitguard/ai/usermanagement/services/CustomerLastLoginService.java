package com.circuitguard.ai.usermanagement.services;


import com.circuitguard.ai.usermanagement.model.CustomerLastLoginModel;
import com.circuitguard.ai.usermanagement.model.UserModel;

public interface CustomerLastLoginService {

    CustomerLastLoginModel save(CustomerLastLoginModel customerLastLoginModel);

    CustomerLastLoginModel findByJtCustomer(UserModel userModel);
}
