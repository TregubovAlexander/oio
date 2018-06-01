package ru.elsu.oio.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.elsu.oio.entity.Address;
import ru.elsu.oio.validator.NotNullNotBlank;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter @Setter
@NoArgsConstructor
public class AddressDto {
    @NotNull
    @Pattern(regexp = "^[0-9]+$")
    private String streetId;
    @Pattern(regexp = "^([0-9]{6})|(\\s*)$")
    private String zip;
    private String region;
    private String district;
    @NotNullNotBlank
    private String city;
    private String street;
    @NotNullNotBlank
    private String building;
    private String kvartira;
    @NotNullNotBlank
    private String text;

}
