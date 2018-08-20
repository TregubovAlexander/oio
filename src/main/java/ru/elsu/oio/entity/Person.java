package ru.elsu.oio.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import ru.elsu.oio.dto.ChildrenDto;
import ru.elsu.oio.dto.PersonDto;
import ru.elsu.oio.dto.PostDto;
import ru.elsu.oio.utils.Util;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "persons")
@Getter @Setter
@NamedQueries({
        @NamedQuery(name = "Person.getAll",
                query = "SELECT p FROM Person p WHERE p.uvolen=:uvolen ORDER BY p.surname"),

        @NamedQuery(name = "Person.getForTabel",
                query = "SELECT DISTINCT p FROM Person p left join p.postList AS post " +
                        "WHERE (post.dateBegin <= :date2) AND (post.dateEnd is null OR post.dateEnd >= :date1) " +
                        "ORDER BY p.surname"),

        @NamedQuery(name = "Person.getByFioDrGender",
                query = "SELECT p FROM Person p WHERE p.uvolen=:uvolen AND p.surname  LIKE :surname AND p.name LIKE :name " +
                        "AND p.patronymic LIKE :patronymic AND p.dr=:dr AND p.gender=:gender ORDER BY p.surname"),

/*        @NamedQuery(name = "Person.getAllShtat",
                query = "SELECT p FROM Person p WHERE p.shtat = :shtat"),

        @NamedQuery(name = "Person.getByPost",
                query = "SELECT p FROM Person p LEFT JOIN p.postList pt WHERE pt.dol = :dol AND (pt.date = (SELECT MAX(pt2.date) FROM Post pt2 WHERE pt2.person = p))"),

        @NamedQuery(name = "Person.getByPodr",
                query = "SELECT p FROM Person p WHERE p.podr = :podr"),

        @NamedQuery(name = "Person.getAllDr1",
                query = "SELECT p FROM Person p WHERE (p.dr = :date)"),

        @NamedQuery(name = "Person.getAllDr2",
                query = "SELECT p FROM Person p WHERE (p.dr >= :date1) AND (p.dr <= :date2)"),

        @NamedQuery(name = "Person.getAllDrYear1",
                query = "SELECT p FROM Person p WHERE (YEAR(p.dr) = :year)"),

        @NamedQuery(name = "Person.getAllDrYear2",
                query = "SELECT p FROM Person p WHERE (YEAR(p.dr) >= :year1) AND (YEAR(p.dr) <= :year2)"),
*/
        @NamedQuery(name = "Person.getBySurname",
                query = "SELECT p FROM Person p WHERE p.uvolen=:uvolen AND p.surname  LIKE '%'||:surname||'%' ORDER BY p.surname")

})
public class Person {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String surname;                     // Фамилия
    private String name;                        // Имя
    private String patronymic;                  // Отчество
    @Temporal(TemporalType.DATE)
    private Date dr;                            // Дата рождения
    private String gender;                      // Пол
    private String homePhone;                   // Телефон домашний
    private String workPhone;                   // Телефон рабочий
    private String mobilePhone;                 // Телефон мобильный
    private String email;                       // Адрес электронной почты
    private Boolean semPol;                     // Семейное положение
    @Temporal(TemporalType.DATE)
    private Date datPrin;                       // Дата принятия
    private String tabNo;                       // Табельный номер
    private String dopsved;                     // Дополнительные сведения
    private Boolean uvolen;                     // Уволен (true) или Работает (false)
    @Temporal(TemporalType.DATE)
    private Date datUvol;                       // Дата увольнения

    // --- Связываем с другими таблицами -------------------------------------------------------------------------------

    @PrimaryKeyJoinColumn
    @OneToOne(mappedBy = "person", fetch = FetchType.EAGER)
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private Address address;

    @PrimaryKeyJoinColumn
    @OneToOne(mappedBy = "person", fetch = FetchType.EAGER)
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private Pasport pasport;

    @OneToMany(mappedBy = "person", fetch = FetchType.EAGER)
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private List<Post> postList;

    @ManyToMany(fetch = FetchType.LAZY)
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    @JoinTable(name = "personChildren",
            joinColumns = @JoinColumn(name = "personId"),
            inverseJoinColumns = @JoinColumn(name = "childrenId"))
    private List<Children> childrenList;                                // Список детей сотрудника


    // Получаем ФИО
    public String getFullName(){
        return surname + " " + name + " " + patronymic;
    }

    // Получаем основную должность (пока сделал основной ту, у которой больше число ставок, или первую из списка)
    public Post getMainPost() {
        Post result = this.postList.get(0);

        // Выбираем подходящую должность
        for (Post p : this.postList) {
            if (p.getStavka() > result.getStavka()) result = p;
        }
        return result;
    }


    // Перегнать в DTO
    public PersonDto toDto() {
        PersonDto dto = new PersonDto();
        dto.setId(this.id);
        dto.setSurname(this.surname);
        dto.setName(this.name);
        dto.setPatronymic(this.patronymic);
        dto.setFullName(this.getFullName());
        dto.setDr(Util.dateToStr(this.dr));
        dto.setGender(this.gender);
        dto.setAddress(this.address.toDto());
        dto.setPasport(this.pasport.toDto());
        dto.setHomePhone(this.homePhone);
        dto.setWorkPhone(this.workPhone);
        dto.setMobilePhone(this.mobilePhone);
        dto.setEmail(this.email);
        dto.setSemPol(this.semPol);
        List<PostDto> posts = new ArrayList<>();
        for (Post p : this.postList) {
            posts.add(p.toDto());
        }
        dto.setPosts(posts);
        dto.setDatPrin(Util.dateToStr(this.datPrin));
        dto.setTabNo(this.tabNo);
        dto.setDopsved(this.dopsved);
        List<ChildrenDto> childrens = new ArrayList<>();
        for (Children c : this.childrenList) {
            childrens.add(c.toDto());
        }
        dto.setChildrens(childrens);
        dto.setUvolen(this.uvolen);
        dto.setDatUvol(Util.dateToStr(this.datUvol));
        return dto;
    }
    
    
    
}
