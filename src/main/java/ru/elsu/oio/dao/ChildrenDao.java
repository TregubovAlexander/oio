package ru.elsu.oio.dao;

import ru.elsu.oio.entity.Children;

import java.util.Date;
import java.util.List;

public interface ChildrenDao {
    public Children getById(Long id);
    public List<Children> getAll();
    public Children get(String surname, String name, Date dr, String gender);
    public void save(Children children);
    public void delete(Children children);
}
