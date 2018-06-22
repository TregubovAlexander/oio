package ru.elsu.oio.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.elsu.oio.entity.Children;
import ru.elsu.oio.utils.Util;
import ru.elsu.oio.validator.NotNullNotBlank;
import ru.elsu.oio.validator.ValidDate;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter @Setter
@NoArgsConstructor
public class ChildrenDto {

    private Long id;
    @NotNull
    @Size(min = 2, max = 30, message = "{validation.stringSize.message}")
    @Pattern(regexp = "^([а-яё]{2,30}([-|\\s]?[а-яё]{2,30})?)|(delete)$", flags = {Pattern.Flag.CASE_INSENSITIVE, Pattern.Flag.UNICODE_CASE}, message = "{Pattern.personDto.surname}")
    private String surname;
    @NotNull
    @Size(min = 2, max = 30, message = "{validation.stringSize.message}")
    @Pattern(regexp = "^[а-яё]{2,30}$", flags = {Pattern.Flag.CASE_INSENSITIVE, Pattern.Flag.UNICODE_CASE}, message = "{Pattern.personDto.name}")
    private String name;
    @NotNull
    @Size(min = 2, max = 30, message = "{validation.stringSize.message}")
    @Pattern(regexp = "^[а-яё]{2,30}$", flags = {Pattern.Flag.CASE_INSENSITIVE, Pattern.Flag.UNICODE_CASE}, message = "{Pattern.personDto.name}")
    private String patronymic;
    private String fullName;
    @NotNullNotBlank
    @ValidDate
    private String dr;
    @NotNull
    @Pattern(regexp = "^[fm]$", flags = {Pattern.Flag.CASE_INSENSITIVE}, message = "{Pattern.personDto.gender}")
    private String gender;
    @Pattern(regexp = "^(M{0,3}(D?C{0,3}|C[DM])(L?X{0,3}|X[LC])(V?I{0,3}|I[VX])((?!^)-)?[а-я]{2}\\s\\d{6})|(\\s*)$", flags = {Pattern.Flag.CASE_INSENSITIVE, Pattern.Flag.UNICODE_CASE}, message = "{Pattern.childrenDto.birthSertificate}")
    private String birthSertificate;

}
