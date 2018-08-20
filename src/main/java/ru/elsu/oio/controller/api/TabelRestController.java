package ru.elsu.oio.controller.api;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import ru.elsu.oio.Url;
import ru.elsu.oio.controller.exeption.IncorrectUserDataException;
import ru.elsu.oio.dto.ErrorDetail;
import ru.elsu.oio.dto.TabelHolidaysDto;
import ru.elsu.oio.entity.*;
import ru.elsu.oio.services.PersonService;
import ru.elsu.oio.services.SprService;
import ru.elsu.oio.services.TabelService;
import ru.elsu.oio.tabel.PersonForOtpusk;
import ru.elsu.oio.tabel.PersonForTabel;
import ru.elsu.oio.dto.TabelSpDaysDto;
import ru.elsu.oio.utils.ExcelUtil;
import ru.elsu.oio.utils.Util;

import javax.validation.Valid;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping(Url.API)
public class TabelRestController {

    @Autowired
    private PersonService personService;

    @Autowired
    private SprService sprService;

    @Autowired
    private TabelService tabelService;

    @Value("${files.path}")
    private String filesPath;

    @Value("${tabel.notation.otpusk.id}")
    private Long tnOtpuskId;


    private List<TabelSpDays> tabelSpDaysList = null;       // Список особенных дней
    private List<TabelHolidays> tabelHolidaysList = null;   // Список праздничных дней


    //region === Табель =========================================================================================================

