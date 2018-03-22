package ru.elsu.oio.model;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter @Setter
public class PersonAddForm {
    @NotEmpty
    private String surname;
    @NotEmpty
    private String name;
    @NotEmpty
    private String patronymic;
    @NotEmpty
    private String dr;
    @NotEmpty
    private String gender;

}
