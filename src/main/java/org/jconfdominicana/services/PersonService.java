package org.jconfdominicana.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.InternalServerErrorException;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.jboss.logging.Logger;
import org.jconfdominicana.dto.PersonDto;
import org.jconfdominicana.model.Person;
import org.jconfdominicana.repositories.PersonRepository;
import org.jconfdominicana.utlis.CustomBeanUtils;

import java.util.List;

@ApplicationScoped
@RequiredArgsConstructor
public class PersonService {

    public static final Logger LOGGER = Logger.getLogger(PersonService.class);

    private final PersonRepository personRepository;
    private final ObjectMapper objectMapper;

    @Transactional(Transactional.TxType.SUPPORTS)
    public List<Person> retrieveAll() {
        return personRepository.findAll()
                .toList();
    }

    @Transactional
    public Person create(PersonDto personDto) {
        LOGGER.info("Creating person: " + personDto.toString());
        Person person = objectMapper.convertValue(personDto, Person.class);
        return personRepository.insert(person);
    }

    @Transactional
    public Person update(Long id, PersonDto personDto) {
        LOGGER.info("Updating person with id: " + id);

        Person personExisted = this.getById(id);

        try {
            CustomBeanUtils.copyNonNullProperties(personDto, personExisted);
        } catch (Exception ex) {
            throw new InternalServerErrorException();
        }

        return personRepository.update(personExisted);
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    public Person getById(Long id) {
        Person person = this.personRepository.findById(id)
                .orElse(null);

        if (person == null) throw new NotFoundException();

        return person;

    }

    @Transactional
    public boolean delete(Long id) {
        LOGGER.info("Deleting person with id: " + id);
        Person person = this.getById(id);
        personRepository.delete(person);
        return true;
    }


}