    //region === GET - Отдаем файл с табелем по HTTP ============================================================================
    @GetMapping(Url.TABEL + "/{year}/{month}")
    public HttpEntity<byte[]> sendTabelFile(@PathVariable int year, @PathVariable int month) {
        String fileName = "tabel-" + Integer.toString(year) + "-" + String.format("%02d", month) + ".xlsx";

        Path path = Paths.get(filesPath + "//tabel//" + fileName);

        byte[] documentBody = new byte[0];
        try {                                   // TODO: Подумать над обработчиком
            documentBody = Files.readAllBytes(path);
        } catch (IOException e) {
            e.printStackTrace();
        }

        HttpHeaders header = new HttpHeaders();
        header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
        header.setContentType(new MediaType("application", "vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        header.setContentLength(documentBody.length);

        return new HttpEntity<byte[]>(documentBody, header);
    }
    //endregion


    //region === POST - Создаем табель за указанный год/месяц  и сохраняем на сервере в виде XLSX файла =========================
    /**
     * Создание табеля за указанные год и месяц и сохранение его на сервере в формате XLSX
     *
     * @param year   - год за который делается табель
     * @param month  - месяц за который делается табель
     */
    @PostMapping(Url.TABEL + "/{year}/{month}")
    public ResponseEntity<?> getTabel(@PathVariable int year, @PathVariable int month) {

        try {
            //region Получаем список всех сотрудников для табеля, с учетом дат начала и окончания должности (в том числе уволенных)
            List<Person> personList = personService.getForTabel(year, month);
            List<PersonForTabel> pftList = new ArrayList<>();
            for (Person person : personList) {
                for (Post post : person.getPostList()) {

                    if (post.forTabel(year, month)) {
                        pftList.add(new PersonForTabel(
                                person.getId(),
                                person.getSurname(),
                                person.getName(),
                                person.getPatronymic(),
                                post.getStavka(),
                                person.getTabNo(),
                                post.getDol().getShortName(),
                                post.getActive(),
                                post.getDateBegin(),
                                post.getDateEnd()
                        ));
                    }

                }
            }

            // Сортируем список сотрудников по фамилии
            pftList.sort(new Comparator<PersonForTabel>() {
                @Override
                public int compare(PersonForTabel lhs, PersonForTabel rhs) {
                    String str1 = lhs.getSurname();
                    String str2 = rhs.getSurname();
                    int res = String.CASE_INSENSITIVE_ORDER.compare(str1, str2);
                    return (res != 0) ? res : str1.compareTo(str2);
                }
            });
            //endregion

            // Создаем файл с табелем
            createTabel(year, month, pftList);

        } catch (FileNotFoundException e) { //TODO: подумать над исключениями - лучше генерировать самостоятельно разные исключения и передавать наверх в этото метод (из createTabel)
            String err = e.getMessage();
            ErrorDetail errorDetail = new ErrorDetail(HttpStatus.BAD_REQUEST, "Ошибка FileNotFoundException", err);
            return new ResponseEntity<ErrorDetail>(errorDetail, errorDetail.getStatus());
        } catch (IOException e) {
            String err = e.getMessage();
            ErrorDetail errorDetail = new ErrorDetail(HttpStatus.BAD_REQUEST, "Ошибка IOException", err);
            return new ResponseEntity<ErrorDetail>(errorDetail, errorDetail.getStatus());
        } catch (Exception e) { // Все остальные исключения (например, NullPointerException когда не найдена именованная ячейка и пр.)
            String err = e.getMessage();
            ErrorDetail errorDetail = new ErrorDetail(HttpStatus.BAD_REQUEST, "Ошибка Exception", err);
            return new ResponseEntity<ErrorDetail>(errorDetail, errorDetail.getStatus());
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }
    //endregion


    //region === Создание файла табеля (открытие шаблона XLSX, заполнение и сохранение под новым именем) ========================
    /**
     * Создание табеля в формате XLSX за указанные год и месяц
     *
     * @param year   - год за который делается табель
     * @param month  - месяц за который делается табель
     * @throws FileNotFoundException
     * @throws IOException
     */
    private void createTabel(int year, int month, List<PersonForTabel> persons) throws IOException { //TODO: подумать над возвращаемыми исключениями

        //TODO: нужна ли проверка корректности года и месяца??? или ее должен делать контроллер???


        //region Загружаем шаблон табеля
        FileInputStream fileInputStream = new FileInputStream(filesPath + "/report/" + "tabel.xlsx");
        Workbook wb = new XSSFWorkbook(fileInputStream);
        fileInputStream.close();

        Sheet sheet;
        Row row, row1, row2;
        Cell cell, cell1, cell2;
        AreaReference areaRef;
        String s;

        // Получаем лист с табелем (первый и единственный в книге)
        sheet = wb.getSheetAt(0); //XSSFSheet sheet = wb.getSheet("Табель");
        //endregion

        //region Даты
        Calendar calendar = new GregorianCalendar();

        calendar.set(year, month - 1, 1);
        int dayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH); // Число дней в месяце
        Date tabelFirstDate = calendar.getTime();

        calendar.set(year, month - 1, dayOfMonth);
        Date tabelLastDate = calendar.getTime();

        String[] monthNames = {"ЯНВАРЯ", "ФЕВРАЛЯ", "МАРТА", "АПРЕЛЯ", "МАЯ", "ИЮНЯ", "ИЮЛЯ", "АВГУСТА", "СЕНТЯБРЯ", "ОКТЯБРЯ", "НОЯБРЯ", "ДЕКАБРЯ"};
        //endregion

        //region Заполняем день, месяц и год табеля
        String sYear = String.format("%02d", year % 100);
        ExcelUtil.getNamedCell("tabelDay", wb).setCellValue(Integer.toString(dayOfMonth));
        ExcelUtil.getNamedCell("tabelMonth", wb).setCellValue(monthNames[month - 1]);
        ExcelUtil.getNamedCell("tabelYear", wb).setCellValue(sYear);
        //endregion

        //region Заполняем дату создания табеля
        Date today = new Date();
        SimpleDateFormat format = new SimpleDateFormat();
        format.applyPattern("dd/MM/yyyy");
        String dateCreation = format.format(today);
        ExcelUtil.getNamedCell("dateCreation", wb).setCellValue(dateCreation);
        ExcelUtil.getNamedCell("signDay", wb).setCellValue(dateCreation.substring(0, 2));
        ExcelUtil.getNamedCell("signMonth", wb).setCellValue(monthNames[month - 1].toLowerCase());
        ExcelUtil.getNamedCell("signYear", wb).setCellValue(sYear);
        //endregion

        //region Получаем номера строк и ячеек с днями месяца
        int[] days = new int[32];
        days[0] = ExcelUtil.getNamedCell("daysRow", wb).getRowIndex();  // Номер строки с номерами дней месяца
        for (int i = 1; i < days.length; i++) {
            s = Integer.toString(i);
            days[i] = ExcelUtil.getNamedCell("personDay" + s, wb).getColumnIndex(); // Номера колонок с днями месяца (1 к 1)
        }
        int[] itog = { // Номера столбцов для подсчета явок/неявок за первую половину месяца - 0 и за весь месяц - 1
                ExcelUtil.getNamedCell("personItog1", wb).getColumnIndex(),
                ExcelUtil.getNamedCell("personItog2", wb).getColumnIndex()
        };

        // Заполняем последние дни месяца
        for (int i = 29; i <= dayOfMonth; i++) {
            row = sheet.getRow(days[0]);
            row.getCell(days[i]).setCellValue(Integer.toString(i));
        }
        //endregion

        //region Получаем номер строки, после которой вставляем заголовок дней (0 - если значение не задано в шаблоне, или задано некорректно)
        int numRowsPerPage = 0;
        try {
            numRowsPerPage = Integer.parseInt(ExcelUtil.getNamedCell("personNum", wb).getStringCellValue()); // Номер должен быть записан в шаблоне в ячейке с именем "personNum"
        } catch (NumberFormatException e) {}
        //endregion

        int personCount = persons.size(); // Число сотрудников

        //region Копируем строку шаблона по числу сотрудников

        // Находим строку с шаблоном
        areaRef = ExcelUtil.getNamedArea("personRow",wb);
        // Получаем номер первой строки с шаблоном
        int templateRowNum = areaRef.getFirstCell().getRow();
        // Получаем число строк в шаблоне
        int templateRowCount = areaRef.getLastCell().getRow() - templateRowNum + 1;
        // Получаем номер последней колонки строки шаблона
        int templateRowLastCellNum = areaRef.getLastCell().getCol();
        // Получаем номер строки с началом нижней части табеля (футер)
        int footerStartRowNum = ExcelUtil.getNamedCell("footerStart", wb).getRowIndex();

        // Сдвигаем футер на (personCount - 1)  * templateRowCount строк вниз
        sheet.shiftRows(footerStartRowNum, sheet.getLastRowNum(), (personCount - 1) * templateRowCount);

        // Копируем строки
        Row newRow, templateRow;

        //region Цикл создания строк
        for (int i = 1; i < personCount; i++) {
            for (int j = 0; j < templateRowCount; j++) {
                newRow = sheet.createRow(templateRowNum + i * templateRowCount + j);
                templateRow = sheet.getRow(templateRowNum + j);

                // Копируем все ячейки из строки шаблона в новую строку
                for (int k = 0; k < templateRow.getLastCellNum(); k++) {
                    Cell oldCell = templateRow.getCell(k);
                    Cell newCell = newRow.createCell(k);

                    if (oldCell == null) {
                        newCell = null;
                        continue;
                    }

                    // Копируем стили
                    CellStyle newCellStyle = wb.createCellStyle();
                    newCellStyle.cloneStyleFrom(oldCell.getCellStyle());
                    newCell.setCellStyle(newCellStyle);
                }
            }
        }//endregion

        //region Объединяем ячейки также, как и в шаблонной строке
        int cellRangeAddressFirstRow;
        for (int i = 0; i < sheet.getNumMergedRegions(); i++) {
            CellRangeAddress cellRangeAddress = sheet.getMergedRegion(i);
            cellRangeAddressFirstRow = cellRangeAddress.getFirstRow();
            if ( cellRangeAddressFirstRow >= templateRowNum && cellRangeAddressFirstRow < templateRowNum + templateRowCount) {  // Регион найден в строке шаблона
                for (int j = 1; j < personCount; j++) {
                    CellRangeAddress newCellRangeAddress = new CellRangeAddress(
                            cellRangeAddress.getFirstRow() + j * templateRowCount,
                            cellRangeAddress.getLastRow() + j * templateRowCount,
                            cellRangeAddress.getFirstColumn(),
                            cellRangeAddress.getLastColumn());
                    sheet.addMergedRegion(newCellRangeAddress);
                }
            }
        }//endregion


        //endregion

        //region --- Заполняем строки табеля ---------------------------------------------------------------------------

        //region Номера столбцов
        int colPersonNum, colPersonFio, colPersonStavka, colPersonTabNo, colPersonDolName;
        colPersonNum = ExcelUtil.getNamedCell("personNum", wb).getColumnIndex();
        colPersonFio = ExcelUtil.getNamedCell("personFio", wb).getColumnIndex();
        colPersonStavka = ExcelUtil.getNamedCell("personStavka", wb).getColumnIndex();
        colPersonTabNo = ExcelUtil.getNamedCell("personTabNo", wb).getColumnIndex();
        colPersonDolName = ExcelUtil.getNamedCell("personDolName", wb).getColumnIndex();
        //endregion

        //region Получаем список особенных дней и список праздников
        tabelSpDaysList = tabelService.getTabelSpDays(tabelFirstDate, tabelLastDate);
        tabelHolidaysList = tabelService.getTabelHolidays(year, month);
        //endregion

        // Константы TODO: заменить на параметры в файле
        SprTabelNotation TN_WEEKEND = sprService.getTabelNotationById(26L);     // 26 - ID выходного дня
        SprTabelNotation TN_WORKDAY = sprService.getTabelNotationById(1L);      // 1 - ID рабочего дня
        SprTabelNotation TN_CHILD_CARE = sprService.getTabelNotationById(15L);  // 15 - ID отпуска по уходу за ребенком до 3-х лет

        String TN_X = "Х";

        // Жирные рамки
        CellRangeAddress range1 = new CellRangeAddress(days[0] - 1 , days[0], 0, templateRowLastCellNum); // вторая строка пока задана грубо
        CellRangeAddress range2 = new CellRangeAddress(days[0] - 1 , days[0], 0, templateRowLastCellNum); // обе строки заданы грубо, в дальнейшем будут уточнены

        //region Цикл по сотрудникам
        PersonForTabel pt = null;
        int rowNum = templateRowNum; // Номер первой строки сотрудника
        for (int i = 0; i < personCount; i++) {
            pt = persons.get(i);
            row1 = sheet.getRow(rowNum);
            row2 = sheet.getRow(rowNum + 1);

            //region ФИО, ставка, таб. номер, должность
            row1.getCell(colPersonNum).setCellValue(i + 1);
            row1.getCell(colPersonFio).setCellValue(Util.getShortFio(pt.getSurname(), pt.getName(), pt.getPatronymic()));
            row1.getCell(colPersonStavka).setCellValue(pt.getStavka() + " ст.");
            row1.getCell(colPersonTabNo).setCellValue(pt.getTabNo());
            row1.getCell(colPersonDolName).setCellValue(pt.getDolName());
            //endregion

            //region Получаем коды всех дней для очередного сотрудника
            Map<Integer, SprTabelNotation> mapDays = new HashMap<Integer, SprTabelNotation>();
            for (int j = 1; j <= dayOfMonth; j++) {
                mapDays.put(j, null);
            }

            int dayBegin, dayEnd;
            Date db, de;

            for (TabelSpDays td: tabelSpDaysList) {
                if (pt.getId() != td.getPerson().getId()) continue;

                db = td.getDateBegin();
                de = td.getDateEnd();

                if (db.before(tabelFirstDate)) db = tabelFirstDate;
                if (de.after(tabelLastDate)) de = tabelLastDate;

                Calendar cal = GregorianCalendar.getInstance();
                cal.setTime(db);
                dayBegin = cal.get(Calendar.DAY_OF_MONTH);
                cal.setTime(de);
                dayEnd = cal.get(Calendar.DAY_OF_MONTH);

                // Пробегаем в цикле по дням
                for (int d = dayBegin; d <= dayEnd; d++) {
                    mapDays.put(d, td.getKod());
                }

            }
            //endregion

            //region Цикл по дням месяца
            int[] yavka = {0, 0};
            int[] neyavka = {0, 0};

            //region Получаем дни начала и конца должности
            int dolDayBegin = 1;
            int dolDayEnd = dayOfMonth;
            Calendar cal = Calendar.getInstance();
            Date dolDateBegin = pt.getDolDateBegin();
            if (dolDateBegin.compareTo(tabelFirstDate) > 0) {
                cal.setTime(dolDateBegin);
                dolDayBegin = cal.get(Calendar.DAY_OF_MONTH);
            }
            Date dolDateEnd = pt.getDolDateEnd();
            if (dolDateEnd != null && dolDateEnd.compareTo(tabelLastDate) < 0) {
                cal.setTime(dolDateEnd);
                dolDayEnd = cal.get(Calendar.DAY_OF_MONTH);
            }
            //endregion

            // TODO: разобраться с цветом явки - не меняется почему-то
//            short cellColor = IndexedColors.WHITE.getIndex(); // TODO: заменить на цвет явки - жестко устанавливать белый не надо!
            short cellColor = TN_WORKDAY.getColor();
            for (int d = dolDayBegin; d <= dolDayEnd; d++) {
                cell1 = null; cell2 = null;
                calendar.set(Calendar.DAY_OF_MONTH, d);
                TabelHolidays th = getHoliday(calendar);

                // Определяем является ли день особенным
                SprTabelNotation tnDayKod = mapDays.get(d);
                if (tnDayKod != null) {
                    cell1 = row1.getCell(days[d]);
                    cell1.setCellValue(tnDayKod.getKod());
                    if (tnDayKod.getWorkDay()) {
                        row2.getCell(days[d]).setCellValue(8 * pt.getStavka());
                        if (d < 16) yavka[0]++;
                        yavka[1]++;
                    } else {
                        cell2 = row2.getCell(days[d]);
                        cell2.setCellValue(TN_X);

                        //region Считаем неявки для особенных дней:
                        // если попадает ня выходной, то не считается неявкой, кроме отпуска по уходу за ребенком до 3-х лет
                        if ( isWeekend(calendar) || (th != null) ) {
                            if ( (th != null && !th.isHoliday()) || tnDayKod.getKod().equals(TN_CHILD_CARE.getKod())) {
                                if (d < 16) neyavka[0]++;
                                neyavka[1]++;
                            }
                        } else {
                            if (d < 16) neyavka[0]++;
                            neyavka[1]++;
                        }
                        //endregion
                        cellColor = tnDayKod.getColor();
                    }
                } else {
                    // Определяем является ли день выходным или праздничным
                    if (isWeekend(calendar) || (th != null)) {
                        cell1 = row1.getCell(days[d]);
                        cell2 = row2.getCell(days[d]);
                        if (th != null && !th.isHoliday()) {    // если день выходной, но рабочий
                            cell1.setCellValue(TN_WORKDAY.getKod());
                            cell2.setCellValue(8 * pt.getStavka());
                            cellColor = TN_WORKDAY.getColor();
                            if (d < 16) yavka[0]++;
                            yavka[1]++;
                        } else {
                            cell1.setCellValue(TN_WEEKEND.getKod());
                            cell2.setCellValue(TN_X);
                            cellColor = TN_WEEKEND.getColor();
                        }
                    } else {
                        // День рабочий
                        row1.getCell(days[d]).setCellValue(TN_WORKDAY.getKod());
                        row2.getCell(days[d]).setCellValue(8 * pt.getStavka());
                        if (d < 16) yavka[0]++;
                        yavka[1]++;
                    }
                }

                // Раскрашиваем ячейки
                if (cell1 != null && cell2 != null) {
                    CellStyle cellStyle = wb.createCellStyle();
                    cellStyle.cloneStyleFrom(cell1.getCellStyle());
                    cellStyle.setFillForegroundColor(cellColor); // Цвет
                    cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                    cell1.setCellStyle(cellStyle);
                    cell2.setCellStyle(cellStyle);
                }



            }//endregion

            //region Устанавливаем число явок/неявок
            row1.getCell(itog[0]).setCellValue(Integer.toString(yavka[0]) + "/" + Integer.toString(neyavka[0]));
            row1.getCell(itog[1]).setCellValue(Integer.toString(yavka[1]) + "/" + Integer.toString(neyavka[1]));
            //endregion

            //region Вставляем строку с номерами дней после строки сотрудника с номером numRowsPerPage
            if (i == numRowsPerPage) {
                range1.setLastRow(rowNum - 1);  // нижняя строка первой жирной рамки
                range2.setFirstRow(rowNum);     // верхняя строка второй жирной рамки

                sheet.shiftRows(rowNum, sheet.getLastRowNum(), 1); // Сдвигаем все строки на 1 вниз
                row = sheet.createRow(rowNum); // Вставляем новую строку

                //region Создаем все ячейки строки и устанавливаем границы
                CellStyle cellStyle = ExcelUtil.setCellBorderStyle(BorderStyle.THIN, wb);
                for (int k = 0; k <= templateRowLastCellNum; k++) {
                    cell = row.createCell(k);
                    cell.setCellStyle(cellStyle);
                }
                //endregion

                //region Объединяем ячейки в этой строке
                CellRangeAddress cellRangeAddress = new CellRangeAddress(rowNum, rowNum, 0, days[1] - 1);
                sheet.addMergedRegion(cellRangeAddress);
                for (int k = 1; k <= 31; k++) {
                    cellRangeAddress.setFirstColumn(days[k]);
                    if ((k == 15) || (k == 31)) {
                        if (k == 15) cellRangeAddress.setLastColumn(itog[0] - 1);
                        if (k == 31) cellRangeAddress.setLastColumn(itog[1] - 1);
                    } else {
                        cellRangeAddress.setLastColumn(days[k + 1] - 1);
                    }
                    sheet.addMergedRegion(cellRangeAddress);
                }
                cellRangeAddress.setFirstColumn(itog[0]);
                cellRangeAddress.setLastColumn(days[16] - 1);
                sheet.addMergedRegion(cellRangeAddress);
                cellRangeAddress.setFirstColumn(itog[1]);
                cellRangeAddress.setLastColumn(templateRowLastCellNum);
                sheet.addMergedRegion(cellRangeAddress);
                //endregion

                //region Вставляем номера дней
                Cell oldCell, newCell;
                Row oldRow = sheet.getRow(days[0]);
                for (int k = 1; k <= dayOfMonth; k++) {
                    // Создаем ячейку и записываем в нее номер дня
                    newCell = row.getCell(days[k]);
                    newCell.setCellValue(Integer.toString(k));
                    // Копируем стили ячеек
                    oldCell = oldRow.getCell(days[k]);
                    cellStyle.cloneStyleFrom(oldCell.getCellStyle());
                    newCell.setCellStyle(cellStyle);
                }
                //endregion

                rowNum++;
            }
            //endregion

            // Сдвигаем указатель текущей строки шаблона
            rowNum += templateRowCount;

        }
        range2.setLastRow(rowNum - 1);

        //region Устанавливаем толстые рамки вокруг ячеек на первом и втором листе
        ExcelUtil.setRangeBorder(BorderStyle.MEDIUM, range1, sheet);
        ExcelUtil.setRangeBorder(BorderStyle.MEDIUM, range2, sheet);
        //endregion



        //endregion ----------------------------------------------------------------------------------------------------

        //region Тест
        // Определение цвета
//        row = sheet.createRow(100);
//        CellStyle style;
//        for (short a = 0; a < 65; a++) {
//            cell = row.createCell(a);
//            cell.setCellValue(a);
//            style = wb.createCellStyle();
//            style.setFillForegroundColor(a); // Цвет
//            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
//            cell.setCellStyle(style);
//        }
//        // getRowBreaks
//        int[] rowBreaks = sheet.getRowBreaks();
//        row = sheet.createRow(101);
//        int i = 0;
//        for (int b: rowBreaks) {
//            cell = row.createCell(i);
//            cell.setCellValue(b);
//            i++;
//        }
        //endregion





        //region Сохраняем сформированный табель под новым именем
        sheet.setSelected(true); // Делаем лист активным
        FileOutputStream fileOutputStream = new FileOutputStream(filesPath + "/tabel/tabel-" + Integer.toString(year) + "-" + String.format("%02d", month) + ".xlsx");
        wb.write(fileOutputStream);
        fileOutputStream.close();
        //endregion
    }
    //endregion

    //endregion


    //region === Особенные дни табеля ===========================================================================================

    //region === GET - Список особенных дней табеля за указанные год/месяц ======================================================
    @GetMapping(Url.TABEL_SPDAYS + "/{year}/{month}")
    public List<TabelSpDaysDto> getTabelSpecialDays(@PathVariable int year, @PathVariable int month) {
        List<TabelSpDays> tsDays = tabelService.getTabelSpDays(year, month);
        List<TabelSpDaysDto> tsDaysDtoList = new ArrayList<>();
        for (TabelSpDays spDays : tsDays) {
            tsDaysDtoList.add(spDays.toDto());
        }

        return tsDaysDtoList;
    }
    //endregion


    //region === POST - Добавление одной записи особенных дней ==================================================================
    @PostMapping(Url.TABEL_SPDAYS)
    public ResponseEntity<?> createSpDays(@Valid @RequestBody TabelSpDaysDto dto) {
        TabelSpDays tabelSpDays = null;
        try {
            // Проверяем, что сотрудник существуют
            Person person = personService.getById(dto.getPersonId());
            if (person == null) {
                throw new IncorrectUserDataException("Сотрудник с идентификатором " + dto.getPersonId() + " не найден");
            }
            // Проверяем, что код табеля существует и активен
            getAndCheckActiveTabelNotation(dto.getKodId());
            // Проверяем, что диапазон дат не пересекается
            checkDateRangeIntersects(person, Util.strToDate(dto.getDateBegin()), Util.strToDate(dto.getDateEnd()));

            tabelSpDays = tabelService.createTabelSpDays(dto);
        } catch (RuntimeException e) {
            ErrorDetail errorDetail = new ErrorDetail(HttpStatus.BAD_REQUEST, "Сохранение не удалось", e.getMessage());
            return new ResponseEntity<ErrorDetail>(errorDetail, errorDetail.getStatus());
        }

        // Если все нормально, отдаем обратно вновь созданный объект
        return new ResponseEntity<TabelSpDaysDto>(tabelSpDays.toDto(), HttpStatus.CREATED);
    }
    //endregion


    //region === PUT - Обновление одной записи особенных дней ===================================================================
    @PutMapping(Url.TABEL_SPDAYS + "/{id}")
    public ResponseEntity<?> updateSpDays(@PathVariable Long id, @Valid @RequestBody TabelSpDaysDto dto) {
        TabelSpDays tabelSpDays = null;
        try {
            // Сначала проверяем, что особенные дни с присланным ID существуют
            tabelSpDays = getAndCheckTabelSpDays(id);

            // При обновлении сотрудника менять НЕЛЬЗЯ - поэтому просто игнорируем поле dto.personId

            // Проверяем, что код табеля существует и активен
            SprTabelNotation tn = getAndCheckActiveTabelNotation(dto.getKodId());

            // Проверяем, что диапазон дат не пересекается
            checkDateRangeIntersects(tabelSpDays.getPerson(), Util.strToDate(dto.getDateBegin()), Util.strToDate(dto.getDateEnd()), id);

            // Обновляем
            tabelSpDays.setKod(tn);
            tabelSpDays.setDateBegin(Util.strToDate(dto.getDateBegin()));
            tabelSpDays.setDateEnd(Util.strToDate(dto.getDateEnd()));
            tabelService.saveTabelSpDays(tabelSpDays);
        } catch (RuntimeException e) {
            ErrorDetail errorDetail = new ErrorDetail(HttpStatus.BAD_REQUEST, "Ошибка обновления данных", e.getMessage());
            return new ResponseEntity<ErrorDetail>(errorDetail, errorDetail.getStatus());
        }

        // Если все нормально, отдаем измененный объект обратно
        return new ResponseEntity<TabelSpDaysDto>(tabelSpDays.toDto(), HttpStatus.OK);
    }
    //endregion


    //region === DELETE - Удаление одной записи особенных дней ==================================================================
    @DeleteMapping(Url.TABEL_SPDAYS + "/{id}")
    public ResponseEntity<?> deleteSpDays(@PathVariable Long id) {
        try {
            // Сначала проверяем, что особенные дни с присланным ID существуют
            TabelSpDays tabelSpDays = getAndCheckTabelSpDays(id);

            tabelService.deleteTabelSpDays(tabelSpDays);
        } catch (RuntimeException e) {
            ErrorDetail errorDetail = new ErrorDetail(HttpStatus.BAD_REQUEST, "Ошибка удаления", e.getMessage());
            return new ResponseEntity<ErrorDetail>(errorDetail, errorDetail.getStatus());
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }
    //endregion

    //endregion


    //region === График отпусков ================================================================================================

    //region === GET - Список сотрудников с датами их отпуска в конкретном году =================================================
    @GetMapping(Url.GRAFIK_OTPUSKOV + "/{year}")
    public List<PersonForOtpusk> getGrafikOtpuskov(@PathVariable int year) {
        // Получаем все особенные дни всех сотрудников за год
        Date d1 = new GregorianCalendar(year, Calendar.JANUARY, 1).getTime();
        Date d2 = new GregorianCalendar(year, Calendar.DECEMBER, 31).getTime();
        List<TabelSpDays> tabelSpDaysList = tabelService.getTabelSpDays(d1, d2);

        // Фильтруем - оставляем только отпуска
        List<PersonForOtpusk> otpusksList = new ArrayList<>();
        for (TabelSpDays days : tabelSpDaysList) {
            if (days.getKod().getId() != tnOtpuskId) continue;

            PersonForOtpusk pfo = new PersonForOtpusk();

            Person p = days.getPerson();

            pfo.setId(days.getId());        // Идентификатор особенного дня (с отпуском)
            pfo.setKodId(days.getKod().getId());
            pfo.setPersonId(p.getId());
            pfo.setFullName(p.getFullName());
            pfo.setDolName(p.getMainPost().getDol().getShortName());
            pfo.setTabNo(p.getTabNo());
            pfo.setDateBegin(days.getDateBegin());
            pfo.setDateEnd(days.getDateEnd());
//            long milliseconds = days.getDateEnd().getTime() - days.getDateBegin().getTime();
//            int dayCount = (int) (milliseconds / (1000 * 60 * 60 * 24)) + 1;
//            pfo.setDayCount(dayCount);

            otpusksList.add(pfo);
        }

        // Сортируем по фамилии сотрудника
        Collections.sort(otpusksList, new Comparator<PersonForOtpusk>() {
            public int compare(PersonForOtpusk p1, PersonForOtpusk p2) {
                return p1.getFullName().compareTo(p2.getFullName());
            }
        });

        return otpusksList;
    }
    //endregion




    //endregion


    //region === Праздничные дни табеля =========================================================================================

    //region === GET - Список праздничных дней за год ===========================================================================
    @GetMapping(Url.TABEL_HOLIDAYS + "/{year}")
    public List<TabelHolidaysDto> getTabelHolidays(@PathVariable int year) {
        List<TabelHolidaysDto> dtoList = new ArrayList<>();
        List<TabelHolidays> holidays = tabelService.getTabelHolidays(year);
        for (TabelHolidays th : holidays) {
            TabelHolidaysDto dto = th.toDto();
            if (th.getYear() == 0) {
                dto.setDate(Util.dateToStr(year, th.getMonth(), th.getDay())); // Заменяем нулевой год на текущий
            }
            dtoList.add(dto);
        }
        return dtoList;
    }
    //endregion


    //region === POST - Добавление праздничных дней  ============================================================================
    @PostMapping(Url.TABEL_HOLIDAYS)
    public ResponseEntity<?> createHolidays(@Valid @RequestBody TabelHolidaysDto dto) {
        TabelHolidays tabelHolidays = null;
        Calendar calendar = null;
        try {
            // Проверяем, что такого праздника еще нет в БД
            calendar = Util.strToCalendar(dto.getDate());
            TabelHolidays th = tabelService.getTabelHolidays(
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH) + 1,
                    calendar.get(Calendar.DAY_OF_MONTH)
            );
            if (th != null) {
                throw new IncorrectUserDataException("Праздничный день " + dto.getDate() + " уже есть в базе данных - " + th.getName());
            }

            // Проверяем, что рабочий день не попадает на будни и не является периодическим
            checkTabelHolidays(calendar, dto);

            tabelHolidays = tabelService.createTabelHolidays(dto);
        } catch (RuntimeException e) {
            ErrorDetail errorDetail = new ErrorDetail(HttpStatus.BAD_REQUEST, "Сохранение не удалось", e.getMessage());
            return new ResponseEntity<ErrorDetail>(errorDetail, errorDetail.getStatus());
        }

        // Если все нормально, отдаем обратно вновь созданный объект
        dto.setId(tabelHolidays.getId());
        dto.setDate(Util.dateToStr(calendar.getTime()));
        return new ResponseEntity<TabelHolidaysDto>(dto, HttpStatus.CREATED);
    }

    //endregion


    //region === PUT - Обновление праздничных дней ==============================================================================
    @PutMapping(Url.TABEL_HOLIDAYS + "/{id}")
    public ResponseEntity<?> updateHolidays(@PathVariable Long id, @Valid @RequestBody TabelHolidaysDto dto) {
        TabelHolidays tabelHolidays = null;
        try {
            // Проверяем, что праздник с таким id существует
            tabelHolidays = getAndCheckTabelHolidays(id);

            // Проверяем, что рабочий день не попадает на будни и не является периодическим
            Calendar calendar = Util.strToCalendar(dto.getDate());
            checkTabelHolidays(calendar, dto);

            // Обновляем (меняем только поля year, name и holiday)
            if (dto.isPeriodic()) {
                tabelHolidays.setYear(0);
            } else {
                tabelHolidays.setYear(calendar.get(Calendar.YEAR));
            }
            tabelHolidays.setName(dto.getName().trim());
            tabelHolidays.setHoliday(dto.isHoliday());

            tabelService.saveTabelHolidays(tabelHolidays);
        } catch (RuntimeException e) {
            ErrorDetail errorDetail = new ErrorDetail(HttpStatus.BAD_REQUEST, "Обновление не удалось", e.getMessage());
            return new ResponseEntity<ErrorDetail>(errorDetail, errorDetail.getStatus());
        }

        // Если все нормально, отдаем измененный объект обратно
        TabelHolidaysDto newDto = tabelHolidays.toDto();
        newDto.setPeriodic(dto.isPeriodic());
        newDto.setDate(dto.getDate());
        return new ResponseEntity<TabelHolidaysDto>(newDto, HttpStatus.OK);
    }
    //endregion


    //region === DELETE - Удаление праздничных дней =============================================================================
    @DeleteMapping(Url.TABEL_HOLIDAYS + "/{id}")
    public ResponseEntity<?> deleteHolidays(@PathVariable Long id) {
        TabelHolidays tabelHolidays = null;
        try {
            // Проверяем, что праздник с таким id существует
            tabelHolidays = getAndCheckTabelHolidays(id);

            tabelService.deleteTabelHolidays(tabelHolidays);
        } catch (RuntimeException e) {
            ErrorDetail errorDetail = new ErrorDetail(HttpStatus.BAD_REQUEST, "Ошибка удаления", e.getMessage());
            return new ResponseEntity<ErrorDetail>(errorDetail, errorDetail.getStatus());
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }
    //endregion

    //endregion


    //region === Условные обозначения ===========================================================================================


    //region === GET ============================================================================================================
    @GetMapping(Url.TABEL_NOTATION)
    public List<SprTabelNotation> getTabelNotation() {
        return sprService.getAllTabelNotations();
    }
    //endregion


    //region === POST ===========================================================================================================
    @PostMapping(Url.TABEL_NOTATION)
    public ResponseEntity<?> createTabelNotation(@Valid @RequestBody SprTabelNotation dto) {
        SprTabelNotation sprTabelNotation = null;
        try {
            // Проверяем, что такого условного обозначения еще нет в БД
            String kod = dto.getKod().trim();
            SprTabelNotation tn = sprService.getTabelNotationByKod(kod);
            if (tn != null) {
                throw new IncorrectUserDataException("Условное обозначение табеля с кодом " + kod + " уже есть в справочнике");
            }
            sprTabelNotation = sprService.createTabelNotation(dto);
        } catch (RuntimeException e) {
            ErrorDetail errorDetail = new ErrorDetail(HttpStatus.BAD_REQUEST, "Сохранение не удалось", e.getMessage());
            return new ResponseEntity<ErrorDetail>(errorDetail, errorDetail.getStatus());
        }

        // Если все нормально, отдаем обратно вновь созданный объект
        dto.setId(sprTabelNotation.getId());
        return new ResponseEntity<SprTabelNotation>(dto, HttpStatus.CREATED);
    }
    //endregion


    //region === PUT ============================================================================================================
    @PutMapping(Url.TABEL_NOTATION + "/{id}")
    public ResponseEntity<?> updateTabelNotation(@PathVariable Long id, @Valid @RequestBody SprTabelNotation dto) {
        SprTabelNotation tn = null;
        try {
            // Проверяем, что условное обозначение существует
            tn = getAndCheckTabelNotation(id);
            // Проверяем, что новое условное обозначение не совпадает с уже существующим
            String kod = dto.getKod().trim();
            SprTabelNotation tn2 = sprService.getTabelNotationByKod(kod);
            if ((tn2 != null) && (tn.getId() != tn2.getId())) {
                throw new IncorrectUserDataException("Условное обозначение табеля с кодом " + kod + " уже есть в справочнике");
            }

            // Обновляем
            tn.setKod(kod);
            tn.setName(dto.getName().trim());
            tn.setColor(dto.getColor());
            tn.setActive(dto.getActive());
            tn.setWorkDay(dto.getWorkDay());

            sprService.saveTabelNotation(tn);
        } catch (RuntimeException e) {
            ErrorDetail errorDetail = new ErrorDetail(HttpStatus.BAD_REQUEST, "Сохранение не удалось", e.getMessage());
            return new ResponseEntity<ErrorDetail>(errorDetail, errorDetail.getStatus());
        }

        // Если все нормально, отдаем обратно вновь созданный объект
        return new ResponseEntity<SprTabelNotation>(tn, HttpStatus.CREATED);
    }
    //endregion


    //region === DELETE =========================================================================================================
    @DeleteMapping(Url.TABEL_NOTATION + "/{id}")
    public ResponseEntity<?>deleteTabelNotation(@PathVariable Long id) {
        SprTabelNotation tn = null;
        try {
            // Проверяем, что условное обозначение существует
            tn = getAndCheckTabelNotation(id);

            sprService.deleteTabelNotation(tn);
        } catch (RuntimeException e) {
            ErrorDetail errorDetail = new ErrorDetail(HttpStatus.BAD_REQUEST, "Ошибка удаления", e.getMessage());
            return new ResponseEntity<ErrorDetail>(errorDetail, errorDetail.getStatus());
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }
    //endregion


    //endregion





    //region === ВСПОМОГАТЕЛЬНЫЕ ПРИВАТНЫЕ МЕТОДЫ ===============================================================================


    /**
     * Проверяет является ли день выходным днем
     * @param gc GregorianCalendar
     * @return true - если выходной, false  в противном случае
     */
    private final boolean isWeekend(Calendar gc) {
        return (gc.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || gc.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY);
    }


    /**
     * Проверяет является ли день праздничным днем
     * @param gc GregorianCalendar с датой проверяемого дня
     * @return Экземпляр класса TabelHolidays, если день праздничный, null - если день не праздничный
     */
    private final TabelHolidays getHoliday(Calendar gc) {
        int day = gc.get(Calendar.DAY_OF_MONTH);
        for (TabelHolidays th : tabelHolidaysList) {
            if (day == th.getDay()) {
                return th;
            }
        }
        return null;
    }


    private final SprTabelNotation getAndCheckTabelNotation(Long id) throws IncorrectUserDataException {
        SprTabelNotation tn = sprService.getTabelNotationById(id);
        if (tn == null) {
            throw new IncorrectUserDataException("Условное обозначение табеля с ID = " + id + " не найдено в справочнике!");
        }
        return tn;
    }


    /**
     * Получает код особенного дня табеля из справочника с одновременной проверкой, что он там существует и активен
      * @param id   идентификатор кода особенного дня табеля
     * @return экземпляр класса SprTabelNotation (код особенного дня)
     * @throws IncorrectUserDataException
     */
    private final SprTabelNotation getAndCheckActiveTabelNotation(Long id) throws IncorrectUserDataException{
        SprTabelNotation tn = getAndCheckTabelNotation(id);
        if (!tn.getActive()) {
            throw new IncorrectUserDataException("Особенный день задан некорректно - помечен в справочнике как неиспользуемый");
        }
        return tn;
    }


    /**
     * Проверяем, что присланный диапазон дат не пересекается с существующими в базе данных
     * @param person сотрудник, по которому осуществляется проверка
     * @param dateBegin дата начала диапазона
     * @param dateEnd дата конца диапазона
     * @throws IncorrectUserDataException
     */
    private final void checkDateRangeIntersects(Person person, Date dateBegin, Date dateEnd) throws IncorrectUserDataException {
        List<TabelSpDays> spDaysList = tabelService.getTabelSpDays(person, dateBegin, dateEnd);

        if (spDaysList.size() > 0) {
            TabelSpDays spDays = spDaysList.get(0);
            String s = spDays.getKod().getKod() + " - c " + Util.dateToStr(spDays.getDateBegin()) + " по " + Util.dateToStr(spDays.getDateEnd());
            throw new IncorrectUserDataException("Заданный диапазон пересекается с уже существующим: <br> " + s);
        }

    }


    /**
     * Проверяем, что присланный диапазон дат не пересекается с существующими в базе данных
     * @param person сотрудник, по которому осуществляется проверка
     * @param dateBegin дата начала диапазона
     * @param dateEnd дата конца диапазона
     * @throws IncorrectUserDataException
     */
    private final void checkDateRangeIntersects(Person person, Date dateBegin, Date dateEnd, Long id) throws IncorrectUserDataException {
        List<TabelSpDays> spDaysList = tabelService.getTabelSpDays(person, dateBegin, dateEnd);

        // Удаляем из полученного списка редактируемый объект (с ID = id)
        Iterator<TabelSpDays> i = spDaysList.iterator();
        while (i.hasNext()) {
            TabelSpDays tsd = i.next();
            if (tsd.getId() == id) i.remove();
        }

        if (spDaysList.size() > 0) {
            TabelSpDays spDays = spDaysList.get(0);
            String s = spDays.getKod().getKod() + " - c " + Util.dateToStr(spDays.getDateBegin()) + " по " + Util.dateToStr(spDays.getDateEnd());
            throw new IncorrectUserDataException("Заданный диапазон пересекается с уже существующим: <br> " + s);
        }


    }


    /**
     * Получаем особенные дни по идентификатору ID, с одновременной проверкой, что они существуют в БД
     * @param id
     * @return
     * @throws IncorrectUserDataException
     */
    private final TabelSpDays getAndCheckTabelSpDays(Long id) throws IncorrectUserDataException {
        TabelSpDays tabelSpDays = tabelService.getTabelSpDays(id);
        if (tabelSpDays == null) {
            throw new IncorrectUserDataException("Особенные дни с ID = " + id + " не найдены в БД !");
        }
        return tabelSpDays;
    }


    private final TabelHolidays getAndCheckTabelHolidays(Long id) throws IncorrectUserDataException {
        TabelHolidays tabelHolidays = tabelService.getTabelHolidays(id);
        if (tabelHolidays == null) {
            throw new IncorrectUserDataException("Праздничный день с ID = " + id + " не найден в БД !");
        }
        return tabelHolidays;
    }


    private final void checkTabelHolidays(Calendar calendar, TabelHolidaysDto dto) throws IncorrectUserDataException {
        // Проверяем, что рабочий день не попадает на будни
        if (!isWeekend(calendar) && !dto.isHoliday()) {
            throw new IncorrectUserDataException("Работа за праздничный день не может быть назначена в будни");
        }

        // Проверяем, что рабочий день не является периодическим
        if (dto.isPeriodic() && !dto.isHoliday()) {
            throw new IncorrectUserDataException("Работа за праздничный день не может быть ежегодной");
        }
    }



    //endregion


}
