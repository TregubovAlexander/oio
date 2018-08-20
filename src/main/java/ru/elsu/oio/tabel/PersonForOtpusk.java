package ru.elsu.oio.tabel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter @Setter
@NoArgsConstructor
public class PersonForOtpusk {
    private Long id;                        // Идентификатор особенного дня
    private Long personId;                  // Идентификатор сотрудника
    private Long kodId;                     // Код отпуска (здесь будет неизменным)
    private String fullName;                // Фамилия Имя Отчество
    private String tabNo;                   // Табельный номер
    private String dolName;                 // Название должности
    private Date dateBegin;                 // Дата начала отпуска
    private Date dateEnd;                   // Дата окончания отпуска
//    private int dayCount;                   // Количество календарных дней отпуска
}
