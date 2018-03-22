package ru.elsu.oio.ace;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Task {
    private String title;
    private int percentage;
    private String progress_class;
    private String progress_bar_class;

    public Task(String title, int percentage){
        this.title = title;
        this.percentage = percentage;
    }
    public Task(String title, int percentage, String progress_bar_class) {
        this(title, percentage);
        this.progress_bar_class = progress_bar_class;
    }
    public Task(String title, int percentage, String progress_bar_class, String progress_class) {
        this(title, percentage, progress_bar_class);
        this.progress_class = progress_class;

    }
}
