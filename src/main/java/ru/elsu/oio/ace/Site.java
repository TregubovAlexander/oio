package ru.elsu.oio.ace;

import lombok.Data;

@Data
public class Site {
    private String title;
    private String brand_icon;
    private String brand_text;
    private String brand_link;
    private boolean ajax;
}
