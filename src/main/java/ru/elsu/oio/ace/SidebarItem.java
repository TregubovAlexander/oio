package ru.elsu.oio.ace;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter @Setter
public class SidebarItem {
    private String name;  // Уникальное имя пункта меню -
    private String link;
    private String title;
    private String icon;
    private String badge;
    private String badge_class;
    private String tooltip;
    private String tooltip_class;
    private String label;
    private String label_class;
    private String label_title;
    private String item_class;
    private List<SidebarItem> submenu;


    public SidebarItem(String name, String title, List<SidebarItem> submenu) {
        this.name = name;
        this.title = title;
        this.submenu = submenu;
    }
    public SidebarItem(String name, String title, String icon, List<SidebarItem> submenu) {
        this(name, title, submenu);
        this.icon = icon;
    }
    public SidebarItem(String name, String link, String title) {
        this.name = name;
        this.link = link;
        this.title = title;
    }
    public SidebarItem(String name, String link, String title, String icon) {
        this(name, link, title);
        this.icon = icon;
    }
    public SidebarItem(String name, String link, String title, String icon, String badge, String badge_class, String tooltip, String tooltip_class) {
        this(name, link, title, icon);
        this.badge = badge;
        this.badge_class = badge_class;
        this.tooltip = tooltip;
        this.tooltip_class = tooltip_class;
    }
    public SidebarItem(String name, String link, String title, String icon, String badge, String badge_class, String tooltip, String tooltip_class, List<SidebarItem> submenu) {
        this(name, link, title, icon, badge, badge_class, tooltip, tooltip_class);
        this.submenu = submenu;
    }

}
