package ru.elsu.oio.ace;

import lombok.Data;

@Data
public class AppSettings {
    private int navbar_tasks_limit;
    private int navbar_messages_limit;
    private int navbar_notifications_limit;
}

