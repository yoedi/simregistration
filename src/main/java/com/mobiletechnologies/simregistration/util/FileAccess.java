package com.mobiletechnologies.simregistration.util;

import com.mobiletechnologies.simregistration.model.Registration;
import com.mobiletechnologies.simregistration.model.value.Gender;
import com.mobiletechnologies.simregistration.model.value.SimType;
import org.jboss.logging.Logger;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class FileAccess {

    Logger logger = Logger.getLogger(FileAccess.class);

    public List<Registration> readFile(String filePath) {
        List<Registration> listMsisdn = new ArrayList<>();
        String pathStr = "/home/yoedi/Documents/Workspaces/simregistration/src/main/resources";

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        try {
            File file = new File(filePath);
            InputStream inputStream = new FileInputStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

            listMsisdn = br.lines().filter( line -> {
                boolean validData = false;
                try {
                    Registration registration = passLineToRegistration(line);

                    Set<ConstraintViolation<Registration>> constraintViolations = validator.validate(registration);
                    if (constraintViolations.size() > 0) {
                        ConstraintViolation<Registration> invalidElement = constraintViolations.iterator().next();
                        logger.info("Data : ["+line+"]  Error : " + invalidElement.getInvalidValue() + "|" + invalidElement.getMessage());
                    } else {
                        validData = true;
                    }
                } catch (Exception e) {
                    logger.info("Data : ["+line+"] Error : "+e.getMessage());
                }
                return validData;

            }).map( lineValid -> {
                Registration registration = passLineToRegistration(lineValid);

                Path path = Path.of(pathStr, registration.getMsisdn() + ".txt");
                try {
                    Files.writeString(path, lineValid);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                logger.info("Data : ["+lineValid+"] Success");

                return registration;
            }).collect(Collectors.toList());

            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return listMsisdn;
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
//                    0,
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
