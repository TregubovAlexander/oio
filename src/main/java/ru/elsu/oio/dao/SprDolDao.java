package ru.elsu.oio.dao;

import ru.elsu.oio.entity.SprDol;
import java.util.List;

public interface SprDolDao {

    public SprDol getById(Long id);                     // Получить должность по идентификатору
    public SprDol getByName(String name);               // Получить должность по имени
    public List<SprDol> getAll();                       // Получить весь список должностей
    public void save(SprDol sprDol);                    // Сохранить (обновить или добавить) запись
    public void delete(SprDol sprDol);                  // Удалить запись из справочника должностей

}