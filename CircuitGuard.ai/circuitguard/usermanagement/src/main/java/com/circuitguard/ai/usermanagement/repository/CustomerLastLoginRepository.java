package com.circuitguard.ai.usermanagement.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.circuitguard.ai.usermanagement.model.CustomerLastLoginModel;
import com.circuitguard.ai.usermanagement.model.UserModel;

@Repository
public interface CustomerLastLoginRepository extends JpaRepository<CustomerLastLoginModel, Long> {

    CustomerLastLoginModel findByCustomer(UserModel userModel);
}
