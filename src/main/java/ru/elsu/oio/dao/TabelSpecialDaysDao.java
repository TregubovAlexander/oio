package ru.elsu.oio.dao;

import ru.elsu.oio.entity.TabelSpecialDays;

import java.util.Date;
import java.util.List;

public interface TabelSpecialDaysDao {
    public List<TabelSpecialDays> get(Date date1, Date date2); // Получить список всех особенных дней табеля между date1 и date2 для всех сотрудников
    public void save(TabelSpecialDays tabelSpecialDays);
    public void delete(TabelSpecialDays tabelSpecialDays);
}
