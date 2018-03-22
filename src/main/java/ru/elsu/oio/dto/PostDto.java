package ru.elsu.oio.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.elsu.oio.entity.Post;
import ru.elsu.oio.utils.Util;

@Getter @Setter
@NoArgsConstructor
public class PostDto {

    private Long id;
    private Long dolId;
    private String dolName;
    private String dateBegin;
    private String dateEnd;
    private Float stavka;
    private Boolean active;

    public PostDto(Post post) {
        if (post == null) return;
        this.id = post.getId();
        this.dolId = post.getDol().getId();
        this.dolName = post.getDol().getName();
        this.dateBegin = Util.dateToStr(post.getDateBegin());
        this.dateEnd = Util.dateToStr(post.getDateEnd());
        this.stavka = post.getStavka();
        this.active = post.getActive();
    }



}
