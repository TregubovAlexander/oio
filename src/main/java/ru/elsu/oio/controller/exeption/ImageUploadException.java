package ru.elsu.oio.controller.exeption;

public class ImageUploadException extends RuntimeException {

    public ImageUploadException(String msg) {
        super(msg);
    }

    public ImageUploadException(String msg, Exception ex) {
        super(msg, ex);
    }

}