package ru.elsu.oio.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.elsu.oio.dao.UserDao;
import ru.elsu.oio.users.AppUser;


@Repository
@Transactional
public class HibernateUserDao implements UserDao {

    @Autowired
    private SessionFactory sessionFactory;
    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    public AppUser getById(Long id) {
        return (AppUser) getCurrentSession().get(AppUser.class, id);
    }

    @SuppressWarnings("unchecked")
    public AppUser getByUserName(String userName) {
        return (AppUser) getCurrentSession().createQuery("from AppUser where userName=?").setParameter(0, userName).uniqueResult();
    }

    public void save(AppUser appUser) {
        getCurrentSession().saveOrUpdate(appUser);
    }

    public void delete(AppUser appUser) {
        getCurrentSession().delete(appUser);
    }

}
