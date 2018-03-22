package ru.elsu.oio.users;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;

@Entity
@Table(name = "userRoles", uniqueConstraints = @UniqueConstraint(columnNames = { "role", "userId" }))
@Getter @Setter
public class UserRole {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private AppUser appUser;                      // Ссылка на пользователя

    @Column(name = "role")
    private String role;

}
