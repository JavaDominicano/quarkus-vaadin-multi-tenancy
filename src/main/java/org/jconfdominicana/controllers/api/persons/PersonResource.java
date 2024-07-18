package org.jconfdominicana.controllers.api.persons;


import io.quarkus.arc.Unremovable;

import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import lombok.RequiredArgsConstructor;
import org.jconfdominicana.config.TenantContext;
import org.jconfdominicana.repositories.PersonRepository;

import java.util.stream.Collectors;

@Unremovable
@RequestScoped
@RequiredArgsConstructor
public class PersonResource {

    private final PersonRepository personRepository;

    @GET
    public String get() {
        int size = personRepository.findAll().collect(Collectors.toSet()).size();
        System.out.println(size);
        return "testing";
    }
}
