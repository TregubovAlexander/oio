package ru.elsu.oio.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.elsu.oio.entity.Children;
import ru.elsu.oio.utils.Util;

@Getter @Setter
@NoArgsConstructor
public class ChildrenDto {
    private Long id;
    private String surname;
    private String name;
    private String patronymic;
    private String fullName;
    private String dr;
    private String gender;
    private String birthSertificate;

    public ChildrenDto(Children children){
        if (children == null) return;
        this.id = children.getId();
        this.surname = children.getSurname();
        this.name = children.getName();
        this.patronymic = children.getPatronymic();
        this.fullName = children.getFullName();
        this.dr = Util.dateToStr(children.getDr());
        this.gender = children.getGender();
        this.birthSertificate = children.getBirthSertificate();
    }

}
