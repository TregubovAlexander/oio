package ru.elsu.oio.ace;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Breadcrumb {
    private String title;
    private String link;
}
