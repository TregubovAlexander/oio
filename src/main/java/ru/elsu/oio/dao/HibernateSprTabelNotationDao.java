package ru.elsu.oio.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.elsu.oio.entity.SprTabelNotation;

import java.util.List;

@Repository
@Transactional
public class HibernateSprTabelNotationDao implements SprTabelNotationDao {

    @Autowired
    private SessionFactory sessionFactory;

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    public SprTabelNotation getById(Long id) {
        return (SprTabelNotation) getCurrentSession().get(SprTabelNotation.class, id);
    }

    public List<SprTabelNotation> getAll() {
        return getCurrentSession().getNamedQuery("SprTabelNotation.getAll").list();
    }

    public List<SprTabelNotation> getAllActive() {
        return getCurrentSession().getNamedQuery("SprTabelNotation.getAllActive").list();
    }

    public void save(SprTabelNotation sprTabelNotation) {
        getCurrentSession().saveOrUpdate(sprTabelNotation);
    }

    public void delete(SprTabelNotation sprTabelNotation) {
        getCurrentSession().delete(sprTabelNotation);
    }

}
