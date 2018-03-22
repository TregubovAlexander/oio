package ru.elsu.oio.users;

import lombok.Getter;
import lombok.Setter;
import ru.elsu.oio.entity.Person;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter @Setter
public class AppUser {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private boolean enabled;
    private Long personId;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "appUser")
    private Set<UserRole> userRole = new HashSet<UserRole>(0);

}
