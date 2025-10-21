package com.circuitguard.ai.usermanagement.services.impl;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.circuitguard.ai.usermanagement.model.CustomerLastLoginModel;
import com.circuitguard.ai.usermanagement.model.UserModel;
import com.circuitguard.ai.usermanagement.repository.CustomerLastLoginRepository;
import com.circuitguard.ai.usermanagement.services.CustomerLastLoginService;

@Service
public class CustomerLastLoginServiceImpl implements CustomerLastLoginService {

    @Autowired
    private CustomerLastLoginRepository customerLastLoginRepository;

    @Override
    @Transactional
    public CustomerLastLoginModel save(CustomerLastLoginModel customerLastLoginModel) {
        return customerLastLoginRepository.save(customerLastLoginModel);
    }

    @Override
    public CustomerLastLoginModel findByJtCustomer(UserModel userModel) {
        return customerLastLoginRepository.findByCustomer(userModel);
    }

}
