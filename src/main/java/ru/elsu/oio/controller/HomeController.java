package ru.elsu.oio.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.spring.support.Layout;
import ru.elsu.oio.Url;
import ru.elsu.oio.ace.AceAdmin;
import ru.elsu.oio.dao.PersonDao;

@Controller
public class HomeController {

    @Autowired
    private AceAdmin aceAdmin;

    @Autowired
    MessageSource messageSource;
    private String getMessage(String key){
        return messageSource.getMessage(key, null, LocaleContextHolder.getLocale());
    }
    private String getMessage(String key, Object[] args){
        return messageSource.getMessage(key, args, LocaleContextHolder.getLocale());
    }

    // ===   Login   ===================================================================================================
    @Layout(value = "layouts/login")
    @RequestMapping(Url.LOGIN_PAGE)
    public String login(ModelMap model) {
        aceAdmin.getPage().setTitle("Отдел информатизации образования - Авторизация");
        aceAdmin.getSite().setBrand_text(getMessage("site.brand.text"));
        model.addAttribute("aceadmin",aceAdmin);
        return "";      // Интерсептор вернет шаблон (пустой вид не вставит)
    }

    // ===   Home   ====================================================================================================
    @Layout(value = "layouts/default")  // Можно не писать, по-умолчанию и так default
    @RequestMapping(Url.INDEX_PAGE)
    public String home(ModelMap model) {

        aceAdmin.getPage().setTitle("Отдел информатизации образования");
        aceAdmin.getPage().setDescription("ЕГУ им. И.А. Бунина");
        aceAdmin.getPage().setInline_scripts(true);

        model.addAttribute("aceadmin",aceAdmin);

        model.addAttribute("version","1.0");


        return "home";
    }




}
