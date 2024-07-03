package org.jconfdominicana.model.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.security.jpa.Password;
import io.quarkus.security.jpa.Roles;
import io.quarkus.security.jpa.UserDefinition;
import io.quarkus.security.jpa.Username;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;
import java.util.Set;


@Entity
@Setter
@Getter
@ToString
@Builder
@UserDefinition
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "users", indexes = {@Index(columnList = "username")})
public class User implements Serializable {

    @Id
    @NotNull
    @Size(min = 1, max = 100)
    @Column(unique = true, updatable = false)
    @Username
    private String username;

    @NotNull
    @JsonIgnore
    @Size(max = 100)
    @Password
    private String password;

    @NotNull
    @JsonIgnore
    @Size(max = 25)
    @Roles
    private String role;

    @OneToMany(mappedBy = "user")
    @ToString.Exclude
    private Set<TenantUser> tenants;
}
