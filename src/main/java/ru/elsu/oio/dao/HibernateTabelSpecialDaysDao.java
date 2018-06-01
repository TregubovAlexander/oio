package ru.elsu.oio.dao;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.elsu.oio.entity.Person;
import ru.elsu.oio.entity.TabelSpDays;

import java.util.Date;
import java.util.List;

@Repository
@Transactional
public class HibernateTabelSpecialDaysDao implements TabelSpDaysDao {

    @Autowired
    private SessionFactory sessionFactory;

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }


    @Override
    public TabelSpDays getById(Long id) {
        TabelSpDays spDays = (TabelSpDays) getCurrentSession().get(TabelSpDays.class, id);
        if (spDays != null) {
            Hibernate.initialize(spDays.getPerson()); // Насильно инициализируем сотрудником
        }
        return spDays;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<TabelSpDays> get(Date date1, Date date2) {
        List<TabelSpDays> tabelSpDays = getCurrentSession().getNamedQuery("TabelSpDays.get")
                .setParameter("date1", date1)
                .setParameter("date2", date2)
                .list();
        // Насильственная инициализация
        for (TabelSpDays tsd : tabelSpDays) {
            Hibernate.initialize(tsd.getPerson()); // Насильно инициализируем сотрудником
        }
        return tabelSpDays;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<TabelSpDays> get(Person person, Date date1, Date date2) {
        return getCurrentSession().getNamedQuery("TabelSpDays.getByPerson")
                .setParameter("person", person)
                .setParameter("date1", date1)
                .setParameter("date2", date2)
                .list();
    }

    @Override
    public void save(TabelSpDays tabelSpDays) {
        getCurrentSession().saveOrUpdate(tabelSpDays);
    }

    @Override
    public void delete(TabelSpDays tabelSpDays) {
        getCurrentSession().delete(tabelSpDays);
    }
}
