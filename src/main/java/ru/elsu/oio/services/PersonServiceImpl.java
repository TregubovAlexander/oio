package ru.elsu.oio.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.elsu.oio.dao.PersonDao;
import ru.elsu.oio.dto.PersonDto;
import ru.elsu.oio.entity.Person;
import ru.elsu.oio.utils.Util;

import java.util.List;

@Service
public class PersonServiceImpl implements PersonService {

    @Autowired
    PersonDao personDao;

    @Override
    public Person getById(Long id) {
        return personDao.getById(id);
    }

    @Override
    public List<Person> getAll() {
        return personDao.getAll(false);
    }

    @Override
    public List<Person> getAllInitialized() {
        return personDao.getAllInitialized(false);
    }

    @Override
    public List<Person> getAllUvolen() {
        return personDao.getAll(true);
    }

    @Override
    public List<Person> getForTabel(int year, int month) {
        return personDao.getForTabel(year, month);
    }

    @Override
    public boolean personExists(PersonDto personDto) {
        return personDao.get(
                personDto.getSurname(),
                personDto.getName(),
                personDto.getPatronymic(),
                Util.strToDate(personDto.getDr()),
                personDto.getGender()) != null;
    }

    @Override
    public Person getByUserName(String userName) {
        return personDao.getByUserName(userName);
    }

    @Override
    public Person createPerson(PersonDto personDto) {
        Person person = personDto.toPerson();
        person.setUvolen(false);
        save(person);
        return person;
    }

    @Override
    public void save(Person person) {
        personDao.save(person);
    }

    @Override
    public void delete(Person person) {
        personDao.delete(person);
    }
}
