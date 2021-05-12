package com.mobiletechnologies.simregistration.model;

import com.mobiletechnologies.simregistration.model.value.Gender;
import com.mobiletechnologies.simregistration.model.value.SimType;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@Entity
@Table(name = "registration", indexes = @Index(columnList = "msisdn"))
public class Registration {

    public Registration(final String msisdn, final SimType simType, final  String name, final LocalDate dateOfBirth,
                        final Gender gender, final String address, final String idNumber) {
        this.msisdn = msisdn;
        this.simType = simType;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.address = address;
        this.idNumber = idNumber;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(unique = true)
    @NotBlank(message = "MSISDN could not be empty.")
    @Pattern(regexp = "^\\+\\d{1,3}[\\d]*$",
            message = "Msisdn must a number and with country code prefix")
    private String msisdn;

    @NotNull(message = "SIM Type could not be empty.")
    @Enumerated(EnumType.STRING)
    private SimType simType;

    @NotBlank(message = "Name could not be empty.")
    @Pattern(regexp = "^[\\w\\s]*$", message = "Name must only word character")
    private String name;

    @NotNull(message = "Date of birth could not be null.")
    @Past(message = "Date of birth must before today")
    private LocalDate dateOfBirth;

    @NotNull(message = "Gender could not be empty.")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @NotBlank(message = "Address could not be empty.")
    @Size(min = 20)
    private String address;

    @NotBlank(message = "ID Number could not be empty.")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])([a-z0-9_-]+)$",
            message = "ID Number must contain word character and number digit")
    private String idNumber;

}

