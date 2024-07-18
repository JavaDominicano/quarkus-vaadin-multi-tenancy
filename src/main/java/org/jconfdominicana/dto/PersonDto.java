package org.jconfdominicana.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.ToString;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link org.jconfdominicana.model.Person}
 */
@Value
@ToString
public class PersonDto implements Serializable {
    @NotNull
    @Size(min = 1, max = 50)
    String firstName;
    @NotNull
    @Size(min = 1, max = 50)
    String lastName;
    @NotNull
    @Size(min = 1, max = 100)
    @Email
    String email;
    @NotNull
    @Size(min = 1, max = 50)
    String phone;
    @NotNull
    LocalDate dateOfBirth;
    @NotNull
    @Size(min = 1, max = 100)
    String occupation;
    @NotNull
    @Size(min = 1, max = 50)
    String role;
    boolean important;
}