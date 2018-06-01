package ru.elsu.oio.entity;

import lombok.Getter;
import lombok.Setter;
import ru.elsu.oio.dao.PostDao;
import ru.elsu.oio.dto.PostDto;
import ru.elsu.oio.tabel.Tabel;
import ru.elsu.oio.utils.Util;

import javax.persistence.*;
import java.util.Calendar;
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

    /**
     * Проверяет нужно ли включать данную должность сотрудника в табель.
     * Включать нужно, если должность активная или если дата начала или дата окончания должности попадают в табель.
     * @param year  год за который делается табель
     * @param month месяц за который делается табель
     * @return true - нужно включать, false - не нужно
     */
    public boolean forTabel(int year, int month) {
        Date d1 = Tabel.getFirstDate(year, month);
        Date d2 = Tabel.getLastDate(year, month);
        return dateBegin.compareTo(d2) <= 0 & (dateEnd == null || dateEnd.compareTo(d1) >= 0);
    }


    // Перегнать в DTO
    public PostDto toDto() {
        PostDto dto = new PostDto();
        dto.setId(this.id);
        dto.setDolId(this.dol.getId());
        dto.setDolName(this.dol.getName());
        dto.setDateBegin(Util.dateToStr(this.dateBegin));
        dto.setDateEnd(Util.dateToStr(this.dateEnd));
        dto.setStavka(this.stavka);
        dto.setActive(this.active);
        return dto;
    }

}
