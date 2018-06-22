package ru.elsu.oio.dao;

import ru.elsu.oio.entity.TabelHolidays;
import java.util.List;


public interface TabelHolidaysDao {

    public TabelHolidays getById(Long id); // Получить одну конкретную дату по id
    public List<TabelHolidays> get(int year); // Получить список всех праздничных дат за год
    public List<TabelHolidays> get(int year, int month); // Получить список всех праздничных дат за месяц года
    public TabelHolidays get(int year, int month, int day); // Получить одну конкретную дату

    public void save(TabelHolidays tabelHolidays);
    public void delete(TabelHolidays tabelHolidays);

}
