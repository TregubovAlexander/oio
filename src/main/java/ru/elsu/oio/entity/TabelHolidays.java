package ru.elsu.oio.entity;

import lombok.Getter;
import lombok.Setter;
import ru.elsu.oio.dto.TabelHolidaysDto;
import ru.elsu.oio.utils.Util;

import javax.persistence.*;

@Entity
@Table(name = "tabelHolidays")
@NamedQueries({
        @NamedQuery(name = "TabelHolidays.getByYear",
                query = "SELECT h FROM TabelHolidays h WHERE (h.year = 0 OR h.year = :year)"),
        @NamedQuery(name = "TabelHolidays.getByYearMonth",
                query = "SELECT h FROM TabelHolidays h WHERE (h.year = 0 OR h.year = :year) AND h.month = :month"),
        @NamedQuery(name = "TabelHolidays.getByYearMonthDay",
                query = "SELECT h FROM TabelHolidays h WHERE (h.year = 0 OR h.year = :year) AND h.month = :month AND h.day = :day")
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
    private boolean holiday;
    private String name;



    // Перегнать в DTO
    public TabelHolidaysDto toDto() {
        TabelHolidaysDto dto = new TabelHolidaysDto();
        dto.setId(this.id);
        dto.setDate(Util.dateToStr(this.year, this.month, this.day));
        dto.setHoliday(this.holiday);
        dto.setPeriodic(this.year == 0);
        dto.setName(this.name);
        return dto;
    }

}
