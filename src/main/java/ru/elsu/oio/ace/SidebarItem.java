package ru.elsu.oio.ace;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter @Setter
public class SidebarItem {
    private String link;
    private String title;
    private String icon;
    private Boolean active;
    private String badge;
    private String badge_class;
    private String tooltip;
    private String tooltip_class;
    private String label;
    private String label_class;
    private String label_title;
    private String item_class;
    private List<SidebarItem> submenu;

    public SidebarItem(String title) {
        this.title = title;
    }
    public SidebarItem(String title, List<SidebarItem> submenu) {
        this.title = title;
        this.submenu = submenu;
    }
    public SidebarItem(String title, String icon, List<SidebarItem> submenu) {
        this(title,submenu);
        this.icon = icon;
    }
    public SidebarItem(String link, String title) {
        this.link = link;
        this.title = title;
    }
    public SidebarItem(String link, String title, String icon) {
        this(link,title);
        this.icon = icon;
    }
    public SidebarItem(String link, String title, String icon, String badge, String badge_class, String tooltip, String tooltip_class) {
        this(link, title, icon);
        this.badge = badge;
        this.badge_class = badge_class;
        this.tooltip = tooltip;
        this.tooltip_class = tooltip_class;
    }
    public SidebarItem(String link, String title, String icon, String badge, String badge_class, String tooltip, String tooltip_class, String item_class, List<SidebarItem> submenu) {
        this.link = link;
        this.title = title;
        this.icon = icon;
        this.badge = badge;
        this.badge_class = badge_class;
        this.tooltip = tooltip;
        this.tooltip_class = tooltip_class;
        this.item_class  = item_class;
        this.submenu = submenu;
    }

}
