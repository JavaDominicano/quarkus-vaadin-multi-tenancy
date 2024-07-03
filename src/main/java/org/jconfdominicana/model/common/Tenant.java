package org.jconfdominicana.model.common;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Set;

@ToString
@Getter
@Setter
@Entity
@Table(name = "tenant", indexes = {@Index(columnList = "name")})
public class Tenant implements Serializable {

    @Id
    @NotNull
    private String tenantId;

    @NotNull
    @Size(min = 1, max = 100)
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "tenant")
    @ToString.Exclude
    private Set<TenantUser> users;

}
