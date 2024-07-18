package org.jconfdominicana.controllers.api.persons;


import io.quarkus.arc.Unremovable;

import jakarta.enterprise.context.RequestScoped;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.jconfdominicana.dto.PersonDto;
import org.jconfdominicana.model.Person;
import org.jconfdominicana.services.PersonService;

import java.util.List;

@Unremovable
@RequestScoped
@RequiredArgsConstructor
public class PersonResource {

    private final PersonService personService;

    @GET
    public List<Person> retrieveAll() {
        return this.personService.retrieveAll();
    }

    @POST
    public Person create(@Valid PersonDto personDto) {
        return this.personService.create(personDto);
    }

    @GET
    @Path("/{id}")
    public Person retrieveById(@PathParam("id") long id) {
        return this.personService.getById(id);
    }

    @Path("/{id}")
    @PUT
    public Person update(@PathParam("id") long id, PersonDto data) {
        return personService.update(id, data);
    }

    @Path("/{id}")
    @DELETE
    public Response delete(@PathParam("id") long id) {
        personService.delete(id);
        return Response.noContent().build();
    }
}
