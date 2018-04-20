package ru.elsu.oio.tabel;
import ru.elsu.oio.utils.Util;

import java.io.File;
import java.util.*;

public final class Tabel { // Класс не расширяемый, только статические методы

    private Tabel() {} // Создать экземпляр класса (инстанцировать) невозможно

    //region === Список пар год/месяц на файлы с табелем ===============================================================================
    /**
     * Получаем список пар чисел (год и месяц) для всех файлов с табелями, хранящихся в файловой системе сервера
     * @param path путь к папке в которой хранятся файлы табеля
     * @param limit Число, ограничивающее длину такого списка
     * @return Список строк - ссылок на файлы
     */
    public static List<TabelPeriod> getTabelList(String path, int limit) {
        // Получаем список файлов в папке
        List<File> files = new ArrayList<File>();
        try {
            File folder = new File(path);
            for (File f : folder.listFiles()) {
                if (f.isFile())
                    files.add(f);
            }
        } catch (Exception e) {
            throw new RuntimeException("Не удалось получить список файлов табеля");
        }
        // Сортируем по дате последней модификации
        Collections.sort(files, new Comparator<File>() {
            public int compare(File f1, File f2) {
                return Long.compare(f2.lastModified(), f1.lastModified());
            }
        });
        // Формируем список с результатом
        List<TabelPeriod> tpList = new ArrayList<>();
        String fn = "", s = "";
        int i = 0;
        for (File f : files) {
            if (i > limit) break;
            fn = f.getName();
            if (Util.isCorrectTabelFileName(fn)) {
                s = fn.replaceAll("[^0-9]", "");
                tpList.add(new TabelPeriod(s.substring(0,4), s.substring(4)));
                i++;
            }
        }

        return tpList;
    }
    //endregion

    // Дата первого дня табеля
    public static Date getFirstDate(int year, int month) {
        Calendar calendar = new GregorianCalendar(year, month - 1, 1);
        return calendar.getTime();
    }

    // Дата последнего дня табеля
    public static Date getLastDate(int year, int month) {
        Calendar calendar = new GregorianCalendar(year, month - 1, 1);
        int dayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendar.set(year, month - 1, dayOfMonth);
        return calendar.getTime();
    }

}

