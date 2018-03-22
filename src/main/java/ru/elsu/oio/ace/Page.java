package ru.elsu.oio.ace;

import lombok.Data;
import java.util.List;

@Data
public class Page {
    private String title;
    private String description;
    private boolean no_breadcrumbs;
    private boolean no_header;
    private List<String> styles;
    private String inline_style;
    private List<String> scripts;
    private Boolean inline_scripts;
    private boolean user_menu_collapsible;
    private String top_menu;                    // Имя вида (html) в котором приписано меню, или пустая строка если меню не нужно
    private boolean horizontal_menu;
    private boolean side_menu_collapsible;
    private boolean responsive_style_2;
    private boolean or_use_an_icon_instead;
}
