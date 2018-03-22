package ru.elsu.oio.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "tabelSpecialDays")
@NamedQueries({
        @NamedQuery(name = "TabelSpecialDays.get",
                query = "SELECT d FROM TabelSpecialDays d WHERE (:date1 <= d.dateEnd) AND (d.dateBegin <= :date2)")
})
@Getter @Setter
public class TabelSpecialDays {
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
}
