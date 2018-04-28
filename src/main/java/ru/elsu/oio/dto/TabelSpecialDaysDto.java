package ru.elsu.oio.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.elsu.oio.entity.SprTabelNotation;


@Getter @Setter
@NoArgsConstructor
public class TabelSpecialDaysDto {

    private Long id;

    private Long personId;

    private String fullName;

    private String dateBegin;

    private String dateEnd;

    //private SprTabelNotation kod;

    private Long kodId;

    private String kod;

    private String kodName;




}
