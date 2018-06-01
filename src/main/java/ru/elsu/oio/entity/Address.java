package ru.elsu.oio.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import ru.elsu.oio.dto.AddressDto;

import javax.persistence.*;

@Entity
@Table(name = "address")
@Getter @Setter
@GenericGenerator(name = "foreign", strategy = "foreign", parameters = {
        @org.hibernate.annotations.Parameter(name = "property", value = "person")
})
public class Address {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "personId")
    @GeneratedValue(generator = "foreign")
    private Long id;

    @PrimaryKeyJoinColumn
    @OneToOne(fetch = FetchType.LAZY) // ???? Я не буду получать список адресов, поэтому EAGER
    private Person person;

    private String streetId;
    private String zip;
    private String region;
    private String district;
    private String city;
    private String street;
    private String building;
    private String kvartira;
    private String text;


    // Перегнать в DTO
    public AddressDto toDto() {
        AddressDto dto = new AddressDto();
        dto.setStreet(this.streetId);
        dto.setZip(this.zip);
        dto.setRegion(this.region);
        dto.setDistrict(this.district);
        dto.setCity(this.city);
        dto.setStreet(this.street);
        dto.setBuilding(this.building);
        dto.setKvartira(this.kvartira);
        dto.setText(this.text);
        return dto;
    }
}
