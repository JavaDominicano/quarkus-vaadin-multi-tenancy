package org.jconfdominicana.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@ToString
@Getter
@Setter
@Entity
@Table(name = "person", indexes = {@Index(columnList = "firstName"), @Index(columnList = "lastName"), @Index(columnList = "email")})
public class Person implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long code;

    @NotNull
    @Size(min = 1, max = 50)
    private String firstName;

    @NotNull
    @Size(min = 1, max = 50)
    private String lastName;

    @Email
    @NotNull
    @Size(min = 1, max = 100)
    private String email;

    @NotNull
    @Size(min = 1, max = 50)
    private String phone;

    @NotNull
    private LocalDate dateOfBirth;

    @NotNull
    @Size(min = 1, max = 100)
    private String occupation;

    @NotNull
    @Size(min = 1, max = 50)
    private String role;

    private boolean important;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Person person = (Person) o;
        return getCode() != null && Objects.equals(getCode(), person.getCode());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
