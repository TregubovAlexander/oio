package ru.elsu.oio.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.elsu.oio.entity.Post;
import ru.elsu.oio.utils.Util;
import ru.elsu.oio.validator.NotNullNotBlank;
import ru.elsu.oio.validator.Stavka;
import ru.elsu.oio.validator.ValidDate;

import javax.validation.constraints.NotNull;

@Getter @Setter
@NoArgsConstructor
public class PostDto {

    private Long id;
    private Long dolId;
    private String dolName;
    @NotNullNotBlank
    @ValidDate
    private String dateBegin;
    @ValidDate
    private String dateEnd;
    @Stavka
    private Float stavka;
    @NotNull
    private Boolean active;

}
