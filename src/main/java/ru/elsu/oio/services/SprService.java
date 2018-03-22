package ru.elsu.oio.services;

import ru.elsu.oio.entity.SprDol;
import ru.elsu.oio.entity.SprTabelNotation;

import java.util.List;

public interface SprService {

    // Справочник должностей
    public SprDol getDolById(Long id);
    public List<SprDol> getSprDol();
    public void saveDol(SprDol dol);
    public void deleteDol(SprDol dol);


    // Справочник кодов табеля
    public SprTabelNotation getTabelNotationById(Long id);
    public List<SprTabelNotation> getAllTabelNotations();
    public List<SprTabelNotation> getActiveTabelNotations();
    public void saveTabelNotation(SprTabelNotation sprTabelNotation);
    public void deleteTabelNotation(SprTabelNotation sprTabelNotation);

}
