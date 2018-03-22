package ru.elsu.oio.ace;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Notification {
    private String title;
    private String icon;
    private String icon_class;
    private String badge;
    private String badge_class;
}

