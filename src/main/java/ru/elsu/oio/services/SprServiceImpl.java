package ru.elsu.oio.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.elsu.oio.dao.SprDolDao;
import ru.elsu.oio.dao.SprTabelNotationDao;
import ru.elsu.oio.entity.SprDol;
import ru.elsu.oio.entity.SprTabelNotation;

import java.util.List;

@Service
public class SprServiceImpl implements SprService {

    @Autowired
    SprDolDao sprDolDao;

    @Autowired
    SprTabelNotationDao sprTabelNotationDao;

    //region === SprDol =========================================================================================================

    @Override
    public SprDol getDolById(Long id) {
        return sprDolDao.getById(id);
    }

    @Override
    public List<SprDol> getSprDol() {
        return sprDolDao.getAll();
    }

    @Override
    public void saveDol(SprDol dol) {
        sprDolDao.save(dol);
    }

    @Override
    public void deleteDol(SprDol dol) {
        sprDolDao.delete(dol);
    }

    //endregion

    //region === SprTabelNotation ===============================================================================================

    @Override
    public SprTabelNotation getTabelNotationById(Long id) {
        return sprTabelNotationDao.getById(id);
    }

    @Override
    public List<SprTabelNotation> getAllTabelNotations() {
        return sprTabelNotationDao.getAll();
    }

    @Override
    public List<SprTabelNotation> getActiveTabelNotations() {
        return sprTabelNotationDao.getAllActive();
    }

    @Override
    public void saveTabelNotation(SprTabelNotation sprTabelNotation) {
        sprTabelNotationDao.save(sprTabelNotation);
    }

    @Override
    public void deleteTabelNotation(SprTabelNotation sprTabelNotation) {
        sprTabelNotationDao.delete(sprTabelNotation);
    }

    //endregion
}
