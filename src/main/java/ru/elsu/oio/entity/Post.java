package ru.elsu.oio.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "posts")
@Getter @Setter
public class Post {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "personId")
    private Person person;                     // Ссылка на сотрудника для связи

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "dolId")
    private SprDol dol;                     // Должность (из справочника)

    @Temporal(TemporalType.DATE)            // Отобразим тип данных из Java-даты (java.util.Date) на SQL-тип даты (java.sql.Date)
    private Date dateBegin;                 // Дата назначения на должность
    @Temporal(TemporalType.DATE)
    private Date dateEnd;                   // Дата завершения должность
    private Float stavka;                   // Число ставок
    private Boolean active;                 // Активная или нет

}
