package ru.elsu.oio.entity;

import lombok.Getter;
import lombok.Setter;
import ru.elsu.oio.validator.NotNullNotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "sprTabelNotation")
@NamedQueries({
        @NamedQuery(name = "SprTabelNotation.getAll",
                query = "SELECT s FROM SprTabelNotation s ORDER BY s.id"),
        @NamedQuery(name = "SprTabelNotation.getAllActive",
                query = "SELECT s FROM SprTabelNotation s WHERE s.active = true"),
        @NamedQuery(name = "SprTabelNotation.getByKod",
                query = "SELECT s FROM SprTabelNotation s WHERE s.kod = :kod")
})
@Getter @Setter
public class SprTabelNotation {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private Boolean active;
    @NotNull
    private Boolean workDay;
    @NotNull
    private short color;
    @NotNullNotBlank
    @Size(min = 1, max = 2, message = "{validation.stringSize.message}")
    private String kod;
    @NotNullNotBlank
    @Size(min = 2, max = 200, message = "{validation.stringSize.message}")
    private String name;

}
