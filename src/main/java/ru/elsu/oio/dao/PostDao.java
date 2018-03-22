package ru.elsu.oio.dao;

import ru.elsu.oio.entity.Post;

public interface PostDao {
    public Post getById(Long id);
    public void save(Post post);
    public void delete(Post post);
}
