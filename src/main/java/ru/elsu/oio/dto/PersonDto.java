package ru.elsu.oio.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.elsu.oio.entity.Children;
import ru.elsu.oio.entity.Person;
import ru.elsu.oio.entity.Post;
import ru.elsu.oio.utils.Util;
import java.util.ArrayList;
import java.util.List;


@Getter @Setter
@NoArgsConstructor
public class PersonDto {
    private Long id;
    private String surname;
    private String name;
    private String patronymic;
    private String fullName;
    private String dr;
    private String gender;
    private AddressDto address;
    private PasportDto pasport;
    private String homePhone;
    private String workPhone;
    private String mobilePhone;
    private String email;
    private Boolean semPol;
    private List<PostDto> posts;
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
