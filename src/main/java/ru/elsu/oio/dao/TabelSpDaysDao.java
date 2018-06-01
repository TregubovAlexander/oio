package ru.elsu.oio.dao;

import ru.elsu.oio.entity.Person;
import ru.elsu.oio.entity.TabelSpDays;

import java.util.Date;
import java.util.List;

public interface TabelSpDaysDao {
    public TabelSpDays getById(Long id);
    public List<TabelSpDays> get(Date date1, Date date2); // Получить список всех особенных дней табеля между date1 и date2 для всех сотрудников
    public List<TabelSpDays> get(Person person, Date date1, Date date2); // Получить особенные дни табеля между date1 и date2 для конкретного сотрудника
    public void save(TabelSpDays tabelSpDays);
    public void delete(TabelSpDays tabelSpDays);
}
