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
    public TabelHolidays getById(Long id) {
        return (TabelHolidays) getCurrentSession().get(TabelHolidays.class, id);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<TabelHolidays> get(int year) {
        return (List<TabelHolidays>) getCurrentSession().getNamedQuery("TabelHolidays.getByYear")
                .setParameter("year", year)
                .list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<TabelHolidays> get(int year, int month) {
        return (List<TabelHolidays>) getCurrentSession().getNamedQuery("TabelHolidays.getByYearMonth")
                .setParameter("year", year)
                .setParameter("month", month)
                .list();
    }

    @Override
    public TabelHolidays get(int year, int month, int day) {
        return (TabelHolidays) getCurrentSession().getNamedQuery("TabelHolidays.getByYearMonthDay")
                .setParameter("year", year)
                .setParameter("month", month)
                .setParameter("day", day)
                .uniqueResult();
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
