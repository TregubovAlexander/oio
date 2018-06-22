package ru.elsu.oio.services;

import ru.elsu.oio.dto.TabelHolidaysDto;
import ru.elsu.oio.dto.TabelSpDaysDto;
import ru.elsu.oio.entity.Person;
import ru.elsu.oio.entity.TabelSpDays;
import ru.elsu.oio.entity.TabelHolidays;

import java.util.Date;
import java.util.List;

public interface TabelService {

    // TabelDays
    public TabelSpDays getTabelSpDays(Long id);
    public List<TabelSpDays> getTabelSpDays(Date date1, Date date2);
    public List<TabelSpDays> getTabelSpDays(int year, int month);
    public List<TabelSpDays> getTabelSpDays(Person person, Date date1, Date date2);
    public TabelSpDays createTabelSpDays(TabelSpDaysDto dto);
    public void saveTabelSpDays(TabelSpDays tabelSpDays);
    public void deleteTabelSpDays(TabelSpDays tabelSpDays);


    // TabelHolidays
    public TabelHolidays getTabelHolidays(Long id);
    public List<TabelHolidays> getTabelHolidays(int year);
    public List<TabelHolidays> getTabelHolidays(int year, int month);
    public TabelHolidays getTabelHolidays(int year, int month, int day);
    public TabelHolidays createTabelHolidays(TabelHolidaysDto dto);
    public void saveTabelHolidays(TabelHolidays tabelHolidays);
    public void deleteTabelHolidays(TabelHolidays tabelHolidays);

}