package ru.elsu.oio.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.elsu.oio.dao.TabelSpecialDaysDao;
import ru.elsu.oio.dao.TabelHolidaysDao;
import ru.elsu.oio.entity.TabelSpecialDays;
import ru.elsu.oio.entity.TabelHolidays;
import ru.elsu.oio.tabel.Tabel;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class TabelServiceImpl implements TabelService {

    @Autowired
    TabelSpecialDaysDao tabelSpecialDaysDao;

    @Autowired
    TabelHolidaysDao tabelHolidaysDao;

    //region === TabelDays ======================================================================================================
    @Override
    public List<TabelSpecialDays> getTabelDays(Date date1, Date date2) {
        return tabelSpecialDaysDao.get(date1, date2);
    }

    @Override
    public List<TabelSpecialDays> getTabelDays(int year, int month) {
        return getTabelDays(Tabel.getFirstDate(year, month), Tabel.getLastDate(year, month));
    }

    @Override
    public void saveTabelDays(TabelSpecialDays tabelSpecialDays) {
        tabelSpecialDaysDao.save(tabelSpecialDays);
    }

    @Override
    public void deleteTabelDays(TabelSpecialDays tabelSpecialDays) {
        tabelSpecialDaysDao.delete(tabelSpecialDays);
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
