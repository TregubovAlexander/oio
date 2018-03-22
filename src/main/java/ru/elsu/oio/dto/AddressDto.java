package ru.elsu.oio.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.elsu.oio.entity.Address;

@Getter @Setter
@NoArgsConstructor
public class AddressDto {
    private String streetId;
    private String zip;
    private String region;
    private String district;
    private String city;
    private String street;
    private String building;
    private String kvartira;
    private String text;

    public AddressDto(Address address){
        if (address == null) return;
        this.streetId = address.getStreetId();
        this.zip = address.getZip();
        this.region = address.getRegion();
        this.district = address.getDistrict();
        this.city = address.getCity();
        this.street = address.getStreet();
        this.building = address.getBuilding();
        this.kvartira = address.getKvartira();
        this.text = address.getText();
    }

}
