package com.mobiletechnologies.simregistration.repository;

import com.mobiletechnologies.simregistration.model.Registration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegistrationRepository  extends JpaRepository<Registration, Long> {

    Registration findByMsisdnIs(String msisdn);
}
