package ru.elsu.oio.dao;

import ru.elsu.oio.entity.Person;

import java.util.Date;
import java.util.List;

public interface PersonDao {
    public Person getById(Long id);
    public List<Person> getAll(boolean uvolen);
    public List<Person> getAllInitialized(boolean uvolen);
    public Person get(String surname, String name, String patronymic, Date dr, String gender);
    public Person getByUserName(String userName); // TODO: может не нужен?
    public void save(Person person);
    public void delete(Person person);
}
