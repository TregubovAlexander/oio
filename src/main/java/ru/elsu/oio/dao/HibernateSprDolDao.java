package ru.elsu.oio.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.elsu.oio.entity.SprDol;
import java.util.List;

@Repository
@Transactional
public class HibernateSprDolDao implements SprDolDao {

    @Autowired
    private SessionFactory sessionFactory;

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    public SprDol getById(Long id) {
        return (SprDol) getCurrentSession().get(SprDol.class, id);
    }

    public SprDol getByName(String name) {
        return (SprDol) getCurrentSession().getNamedQuery("SprDol.getByName").setParameter("name", name).uniqueResult();
    }

    public List<SprDol> getAll() {
        return getCurrentSession().getNamedQuery("SprDol.getAll").list();
    }

    public void save(SprDol sprDol) {
        getCurrentSession().saveOrUpdate(sprDol);
    }

    public void delete(SprDol sprDol) {
        getCurrentSession().delete(sprDol);
    }

}
