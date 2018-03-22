package ru.elsu.oio.dto;

import lombok.Getter;
import lombok.Setter;

// Вспомогательный объект с информацией о результате загрузки файла
@Getter @Setter
public class FileUploadResult {
    private String status;
    private String message;
    private String url;

    public void set (String status, String message, String url) {
        this.status = status;
        this.message = message;
        this.url = url;
    }

}
