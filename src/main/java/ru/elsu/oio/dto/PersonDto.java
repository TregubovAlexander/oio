package ru.elsu.oio.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.elsu.oio.entity.Children;
import ru.elsu.oio.entity.Person;
import ru.elsu.oio.entity.Post;
import ru.elsu.oio.utils.Util;
import ru.elsu.oio.validator.ValidDate;


import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;


@Getter @Setter
@NoArgsConstructor
public class PersonDto {
    private Long id;

    @NotNull
    @Size(min = 2, max = 30, message = "{Size.personDto.name}")
    @Pattern(regexp = "^[а-яё]{2,30}([-|\\s]?[а-яё]{2,30})?$", flags = {Pattern.Flag.CASE_INSENSITIVE, Pattern.Flag.UNICODE_CASE})
    private String surname;
    @NotNull
    @Size(min = 2, max = 30)
    @Pattern(regexp = "^[а-яё]{2,30}$", flags = {Pattern.Flag.CASE_INSENSITIVE, Pattern.Flag.UNICODE_CASE})
    private String name;
    @NotNull
    @Size(min = 2, max = 30, message = "{Size.personDto.name}")
    @Pattern(regexp = "^[а-яё]{2,30}$", flags = {Pattern.Flag.CASE_INSENSITIVE, Pattern.Flag.UNICODE_CASE}, message = "{Pattern.personDto.name}")
    private String patronymic;
    private String fullName;
    @NotNull
    @ValidDate
    private String dr;
    @NotNull
    @Pattern(regexp = "^[fm]$", flags = {Pattern.Flag.CASE_INSENSITIVE})
    private String gender;
    private AddressDto address;
    private PasportDto pasport;
    @Pattern(regexp = "^([0-9]{5,10})|(\\s*)$")
    private String homePhone;
    @Pattern(regexp = "^([0-9]{5,10})|(\\s*)$", message = "{Pattern.personDto.homePhone}")
    private String workPhone;
    @Pattern(regexp = "^([0-9]{10})|(\\s*)$")
    private String mobilePhone;
    @Pattern(regexp = "^([_a-z0-9-\\+]+(\\.[_a-z0-9-]+)*@[a-z0-9-]+(\\.[a-z0-9]+)*(\\.[a-z]{2,}))|(\\s*)$", flags = {Pattern.Flag.CASE_INSENSITIVE, Pattern.Flag.UNICODE_CASE})
    private String email;
    private Boolean semPol;
    private List<PostDto> posts;
    @ValidDate
    private String datPrin;
    private String tabNo;
    private String dopsved;
    private List<ChildrenDto> childrens;
    private Boolean uvolen;
    private String datUvol;


    // Конструктор из Person
    public PersonDto(Person person) {
        if (person == null) return;
        this.id = person.getId();
        this.surname = person.getSurname();
        this.name = person.getName();
        this.patronymic = person.getPatronymic();
        this.fullName = person.getFullName();
        this.dr = Util.dateToStr(person.getDr());
        this.gender = person.getGender();
        this.address = new AddressDto(person.getAddress());
        this.pasport = new PasportDto(person.getPasport());
        this.homePhone = person.getHomePhone();
        this.workPhone = person.getWorkPhone();
        this.mobilePhone = person.getMobilePhone();
        this.email = person.getEmail();
        this.semPol = person.getSemPol();
        this.posts = new ArrayList<>();
        List<Post> postList = person.getPostList();
        if (postList != null) {
            for (Post p : postList) {
                posts.add(new PostDto(p));
            }
        }
        this.datPrin = Util.dateToStr(person.getDatPrin());
        this.tabNo = person.getTabNo();
        this.dopsved = person.getDopsved();
        this.childrens = new ArrayList<>();
        List<Children> childrenList = person.getChildrenList();
        if (childrenList != null) {
            for (Children c : childrenList) {
                childrens.add(new ChildrenDto(c));
            }
        }
        this.uvolen = person.getUvolen();
        this.datUvol = Util.dateToStr(person.getDatUvol());
    }

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
