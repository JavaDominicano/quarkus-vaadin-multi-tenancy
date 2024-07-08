package org.jconfdominicana.model.common;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;
import org.jconfdominicana.config.CurrentTenantResolver;

import java.io.Serializable;
import java.util.Objects;

@ToString
@Getter
@Setter
@Entity
@Table(name = "tenant_user", schema = CurrentTenantResolver.DEFAULT)
public class TenantUser implements Serializable {

    @EmbeddedId
    private TenantUserKey id;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("tenantId")
    @JoinColumn(name = "tenant_id")
    @ToString.Exclude
    private Tenant tenant;

    @ManyToOne
    @MapsId("username")
    @JoinColumn(name = "username")
    private User user;

    private boolean disabled;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        TenantUser that = (TenantUser) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
