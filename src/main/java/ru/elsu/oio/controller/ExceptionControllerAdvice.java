package ru.elsu.oio.controller;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import ru.elsu.oio.ace.AceAdmin;
import ru.elsu.oio.controller.exeption.ResourceNotFoundException;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;


//@ControllerAdvice
public class ExceptionControllerAdvice {

/*    final static Logger logger = LoggerFactory.getLogger(ExceptionControllerAdvice.class);

    public String getFullURL(HttpServletRequest request) {
        StringBuffer requestURL = request.getRequestURL();
        String queryString = request.getQueryString();

        if (queryString == null) {
            return requestURL.toString();
        } else {
            return requestURL.append('?').append(queryString).toString();
        }
    }

    @Autowired
    AceAdmin aceAdmin;

    // === Обработка исключения ResourceNotFoundException (404 ошибка) =================================================
    @ExceptionHandler(ResourceNotFoundException.class)
    public ModelAndView handleResourceNotFoundException(Principal pr, HttpServletRequest req, Exception exception) {
        logger.error("Запрос: " + getFullURL(req) + " пользователя " + pr.getName() + " вызвал исключение " + exception);

        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", exception);
        mav.addObject("url", req.getRequestURL());

        aceAdmin.getPage().setTitle("Страница не найдена");
        aceAdmin.getPage().setDescription("Ошибка");
        mav.addObject("aceadmin", aceAdmin);

        mav.addObject("view","error-404");
        mav.setViewName("layouts/default");
        return mav;
    }

    // === Обработка ВСЕХ исключений ===================================================================================
    @ExceptionHandler(Exception.class)
    public ModelAndView handleAllException(Principal pr, HttpServletRequest req, Exception exception) {
        logger.error("Запрос: " + getFullURL(req) + " пользователя " + pr.getName() + " вызвал исключение " + exception);

        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", exception);
        mav.addObject("url", req.getRequestURL());

        aceAdmin.getPage().setTitle("Произошла непредвиденная ошибка");
        aceAdmin.getPage().setDescription("Ошибка");
        mav.addObject("aceadmin", aceAdmin);

        mav.addObject("view","error");
        mav.setViewName("layouts/default");
        return mav;
    }
*/

}