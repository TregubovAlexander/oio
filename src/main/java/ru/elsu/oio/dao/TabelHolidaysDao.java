package ru.elsu.oio.dao;

import ru.elsu.oio.entity.TabelHolidays;
import java.util.List;


public interface TabelHolidaysDao {
    public List<TabelHolidays> get(int year, int month); // Получить список всех праздничных дат за месяц года
    public void save(TabelHolidays tabelHolidays);
    public void delete(TabelHolidays tabelHolidays);
}
