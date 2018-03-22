package ru.elsu.oio.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.elsu.oio.entity.TabelHolidays;

import java.util.List;

@Repository
@Transactional
public class HibernateTabelHolidaysDao implements TabelHolidaysDao {

    @Autowired
    private SessionFactory sessionFactory;

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public List<TabelHolidays> get(int year, int month) {
        List<TabelHolidays> tabelHolidays = getCurrentSession().getNamedQuery("TabelHolidays.get")
                .setParameter("year", year)
                .setParameter("month", month)
                .list();
        return tabelHolidays;
    }

    @Override
    public void save(TabelHolidays tabelHolidays) {
        getCurrentSession().saveOrUpdate(tabelHolidays);
    }

    @Override
    public void delete(TabelHolidays tabelHolidays) {
        getCurrentSession().delete(tabelHolidays);
    }

}
