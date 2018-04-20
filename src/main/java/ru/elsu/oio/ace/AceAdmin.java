package ru.elsu.oio.ace;

import lombok.Data;
import java.util.List;

@Data
public class AceAdmin {
    private AppSettings appSettings;
    private Page page;
    private Site site;
    private List<Breadcrumb> breadcrumbs;
    private List<Task> tasks;
    private List<Notification> notifications;
    private List<Message> messages;
    private List<SidebarItem> sidebarItems;

    public void setPageInfo(String title, String description){
        this.page.setTitle(title);
        this.page.setDescription(description);
    }

}
