package ru.elsu.oio.entity;

import lombok.Getter;
import lombok.Setter;
import ru.elsu.oio.dto.TabelSpDaysDto;
import ru.elsu.oio.utils.Util;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "tabelSpecialDays")
@NamedQueries({
        @NamedQuery(name = "TabelSpDays.get",
                query = "SELECT d FROM TabelSpDays d WHERE (:date1 <= d.dateEnd) AND (d.dateBegin <= :date2)"),
        @NamedQuery(name = "TabelSpDays.getByPerson",
                query = "SELECT d FROM TabelSpDays d WHERE (d.person = :person) AND (:date1 <= d.dateEnd) AND (d.dateBegin <= :date2)")
})
@Getter @Setter
public class TabelSpDays {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "personId")
    private Person person;              // Ссылка на сотрудника для связи
    @Temporal(TemporalType.DATE)
    private Date dateBegin;             // Дата начала особенного диапазона дней в тебеле
    @Temporal(TemporalType.DATE)
    private Date dateEnd;               // Дата окончания особенного диапазона дней в табеле
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "kod")
    private SprTabelNotation kod;       // Код особенного дня - Запись в справочнике SprTabelNotation


    // Перегнать в DTO
    public TabelSpDaysDto toDto() {
        TabelSpDaysDto dto = new TabelSpDaysDto();
        dto.setId(this.id);
        dto.setPersonId(this.person.getId());
        dto.setFullName(this.person.getFullName());
        dto.setDateBegin(Util.dateToStr(this.dateBegin));
        dto.setDateEnd(Util.dateToStr(this.dateEnd));
        dto.setKodId(this.kod.getId());
        dto.setKod(this.kod.getKod());
        dto.setKodName(this.kod.getName());
        return dto;
    }



}
