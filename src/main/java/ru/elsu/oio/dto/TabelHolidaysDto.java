package ru.elsu.oio.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.elsu.oio.validator.NotNullNotBlank;
import ru.elsu.oio.validator.ValidDate;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter @Setter
@NoArgsConstructor
public class TabelHolidaysDto {
    private Long id;
    @NotNullNotBlank
    @ValidDate
    private String date;
    private boolean holiday;
    private boolean periodic;
    @Size(min = 2, max = 100, message = "{validation.stringSize.message}")
    private String name;
}
