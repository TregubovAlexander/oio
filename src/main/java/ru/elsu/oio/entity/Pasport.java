package ru.elsu.oio.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "pasport")
@Getter @Setter
@GenericGenerator(name = "foreign", strategy = "foreign", parameters = {
        @org.hibernate.annotations.Parameter(name = "property", value = "person")
})
public class Pasport {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "personId")
    @GeneratedValue(generator = "foreign")
    private Long id;

    @PrimaryKeyJoinColumn
    @OneToOne(fetch = FetchType.LAZY)
    private Person person;                                      // Ссылка на сотрудника

    private String ser;
    private String num;
    @Temporal(TemporalType.DATE)
    private Date dat;
    private String org;

}
