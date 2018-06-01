package ru.elsu.oio.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.elsu.oio.validator.NotNullNotBlank;
import ru.elsu.oio.validator.ValidDate;

import javax.validation.constraints.NotNull;

@Getter @Setter
@NoArgsConstructor
public class TabelSpDaysDto {
    private Long id;
    @NotNull
    private Long personId;
    private String fullName;
    @NotNullNotBlank
    @ValidDate
    private String dateBegin;
    @NotNullNotBlank
    @ValidDate
    private String dateEnd;
    @NotNull
    private Long kodId;
    private String kod;
    private String kodName;
}
