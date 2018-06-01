package ru.elsu.oio.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.elsu.oio.entity.Pasport;
import ru.elsu.oio.utils.Util;
import ru.elsu.oio.validator.NotNullNotBlank;
import ru.elsu.oio.validator.ValidDate;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter @Setter
@NoArgsConstructor
public class PasportDto {
    private Long personId;
    @NotNull
    @Pattern(regexp = "^[0-9]{4}$")
    private String ser;
    @NotNull
    @Pattern(regexp = "^[0-9]{6}$")
    private String num;
    @NotNullNotBlank
    @ValidDate
    private String dat;
    @NotNullNotBlank
    private String org;
}
