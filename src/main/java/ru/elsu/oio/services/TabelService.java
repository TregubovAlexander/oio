package ru.elsu.oio.services;

import ru.elsu.oio.entity.TabelSpecialDays;
import ru.elsu.oio.entity.TabelHolidays;

import java.util.Date;
import java.util.List;

public interface TabelService {

    // TabelDays
    public List<TabelSpecialDays> getTabelDays(Date date1, Date date2);
    public void saveTabelDays(TabelSpecialDays tabelSpecialDays);
    public void deleteTabelDays(TabelSpecialDays tabelSpecialDays);

    // TabelHolidays
    public List<TabelHolidays> getTabelHolidays(int year, int month);
    public void saveTabelHolidays(TabelHolidays tabelHolidays);
    public void deleteTabelHolidays(TabelHolidays tabelHolidays);

}