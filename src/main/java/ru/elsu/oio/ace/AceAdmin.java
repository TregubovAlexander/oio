package ru.elsu.oio.ace;

import lombok.Data;

import java.util.ArrayList;
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
    private List<SidebarItem> sidebar;


    public void setPageInfo(String title, String description){
        this.page.setTitle(title);
        this.page.setDescription(description);
    }



    // Устанавливает в true свойство active пункта меню с именем itemName и всех его родительских меню в дереве,
    // сбрасывая при этом все остальные пункты
    public void setActiveSidebarItem(String itemName){
        List<SidebarItem> path = new ArrayList<SidebarItem>(); // Нужен для поиска полного пути в дереве меню
        findItem(this.sidebar, itemName, path);

    }

    // Рекурсивный обход дерева меню в поисках нужного пункта, одновременное построение пути
    private void findItem(List<SidebarItem> sidebar, String itemName,  List<SidebarItem> path){
        int i;
        for (i = 0; i < sidebar.size(); i++) {
            SidebarItem item = sidebar.get(i);
            path.add(item);
            item.setItem_class("");
            if (item.getSubmenu() == null) { // Нашли лист
                if (item.getName().equals(itemName)){ // Нашли то, что искали
                    // Делаем активными первый и последний элементы
                    path.get(0).setItem_class("active open");
                    path.get(path.size() - 1).setItem_class("active");
                    for (int j = 1; j < path.size() - 1; j++) {
                        path.get(j).setItem_class("open");
                    }
                }
            } else { // Нашли ветку
                findItem(item.getSubmenu(), itemName, path);
            }
            path.remove(path.size() - 1); // Удаляем ненужный лист из найденного пути (path)
        }
    }


}
