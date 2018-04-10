package ru.elsu.oio.tabel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter @Setter
@AllArgsConstructor
public class PersonForTabel {
    private Long id;                        // Идентификатор
    private String surname;                 // Фамилия
    private String name;                    // Имя
    private String patronymic;              // Отчество
    private Float  stavka;                  // Число ставок
    private String tabNo;                   // Табельный номер
    private String dolName;                 // Название должности
    private Boolean dolActive;              // Активна ли должность
    private Date dolDateBegin;              // Дата назначения на должность
    private Date dolDateEnd;                // Дата окончания должности
}
