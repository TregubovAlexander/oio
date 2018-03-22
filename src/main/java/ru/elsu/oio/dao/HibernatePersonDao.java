package ru.elsu.oio.dao;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.elsu.oio.controller.exeption.ResourceNotFoundException;
import ru.elsu.oio.entity.Person;

import java.util.Date;
import java.util.List;

@Repository
@Transactional
public class HibernatePersonDao implements PersonDao {

    @Autowired
    private SessionFactory sessionFactory;
    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    public Person getById(Long id) {
        Person person  = (Person) getCurrentSession().get(Person.class, id);
        if (person != null) {
            Hibernate.initialize(person.getChildrenList()); // Насильно инициализируем списком детей
        }
        return person;
    }

    /**
     * Список всех сотрудников, не проинициализированный, с учетом параметра uvolen
     *
     * @param uvolen    true - выбрать всех уволенных
     *                  false - выбрать всех работающих
     * @return  List<Person> - список всех работающих или всех уволенных сотрудников
     */
    @SuppressWarnings("unchecked")
    public List<Person> getAll(boolean uvolen) {
        List<Person> persons = getCurrentSession().getNamedQuery("Person.getAll")
                .setParameter("uvolen", uvolen)
                .list();
        return persons;
    }

    /**
     * Список всех сотрудников, проинициализированный списком их детей, с учетом параметра uvolen
     * @param uvolen    true - выбрать всех уволенных
     *                  false - выбрать всех работающих
     * @return  List<Person> - список всех работающих или всех уволенных сотрудников с их детьми
     */
    @SuppressWarnings("unchecked")
    public List<Person> getAllInitialized(boolean uvolen) {
        List<Person> persons = getAll(uvolen);
        // Насильственная инициализация
        for (Person person : persons) {
            Hibernate.initialize(person.getChildrenList()); // Насильно инициализируем списком детей
        }
        return persons;
    }

    /**
     * Получаем работающего сотрудника по ФИО, дате рождения и полу
     *
     * @param surname       фамилия
     * @param name          имя
     * @param patronymic    отчество
     * @param dr            дата рождения
     * @param gender        пол
     *
     * @return      Person
     */
    public Person get(String surname, String name, String patronymic, Date dr, String gender) {
        Person person = (Person) getCurrentSession().getNamedQuery("Person.getByFioDrGender")
                .setParameter("uvolen", false)  // Интересуют только работающие сотрудники
                .setParameter("surname", surname)
                .setParameter("name", name)
                .setParameter("patronymic", patronymic)
                .setParameter("dr", dr)
                .setParameter("gender", gender)
                .uniqueResult();
        return person;
    }

    // TODO: этот метод может и не нужен? и в personService тоже?
    @SuppressWarnings("unchecked")
    public Person getByUserName(String userName) {
        return (Person) getCurrentSession().createQuery("from Person where userName=?")
                .setParameter(0, userName)
                .uniqueResult();
    }

    public void save(Person person) {
        getCurrentSession().saveOrUpdate(person);
    }

    public void delete(Person person) {
        getCurrentSession().delete(person);
    }
}
