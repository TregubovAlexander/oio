package ru.elsu.oio.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.elsu.oio.dao.TabelSpDaysDao;
import ru.elsu.oio.dao.TabelHolidaysDao;
import ru.elsu.oio.dto.TabelSpDaysDto;
import ru.elsu.oio.entity.Person;
import ru.elsu.oio.entity.TabelSpDays;
import ru.elsu.oio.entity.TabelHolidays;
import ru.elsu.oio.tabel.Tabel;
import ru.elsu.oio.utils.Util;

import java.util.Date;
import java.util.List;

@Service
public class TabelServiceImpl implements TabelService {

    @Autowired
    TabelSpDaysDao tabelSpDaysDao;

    @Autowired
    TabelHolidaysDao tabelHolidaysDao;

    @Autowired
    PersonService personService;

    @Autowired
    SprService sprService;


    //region === TabelDays ======================================================================================================
    @Override
    public TabelSpDays getTabelSpDays(Long id) {
        return tabelSpDaysDao.getById(id);
    }

    @Override
    public List<TabelSpDays> getTabelSpDays(Date date1, Date date2) {
        return tabelSpDaysDao.get(date1, date2);
    }

    @Override
    public List<TabelSpDays> getTabelSpDays(int year, int month) {
        return getTabelSpDays(Tabel.getFirstDate(year, month), Tabel.getLastDate(year, month));
    }

    @Override
    public List<TabelSpDays> getTabelSpDays(Person person, Date date1, Date date2) {
        return tabelSpDaysDao.get(person, date1, date2);
    }

    @Override
    public TabelSpDays createTabelSpDays(TabelSpDaysDto dto) {
        TabelSpDays spDays = new TabelSpDays();

        spDays.setPerson(personService.getById(dto.getPersonId()));
        spDays.setDateBegin(Util.strToDate(dto.getDateBegin()));
        spDays.setDateEnd(Util.strToDate(dto.getDateEnd()));
        spDays.setKod(sprService.getTabelNotationById(dto.getKodId()));

        saveTabelSpDays(spDays);
        return spDays;
    }

    @Override
    public void saveTabelSpDays(TabelSpDays tabelSpDays) {
        tabelSpDaysDao.save(tabelSpDays);
    }

    @Override
    public void deleteTabelSpDays(TabelSpDays tabelSpDays) {
        tabelSpDaysDao.delete(tabelSpDays);
    }
    //endregion

    //region === TabelHolidays ==================================================================================================
    @Override
    public List<TabelHolidays> getTabelHolidays(int year, int month) {
        return tabelHolidaysDao.get(year, month);
    }

    @Override
    public void saveTabelHolidays(TabelHolidays tabelHolidays) {
        tabelHolidaysDao.save(tabelHolidays);
    }

    @Override
    public void deleteTabelHolidays(TabelHolidays tabelHolidays) {
        tabelHolidaysDao.delete(tabelHolidays);
    }
    //endregion
}
