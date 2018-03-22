package ru.elsu.oio.dao;

import ru.elsu.oio.users.AppUser;

public interface UserDao {
    public AppUser getById(Long id);
    public AppUser getByUserName(String userName);
    public void save(AppUser appUser);
    public void delete(AppUser appUser);
}
