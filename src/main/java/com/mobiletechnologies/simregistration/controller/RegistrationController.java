package com.mobiletechnologies.simregistration.controller;

import com.mobiletechnologies.simregistration.configuration.JmsProducer;
import com.mobiletechnologies.simregistration.model.Registration;
import com.mobiletechnologies.simregistration.model.value.Gender;
import com.mobiletechnologies.simregistration.model.value.SimType;
import com.mobiletechnologies.simregistration.repository.RegistrationRepository;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("mobiletechnologies/sim")
public class RegistrationController {

    Logger log = Logger.getLogger(RegistrationController.class);

    private final String queueName = "MSGREGISTERED";

    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private JmsProducer jmsProducer;

    @PostMapping(path = "/bulk-registration")
    private String uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        long start = System.currentTimeMillis();
        String pathWrite = "/home/yoedi/Documents/Workspaces/simregistration/src/main/resources";

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        BufferedReader br = null;

        try {
            InputStream inputStream = file.getInputStream();
            br = new BufferedReader(new InputStreamReader(inputStream));

            List<Registration> listMsisdn = br.lines().filter( line -> {
                boolean validData = false;
                try {
                    Registration registration = passLineToRegistration(line);

                    Set<ConstraintViolation<Registration>> constraintViolations = validator.validate(registration);
                    if (constraintViolations.size() > 0) {
                        ConstraintViolation<Registration> invalidElement = constraintViolations.iterator().next();
                        log.info("Data : ["+line+"]  Error : " + invalidElement.getInvalidValue() + "|" + invalidElement.getMessage());
                    } else {
                        validData = true;
                    }
                } catch (Exception e) {
                    log.info("Data : ["+line+"] Error : "+e.getMessage());
                }
                return validData;

            }).map( lineValid -> {
                Registration registration = passLineToRegistration(lineValid);

                try {
                    Registration checkExist = registrationRepository.findByMsisdnIs(registration.getMsisdn());
                    if (checkExist == null) {
                        Path path = Path.of(pathWrite, registration.getMsisdn() + ".txt");
                        Files.writeString(path, lineValid);

                        registrationRepository.save(registration);
                        jmsProducer.sendMessage(queueName, lineValid);

                        log.info("Data : ["+lineValid+"] Success");
                    } else {
                        log.info("Data : ["+lineValid+"] Already registered");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return registration;
            }).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) br.close();

            long finish = System.currentTimeMillis();
            log.info("Time Elapsed = " + (finish - start));
        }

        return "Done";
    }

    private Registration passLineToRegistration(String line) {
        try {
            String[] arrayLine = line.split(",");

            String msisdn = arrayLine[0];
            SimType simType = SimType.valueOf(arrayLine[1]);
            String name = arrayLine[2];
            LocalDate dateOfBirth = LocalDate.parse(arrayLine[3]);
            Gender gender = Gender.valueOf(arrayLine[4]);
            String address = arrayLine[5];
            String idNumber = arrayLine[6];

            Registration registration = new Registration(
                    msisdn,
                    simType,
                    name,
                    dateOfBirth,
                    gender,
                    address,
                    idNumber
            );

            return registration;
        } catch (Exception e) {
            throw e;
        }
    }
}
