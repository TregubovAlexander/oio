package ru.elsu.oio.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.i18n.LocaleContextHolder;
import ru.elsu.oio.Url;
import ru.elsu.oio.ace.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
public class AceConfig {

    @Autowired
    MessageSource messageSource;
    private String getMessage(String key) {
        return messageSource.getMessage(key, null, LocaleContextHolder.getLocale());
    }
    private String getMessage(String key, Object[] args) {
        return messageSource.getMessage(key, args, LocaleContextHolder.getLocale());
    }

    // Настройки шаблона AceAdmin
    @Bean
    @Scope("prototype")
    public AceAdmin aceAdmin(){
        // Параметры приложения
        AppSettings appSettings = new AppSettings();
        appSettings.setNavbar_messages_limit(5);
        appSettings.setNavbar_tasks_limit(5);
        appSettings.setNavbar_notifications_limit(5);

        // Параметры страницы
        Page page = new Page();
        page.setTitle("");
        page.setDescription("");
        page.setNo_breadcrumbs(false);
        page.setNo_header(false);
        //page.setUser_menu_collapsible(true);
        page.setTop_menu("");                       // Пустая строка означает, что верхнее меню не нужно
        //page.setHorizontal_menu(true);
        page.setSide_menu_collapsible(true);
        page.setResponsive_style_2(true);
        page.setOr_use_an_icon_instead(true);

        // Параметры сайта
        Site site = new Site();
        site.setTitle("Управление информатизации образования и информационной безопасности");
        site.setBrand_text("УИОиИБ");
        site.setBrand_icon("fa fa-area-chart");
        site.setBrand_link(""); // Ссылка на корень WEB-приложения
        site.setAjax(false);

        // Хлебные крошки
        List<Breadcrumb> breadcrumbs = new ArrayList<Breadcrumb>();
        breadcrumbs.add(new Breadcrumb("Ссылка1","link1"));
        breadcrumbs.add(new Breadcrumb("Ссылка2","link2"));
        breadcrumbs.add(new Breadcrumb("Ссылка3","link3"));

        // Задачи
        List<Task> tasks = new ArrayList<Task>();
        tasks.add(new Task("Software Update",65));
        tasks.add(new Task("Hardware Upgrade",35,"progress-bar-danger"));
        tasks.add(new Task("Unit Testing",15,"progress-bar-warning"));
        tasks.add(new Task("Bug Fixes",90,"progress-bar-success","progress-striped active"));

        // Уведомления
        List<Notification> notifications = new ArrayList<Notification>();
        notifications.add(new Notification("New Comments","fa fa-comment","btn-pink","+12","badge-info"));
        notifications.add(new Notification("Bob just signed up as an editor ...","fa fa-user","btn-primary","",""));
        notifications.add(new Notification("New Orders","fa fa-shopping-cart","btn-success","+8","badge-success"));
        notifications.add(new Notification("Followers","fa fa-twitter","btn-info","+11","badge-info"));

        // Сообщения
        List<Message> messages = new ArrayList<Message>();
        messages.add(new Message("Alex", "avatar.png", "a moment ago", "Ciao sociis natoque penatibus et auctor ..."));
        messages.add(new Message("Susan", "avatar3.png", "20 minutes ago", "Vestibulum id ligula porta felis euismod ..."));
        messages.add(new Message("Bob", "avatar4.png", "3:15 pm", "Nullam quis risus eget urna mollis ornare ..."));
        messages.add(new Message("Kate", "avatar2.png", "1:33 pm", "Ciao sociis natoque eget urna mollis ornare ..."));
        messages.add(new Message("Fred", "avatar5.png", "10:09 am", "Vestibulum id penatibus et auctor  ..."));




        // Элементы меню (SideBar)
        List<SidebarItem> sidebarItems = new ArrayList<SidebarItem>();
        sidebarItems.add(new SidebarItem("persons","Сотрудники","fa fa-user"));
        sidebarItems.add(new SidebarItem("Документация", "fa fa-files-o", Arrays.asList(
                new SidebarItem("doc/tabel","Табель"),
                new SidebarItem("doc/grafotpusk","График отпусков")
        )));

        sidebarItems.add(new SidebarItem("Справочники","fa fa-file-text-o", Arrays.asList(
                new SidebarItem("sprav/activitygoal","Цели деятельности ПФХД"),
                new SidebarItem("sprav/activitykind","Виды деятельности ПФХД"),
                new SidebarItem("sprav/servicelist","Перечень услуг (работ), осуществляемых на платной основе"),
                new SidebarItem("sprav/activitykindtype","Виды финансового обеспечения"),
                new SidebarItem("sprav/vktype","Перечень доходов (поступлений) и расходов (выплат)"),
                new SidebarItem("sprav/extravalue","Перечень дополнительных показателей")
        )));
        sidebarItems.add(new SidebarItem("extravalue","Доп. показатели","fa fa-database"));



        // Собираем все в кучу
        AceAdmin aceAdmin = new AceAdmin();
        aceAdmin.setAppSettings(appSettings);
        aceAdmin.setPage(page);
        aceAdmin.setSite(site);
        aceAdmin.setBreadcrumbs(breadcrumbs);
        aceAdmin.setTasks(tasks);
        aceAdmin.setNotifications(notifications);
        aceAdmin.setMessages(messages);
        aceAdmin.setSidebarItems(sidebarItems);

        return aceAdmin;
    }



}
