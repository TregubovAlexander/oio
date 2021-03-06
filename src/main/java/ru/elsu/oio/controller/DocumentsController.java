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
import ru.elsu.oio.ace.StyleMapping;
import ru.elsu.oio.services.TabelService;
import ru.elsu.oio.tabel.Tabel;

import java.util.Arrays;

import static ru.elsu.oio.ace.ScriptMapping.*;

@Controller
@RequestMapping(Url.DOC)
public class DocumentsController {

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
    private int tabelFilesLimit;

    @Value("${tabel.notation.otpusk.id}")
    private Long tnOtpuskId;                    // ID отпуска в справочнике условных обозначений табеля




    //region ===   Документация - Табель ========================================================================================
    @RequestMapping(Url.TABEL)
    public String docTabel(ModelMap model) {

        aceAdmin.setPageInfo(getMessage("ui.section.tabel.name"), getMessage("organization.name"));
        aceAdmin.getPage().setInline_scripts(true);
        aceAdmin.getPage().setStyles(Arrays.asList(
                StyleMapping.DATETIMEPICKER,
                StyleMapping.DATEPICKER,
                StyleMapping.SWEETALERT2,
                StyleMapping.JQUERY_UI,
                StyleMapping.CHOSEN,
                StyleMapping.CALENDAR,
                StyleMapping.COLORPICKER
        ));
        aceAdmin.getPage().setScripts(Arrays.asList(
                JQUERY_UI1,
                JQUERY_UI2,
                MOMENT,
                DATETIMEPICKER,
                DATEPICKER,
                SWEETALERT2,
                FORMVALIDATION,
                CHOSEN,
                CALENDAR,
                COLORPICKER
        ));

        aceAdmin.setActiveSidebarItem("sbDocTabel");

        model.addAttribute("aceadmin",aceAdmin);

        // Список файлов с табелем
        model.addAttribute("tabelList", Tabel.getTabelList(filesPath + "/tabel/", tabelFilesLimit));

        return "tabel";
    }
    //endregion


    //region ===   Документация - График отпусков ===============================================================================
    @RequestMapping(Url.GRAFIK_OTPUSKOV)
    public String docGrafikOtpuskov(ModelMap model) {

        aceAdmin.setPageInfo(getMessage("ui.section.grafikotpuskov.name"), getMessage("organization.name"));
        aceAdmin.getPage().setInline_scripts(true);
        aceAdmin.getPage().setStyles(Arrays.asList(
                StyleMapping.DATETIMEPICKER,
                StyleMapping.DATEPICKER,
                StyleMapping.SWEETALERT2,
                StyleMapping.JQUERY_UI,
                StyleMapping.CHOSEN
        ));
        aceAdmin.getPage().setScripts(Arrays.asList(
                JQUERY_UI1,
                JQUERY_UI2,
                MOMENT,
                DATETIMEPICKER,
                DATEPICKER,
                SWEETALERT2,
                FORMVALIDATION,
                CHOSEN
        ));

        aceAdmin.setActiveSidebarItem("sbDocOtpusk");

        model.addAttribute("aceadmin",aceAdmin);
        model.addAttribute("tnOtpuskId",tnOtpuskId);





        return "grafik-otpuskov";
    }
    //endregion





}
