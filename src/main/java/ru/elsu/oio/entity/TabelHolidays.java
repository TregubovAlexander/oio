package ru.elsu.oio.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "tabelHolidays")
@NamedQueries({
        @NamedQuery(name = "TabelHolidays.get",
                query = "SELECT h FROM TabelHolidays h WHERE (h.year = 0 OR h.year = :year) AND h.month = :month")
})
@Getter @Setter
public class TabelHolidays {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int year;
    private int month;
    private int day;
    private boolean isHoliday;

}
