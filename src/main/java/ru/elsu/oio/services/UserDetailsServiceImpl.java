package ru.elsu.oio.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.elsu.oio.dao.UserDao;
import ru.elsu.oio.users.AppUser;
import ru.elsu.oio.users.UserRole;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserDao userDao;

    @Transactional(readOnly=true)
    public UserDetails loadUserByUsername(final String userName) throws UsernameNotFoundException {
        AppUser appUser = userDao.getByUserName(userName);
        List<GrantedAuthority> authorities = buildUserAuthority(appUser.getUserRole());
        return buildUserForAuthentication(appUser, authorities);
    }

    // Конвертируем ru.elsu.oio.AppUser в org.springframework.security.core.userdetails.User
    private User buildUserForAuthentication(AppUser appUser, List<GrantedAuthority> authorities) {
        return new User(appUser.getUsername(), appUser.getPassword(), appUser.isEnabled(), true, true, true, authorities);
    }

    private List<GrantedAuthority> buildUserAuthority(Set<UserRole> userRoles) {
        Set<GrantedAuthority> setAuths = new HashSet<GrantedAuthority>();
        // Build user's authorities
        for (UserRole userRole : userRoles) {
            setAuths.add(new SimpleGrantedAuthority(userRole.getRole()));
        }
        List<GrantedAuthority> Result = new ArrayList<GrantedAuthority>(setAuths);
        return Result;
    }



}
