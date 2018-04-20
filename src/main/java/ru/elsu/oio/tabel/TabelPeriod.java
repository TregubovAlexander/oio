package ru.elsu.oio.tabel;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class TabelPeriod {

    private String year;
    private String month;
    private String monthName;

    public TabelPeriod(String year, String month) {
        this.year = year;
        this.month = month;
    }

    public String getMonthName() {
        String[] monthNames = {"январь", "февраль", "март", "апрель", "май", "июнь", "июль", "август", "сентябрь", "октябрь", "ноябрь", "декабрь"};
        int m = 0;
        try {
            m = Integer.parseInt(this.month);
        } catch (NumberFormatException e) {
            return "";
        }
        return monthNames[m - 1];
    }

}
