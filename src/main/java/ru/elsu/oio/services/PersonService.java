package ru.elsu.oio.services;

import ru.elsu.oio.dto.PersonDto;
import ru.elsu.oio.entity.Person;

import java.util.Date;
import java.util.List;

public interface PersonService {
    public Person getById(Long id);
    public List<Person> getAll();
    public List<Person> getAllInitialized();
    public List<Person> getAllUvolen();
    public boolean personExists(PersonDto personDto);
    public Person getByUserName(String userName); // TODO: может не нужен?
    public Person createPerson(PersonDto personDto);
    public void save(Person person);
    public void delete(Person person);
}
