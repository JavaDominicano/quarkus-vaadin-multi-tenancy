package org.jconfdominicana.controllers.api.persons;


import io.quarkus.arc.Unremovable;

import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.*;
import lombok.RequiredArgsConstructor;
import org.jconfdominicana.config.TenantContext;
import org.jconfdominicana.model.Person;
import org.jconfdominicana.repositories.PersonRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Unremovable
@RequestScoped
@RequiredArgsConstructor
public class PersonResource {

    private final PersonRepository personRepository;

    @GET
    public List<Person> retrieveAll() {
        List<Person> persons = personRepository.findAll().collect(Collectors.toList());
        return persons;
    }

    @Path("/{id}")
    @GET
    public Person retrieveById(@PathParam("id") long id) {
        Person person = personRepository.findById(id).orElseThrow(() -> new NotFoundException());
        return person;
    }
}
