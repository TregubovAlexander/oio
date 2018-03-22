package ru.elsu.oio.dao;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.elsu.oio.entity.TabelSpecialDays;

import java.util.Date;
import java.util.List;

@Repository
@Transactional
public class HibernateTabelSpecialDaysDao implements TabelSpecialDaysDao {

    @Autowired
    private SessionFactory sessionFactory;

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public List<TabelSpecialDays> get(Date date1, Date date2) {
        List<TabelSpecialDays> tabelSpecialDays = getCurrentSession().getNamedQuery("TabelSpecialDays.get")
                .setParameter("date1", date1)
                .setParameter("date2", date2)
                .list();
        // Насильственная инициализация
        for (TabelSpecialDays tsd : tabelSpecialDays) {
            Hibernate.initialize(tsd.getPerson()); // Насильно инициализируем списком детей
        }
        return tabelSpecialDays;
    }

    @Override
    public void save(TabelSpecialDays tabelSpecialDays) {
        getCurrentSession().saveOrUpdate(tabelSpecialDays);
    }

    @Override
    public void delete(TabelSpecialDays tabelSpecialDays) {
        getCurrentSession().delete(tabelSpecialDays);
    }
}
