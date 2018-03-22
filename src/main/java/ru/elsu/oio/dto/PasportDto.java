package ru.elsu.oio.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.elsu.oio.entity.Pasport;
import ru.elsu.oio.utils.Util;

@Getter @Setter
@NoArgsConstructor
public class PasportDto {
    private Long personId;
    private String ser;
    private String num;
    private String dat;
    private String org;

    // Конструктор из Pasport
    public PasportDto(Pasport pasport) {
        if (pasport == null) return;
        this.personId = pasport.getId();
        this.ser = pasport.getSer();
        this.num = pasport.getNum();
        this.dat = Util.dateToStr(pasport.getDat());
        this.org = pasport.getOrg();
    }
}
