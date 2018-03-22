package ru.elsu.oio.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "sprDol")
@NamedQueries({
        @NamedQuery(name = "SprDol.getAll",
                query = "SELECT d FROM SprDol d ORDER BY d.name"),
        @NamedQuery(name = "SprDol.getByName",
                query = "SELECT d FROM SprDol d WHERE lower(d.name) = :name")
})
@Getter @Setter
public class SprDol {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String shortName;

}
