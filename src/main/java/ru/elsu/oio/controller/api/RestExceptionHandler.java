package ru.elsu.oio.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import ru.elsu.oio.controller.exeption.ResourceNotFoundException;
import ru.elsu.oio.dto.ErrorDetail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ControllerAdvice
public class RestExceptionHandler {

    final static Logger logger = LoggerFactory.getLogger(RestExceptionHandler.class);

    @Autowired
    MessageSource messageSource;

    public String getFullURL(HttpServletRequest request) {
        StringBuffer requestURL = request.getRequestURL();
        String queryString = request.getQueryString();

        if (queryString == null) {
            return requestURL.toString();
        } else {
            return requestURL.append('?').append(queryString).toString();
        }
    }



    //region === Обработка исключения ResourceNotFoundException (404 ошибка) ====================================================
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(Principal principal, ResourceNotFoundException exception, HttpServletRequest request) {
        String userName = "NULL";
        if (principal != null) userName = principal.getName();
        logger.error("Запрос: " + getFullURL(request) + " пользователя " + userName + " вызвал исключение " + exception);

        ErrorDetail errorDetail = new ErrorDetail(
                HttpStatus.NOT_FOUND,
                "Ресурс не найден",
                exception.getMessage()
        );
        return new ResponseEntity<>(errorDetail, null, errorDetail.getStatus());
    }//endregion


    //region === Обработка исключения MethodArgumentNotValidException ===========================================================
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationError(Principal principal, MethodArgumentNotValidException exception, HttpServletRequest request) {
        String userName = "NULL";
        if (principal != null) userName = principal.getName();
        logger.error("Запрос: " + getFullURL(request) + " пользователя " + userName + " вызвал исключение " + exception);

        ErrorDetail errorDetail = new ErrorDetail(
                HttpStatus.BAD_REQUEST,
                messageSource.getMessage("validationError", null, LocaleContextHolder.getLocale()),
                new ArrayList<String>()
        );

        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
        for (FieldError fe: fieldErrors) {
            errorDetail.getErrors().add( fe.getField() + ": " + messageSource.getMessage(fe, LocaleContextHolder.getLocale()) );
        }

        return new ResponseEntity<>(errorDetail, null, errorDetail.getStatus());
    }//endregion


    //region === Обработка ВСЕХ исключений ======================================================================================
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleAllException(Principal principal, Exception exception, HttpServletRequest request) {
        String userName = "NULL";
        if (principal != null) userName = principal.getName();
        logger.error("Запрос: " + getFullURL(request) + " пользователя " + userName + " вызвал исключение " + exception);

        ErrorDetail errorDetail = new ErrorDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Произошла непредвиденная ошибка",
                exception.getMessage()
        );
        return new ResponseEntity<>(errorDetail, null, errorDetail.getStatus());
    }//endregion


}
