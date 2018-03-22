package ru.elsu.oio.dao;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.elsu.oio.entity.Children;

import java.util.Date;
import java.util.List;

@Repository
@Transactional
public class HibernateChildrenDao implements ChildrenDao {

    @Autowired
    private SessionFactory sessionFactory;
    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    public Children getById(Long id) {
        Children children  = (Children) getCurrentSession().get(Children.class, id);
        if (children != null) {
            Hibernate.initialize(children.getPersonList()); // Насильно инициализируем списком родителей
        }
        return children;
    }

    @SuppressWarnings("unchecked")
    public List<Children> getAll() {
        List<Children> childrenList = getCurrentSession().getNamedQuery("Children.getAll").list();
        return childrenList;
    }

    /**
     * Получаем ребенка по фамилии, имени, дате рождения и полу
     *
     * @param surname   фамилия
     * @param name      имя
     * @param dr        дата рождения
     * @param gender    пол
     *
     * @return  Children (ребенок)
     */
    @SuppressWarnings("unchecked")
    public Children get(String surname, String name, Date dr, String gender) {

        Children children = (Children) getCurrentSession().getNamedQuery("Children.getBySurnameNameDrGender")
                .setParameter("surname", surname)
                .setParameter("name", name)
                .setParameter("dr", dr)
                .setParameter("gender", gender)
                .uniqueResult();
        if (children != null) {
            Hibernate.initialize(children.getPersonList()); // Насильно инициализируем списком родителей
        }
        return children;
    }

    public void save(Children children) {
        getCurrentSession().saveOrUpdate(children);
    }

    public void delete(Children children) {
        getCurrentSession().delete(children);
    }

}
