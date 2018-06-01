package ru.elsu.oio.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.elsu.oio.Url;
import ru.elsu.oio.dao.ChildrenDao;
import ru.elsu.oio.dto.IdName;
import ru.elsu.oio.dto.PersonDto;
import ru.elsu.oio.entity.Person;
import ru.elsu.oio.entity.SprTabelNotation;
import ru.elsu.oio.services.PersonService;
import ru.elsu.oio.services.SprService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(Url.API_SERVICE)
public class ServiceRestController {

    @Autowired
    private PersonService personService;

    @Autowired
    private SprService sprService;

    //region === Список сотрудников (Id и FullName) =============================================================================
    @GetMapping(Url.PERSONS)
    public List<IdName> getAllPersons() {
        List<Person> personList = personService.getAll();
        List<IdName> idNameList = new ArrayList<>();
        for (Person p : personList) {
            idNameList.add(new IdName(p.getId(), p.getFullName()));
        }
        return idNameList;
    }
    //endregion

    //region === Список кодов табеля (Id и Kod+Name) =============================================================================
    @GetMapping(Url.TABEL_NOTATION)
    public List<IdName> getTabelNotations() {
        List<SprTabelNotation> tnList = sprService.getActiveTabelNotations();

        List<IdName> idNameList = new ArrayList<>();
        for (SprTabelNotation tn : tnList) {
            idNameList.add(new IdName(tn.getId(), String.format("%2s - %s", tn.getKod(), tn.getName())));
        }
        return idNameList;
    }
    //endregion


}
