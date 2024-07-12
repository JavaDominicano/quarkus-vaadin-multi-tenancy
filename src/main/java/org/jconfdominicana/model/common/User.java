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
import org.hibernate.annotations.OptimisticLock;
import org.hibernate.proxy.HibernateProxy;
import org.jconfdominicana.config.CurrentTenantResolver;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


@Entity
@Setter
@Getter
@ToString
@Builder
@UserDefinition
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "users", schema = CurrentTenantResolver.DEFAULT, indexes = {@Index(columnList = "username")})
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

    @Transient
    private Boolean policyCheckbox;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    @OptimisticLock(excluded = true)
    private Set<TenantUser> tenants = new HashSet<>();

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        User user = (User) o;
        return getUsername() != null && Objects.equals(getUsername(), user.getUsername());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
