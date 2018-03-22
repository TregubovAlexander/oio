package ru.elsu.oio.ace;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Message {
    private String name;
    private String img;
    private String time;
    private String summary;
}
