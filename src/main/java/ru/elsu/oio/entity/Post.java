package ru.elsu.oio.entity;

import lombok.Getter;
import lombok.Setter;

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

        if (this.getActive()) return true;

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(this.getDateBegin());
        int yearBegin = calendar.get(Calendar.YEAR);
        int monthBegin = calendar.get(Calendar.MONTH) + 1;
        calendar.setTime(this.getDateEnd());
        int yearEnd = calendar.get(Calendar.YEAR);
        int monthEnd = calendar.get(Calendar.MONTH) + 1;

        if ( ((yearBegin == year) && (monthBegin == month)) || ((yearEnd == year) && (monthEnd == month)) ) return true;

        return false;
    }

}
