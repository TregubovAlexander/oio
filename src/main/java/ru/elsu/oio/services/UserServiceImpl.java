package ru.elsu.oio.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.elsu.oio.dao.UserDao;
import ru.elsu.oio.users.AppUser;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public AppUser getLoggedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AppUser appUser = userDao.getByUserName(authentication.getName());
        return appUser;
    }
}
