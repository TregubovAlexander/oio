package ru.elsu.oio.entity;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "children")
@Getter @Setter
@NamedQueries({
        @NamedQuery(name = "Children.getAll",
                query = "SELECT c FROM Children c ORDER BY c.surname"),
        @NamedQuery(name = "Children.getBySurname",
                query = "SELECT c FROM Children c WHERE c.surname  LIKE :surname||'%'"),
        @NamedQuery(name = "Children.getBySurnameNameDrGender",
                query = "SELECT c FROM Children c WHERE c.surname  LIKE :surname AND c.name LIKE :name AND c.dr=:dr AND c.gender=:gender")

//        @NamedQuery(name = "Children.getAllDr1",
//                query = "SELECT c FROM Children c WHERE (c.dr = :date)"),
//        @NamedQuery(name = "Children.getAllDr2",
//                query = "SELECT c FROM Children c WHERE (c.dr >= :date1) AND (c.dr <= :date2)"),
//        @NamedQuery(name = "Children.getAllDrYear1",
//                query = "SELECT c FROM Children c WHERE (YEAR(c.dr) = :year)"),
//        @NamedQuery(name = "Children.getAllDrYear2",
//                query = "SELECT c FROM Children c WHERE (YEAR(c.dr) >= :year1) AND (YEAR(c.dr) <= :year2)")
})

public class Children {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                            // Идентификатор ребенка

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "personChildren",
            joinColumns = @JoinColumn(name = "childrenId"),
            inverseJoinColumns = @JoinColumn(name = "personId"))
    private List<Person> personList;            // Список детей сотрудника

    private String surname;                     // Фамилия
    private String name;                        // Имя
    private String patronymic;                  // Отчество
    @Temporal(TemporalType.DATE)
    private Date dr;                            // Дата рождения
    private String gender;                      // Пол
    private String birthSertificate;            // Свидетельство о рождении


    // Получаем ФИО
    public String getFullName(){
        return surname + " " + name + " " + patronymic;
    }

}
