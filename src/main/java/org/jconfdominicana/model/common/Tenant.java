package org.jconfdominicana.model.common;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

@ToString
@Getter
@Setter
@Entity
@Table(name = "tenant", indexes = {@Index(columnList = "name")})
public class Tenant implements Serializable {

    @Id
    @NotNull
    @Column(name = "tenant_id")
    private String tenantId;

    @NotNull
    @Size(min = 1, max = 100)
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "tenant")
    @ToString.Exclude
    private Set<TenantUser> users;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Tenant tenant = (Tenant) o;
        return getTenantId() != null && Objects.equals(getTenantId(), tenant.getTenantId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
