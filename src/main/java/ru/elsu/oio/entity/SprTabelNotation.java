package ru.elsu.oio.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "sprTabelNotation")
@NamedQueries({
        @NamedQuery(name = "SprTabelNotation.getAll",
                query = "SELECT s FROM SprTabelNotation s ORDER BY s.id"),
        @NamedQuery(name = "SprTabelNotation.getAllActive",
                query = "SELECT s FROM SprTabelNotation s WHERE s.active = true")
})
@Getter @Setter
public class SprTabelNotation {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Boolean active;
    private Boolean workDay;
    private short color;
    private String kod;
    private String name;

}
