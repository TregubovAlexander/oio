package ru.elsu.oio.controller.exeption;

public class IncorrectUserDataException extends RuntimeException {

    public IncorrectUserDataException(String msg) {
        super(msg);
    }

    public IncorrectUserDataException(String msg, Exception ex) {
        super(msg, ex);
    }
}
