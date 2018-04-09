package ru.elsu.oio.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import ru.elsu.oio.Url;
import ru.elsu.oio.ace.AceAdmin;
import ru.elsu.oio.ace.StyleMapping;
import ru.elsu.oio.controller.exeption.ResourceNotFoundException;
import ru.elsu.oio.entity.Person;
import ru.elsu.oio.entity.SprDol;
import ru.elsu.oio.services.PersonService;
import ru.elsu.oio.services.SprService;
import ru.elsu.oio.utils.Util;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

import static ru.elsu.oio.ace.ScriptMapping.*;

@Controller
@PropertySources(value = { @PropertySource("classpath:/application.properties") })
public class PersonController {

    @Autowired
    private AceAdmin aceAdmin;

    @Autowired
    private PersonService personService;

    @Autowired
    private SprService sprService;


    // Загоняем в модель справочник должностей
    @ModelAttribute("sprDol")
    public List<SprDol> getSprDol(){
        return sprService.getSprDol();
    }



    // === Показ списка всех сотрудников отдела ========================================================================
    @GetMapping(Url.PERSONS)
    public String getAllPersons(ModelMap model) {

        aceAdmin.getPage().setTitle("Отдел информатизации образования");
        aceAdmin.getPage().setDescription("Список сотрудников");
        aceAdmin.getPage().setInline_scripts(true);
        aceAdmin.getPage().setStyles(Arrays.asList(StyleMapping.DATETIMEPICKER, StyleMapping.SWEETALERT2));
        aceAdmin.getPage().setScripts(Arrays.asList(MOMENT, DATETIMEPICKER, SWEETALERT2, FORMVALIDATION));
        aceAdmin.getPage().setTop_menu("topmenu_extravalue");

        model.addAttribute("aceadmin",aceAdmin);
        model.addAttribute("persons", personService.getAll());

        return "persons";
    }


    // === Показ инормации о конкретном сотруднике с идентификатором id ================================================
    @GetMapping(Url.PERSON)
    public String getPersonById(@PathVariable Long id, ModelMap model, HttpServletRequest request) {

        // Получаем сотрудника по id
        Person person = personService.getById(id);
        if (person == null) {
            throw new ResourceNotFoundException("Сотрудник с ID " + id + " не найден");
        }

        aceAdmin.getPage().setTitle("Сведения о сотруднике");
        aceAdmin.getPage().setDescription(person.getFullName());
        aceAdmin.getPage().setInline_scripts(true);
        aceAdmin.getPage().setStyles(Arrays.asList(StyleMapping.DATETIMEPICKER, StyleMapping.CHOSEN, StyleMapping.KLADR,
                StyleMapping.SWEETALERT2, StyleMapping.X_EDITABLE, StyleMapping.GRITTER));
        aceAdmin.getPage().setScripts(Arrays.asList(MOMENT, DATETIMEPICKER, EASY_PIE_CHART, MASKEDINPUT, CHOSEN,
                WYSIWYG1, WYSIWYG2, SWEETALERT2, FORMVALIDATION, X_EDITABLE1, X_EDITABLE2, KLADR, YANDEX_MAPS, GRITTER));
        model.addAttribute("aceadmin",aceAdmin);

        model.addAttribute("person", person); // TODO: Убрать после, т.к. person получается по AJAX из REST
        model.addAttribute("person_id", person.getId());
        model.addAttribute("person_age", Util.age(person.getDr()));

        return "person";
    }








}
