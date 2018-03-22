package ru.elsu.oio.dao;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.elsu.oio.entity.Post;

@Repository
@Transactional
public class HibernatePostDao implements PostDao {

    @Autowired
    private SessionFactory sessionFactory;

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    public Post getById(Long id) {
        Post post = (Post) getCurrentSession().get(Post.class, id);
//        if (post != null) {
//            Hibernate.initialize(post.getPerson()); // Насильно инициализируем соответствующим сотрудником
//        }
        return post;
    }

    public void save(Post post) {
        getCurrentSession().saveOrUpdate(post);
    }

    public void delete(Post post) {
        getCurrentSession().delete(post);
    }

}
