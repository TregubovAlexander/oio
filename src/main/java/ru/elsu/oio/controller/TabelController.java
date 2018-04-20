package ru.elsu.oio.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.elsu.oio.Url;
import ru.elsu.oio.ace.AceAdmin;
import ru.elsu.oio.services.TabelService;
import ru.elsu.oio.tabel.Tabel;

@Controller
public class TabelController {

    @Autowired
    private AceAdmin aceAdmin;

    @Autowired
    private TabelService tabelService;

    @Autowired
    MessageSource messageSource;
    private String getMessage(String key){
        return messageSource.getMessage(key, null, LocaleContextHolder.getLocale());
    }
    private String getMessage(String key, Object[] args){
        return messageSource.getMessage(key, args, LocaleContextHolder.getLocale());
    }

    @Value("${files.path}")
    private String filesPath;

    @Value("${tabel.file.limit}")
    private int tabelFileLimit;


    //region ===   Табель - Начальная страница раздела ==========================================================================
    @RequestMapping(Url.DOC + Url.TABEL_PAGE)
    public String home(ModelMap model) {

        aceAdmin.setPageInfo(getMessage("ui.section.tabel.name"), getMessage("organization.name"));
        aceAdmin.getPage().setInline_scripts(true);
        model.addAttribute("aceadmin",aceAdmin);


        // Загоняем в модель список файлов с табелем
        model.addAttribute("tabelList", Tabel.getTabelList(filesPath + "/tabel/", tabelFileLimit));



        return "tabel";
    }
    //endregion





}
