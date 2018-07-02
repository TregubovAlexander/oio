package ru.elsu.oio.dao;


import ru.elsu.oio.entity.SprTabelNotation;

import java.util.List;

public interface SprTabelNotationDao {

    public SprTabelNotation getById(Long id);                     // Получить запись по идентификатору
    public SprTabelNotation getByKod(String kod);                 // Получить запись по коду
    public List<SprTabelNotation> getAll();                       // Получить весь список кодов табеля
    public List<SprTabelNotation> getAllActive();                 // Получить список только используемых нами кодов табеля
    public void save(SprTabelNotation sprTabelNotation);          // Сохранить (обновить или добавить) запись
    public void delete(SprTabelNotation sprTabelNotation);        // Удалить запись из справочника

}