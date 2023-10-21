package client_1.model;

import javafx.beans.property.*;

import java.time.LocalDate;

public class SettingModel {
    private StringProperty Daily;
    private StringProperty Intraday;
    private ObjectProperty<LocalDate> date;
    private IntegerProperty numOfLastDays;

    public SettingModel(String daily, String intraday,LocalDate date,int numOfLastDays) {
        this.Daily = new SimpleStringProperty(daily);
        this.Intraday = new SimpleStringProperty(intraday);
        this.date = new SimpleObjectProperty<>(date);
        this.numOfLastDays = new SimpleIntegerProperty(numOfLastDays);
    }

    public String getDaily() {
        return Daily.get();
    }

    public StringProperty dailyProperty() {
        return Daily;
    }

    public void setDaily(String daily) {
        this.Daily.set(daily);
    }

    public String getIntraday() {
        return Intraday.get();
    }

    public StringProperty intradayProperty() {
        return Intraday;
    }

    public void setIntraday(String intraday) {
        this.Intraday.set(intraday);
    }

    public LocalDate getDate() {
        return date.get();
    }

    public ObjectProperty<LocalDate> dateProperty() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date.set(date);
    }

    public int getNumOfLastDays() {
        return numOfLastDays.get();
    }

    public IntegerProperty numOfLastDaysProperty() {
        return numOfLastDays;
    }

    public void setNumOfLastDays(int numOfLastDays) {
        this.numOfLastDays.set(numOfLastDays);
    }
}
