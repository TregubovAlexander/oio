package ru.elsu.oio.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.elsu.oio.entity.Children;
import ru.elsu.oio.entity.Person;
import ru.elsu.oio.entity.Post;
import ru.elsu.oio.utils.Util;
import ru.elsu.oio.validator.NotNullNotBlank;
import ru.elsu.oio.validator.ValidDate;


import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;


@Getter @Setter
@NoArgsConstructor
public class PersonDto {
    private Long id;

    @NotNull
    @Size(min = 2, max = 30, message = "{validation.stringSize.message}")
    @Pattern(regexp = "^[а-яё]{2,30}([-|\\s]?[а-яё]{2,30})?$", flags = {Pattern.Flag.CASE_INSENSITIVE, Pattern.Flag.UNICODE_CASE})
    private String surname;
    @NotNull
    @Size(min = 2, max = 30, message = "{validation.stringSize.message}")
    @Pattern(regexp = "^[а-яё]{2,30}$", flags = {Pattern.Flag.CASE_INSENSITIVE, Pattern.Flag.UNICODE_CASE})
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
    @Pattern(regexp = "^[fm]$", flags = {Pattern.Flag.CASE_INSENSITIVE})
    private String gender;
    private @Valid AddressDto address;
    private @Valid PasportDto pasport;
    @Pattern(regexp = "^([0-9]{5,10})|(\\s*)$")
    private String homePhone;
    @Pattern(regexp = "^([0-9]{5,10})|(\\s*)$", message = "{Pattern.personDto.homePhone}")
    private String workPhone;
    @Pattern(regexp = "^([0-9]{10})|(\\s*)$")
    private String mobilePhone;
    @Pattern(regexp = "^([_a-z0-9-\\+]+(\\.[_a-z0-9-]+)*@[a-z0-9-]+(\\.[a-z0-9]+)*(\\.[a-z]{2,}))|(\\s*)$", flags = {Pattern.Flag.CASE_INSENSITIVE, Pattern.Flag.UNICODE_CASE})
    private String email;
    private Boolean semPol;
    private @Valid List<PostDto> posts;
    @ValidDate
    private String datPrin;
    @Pattern(regexp = "^([0-9]{1,10})|(\\s*)$")
    private String tabNo;
    private String dopsved;
    private @Valid List<ChildrenDto> childrens;
    private Boolean uvolen;
    @ValidDate
    private String datUvol;


    // Создание сотрудника Person из полученного JSON через REST
    // TODO: Дополнить (возможно)
    public Person toPerson(){
        Person person = new Person();
        person.setSurname(this.surname.trim());
        person.setName(this.name.trim());
        person.setPatronymic(this.patronymic.trim());
        person.setDr(Util.strToDate(this.dr));
        person.setGender(this.gender);
        person.setMobilePhone(this.mobilePhone);
        return person;
    }






}
