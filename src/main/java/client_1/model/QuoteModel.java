package client_1.model;

import client_1.inc.MathTools;
import client_1.metastock.model.Quote;
import javafx.beans.property.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class QuoteModel {
    private ObjectProperty<LocalDate> date;
    private FloatProperty close;
    private FloatProperty high;
    private FloatProperty low;
    private FloatProperty open;
    private FloatProperty interest;
    private FloatProperty volume;
    private BooleanProperty valid;
//    public Quote quote;

    public QuoteModel(Quote quote){
//        this.quote = quote;

        this.date = new SimpleObjectProperty<LocalDate>(quote.date);
        this.close = new SimpleFloatProperty(MathTools.toPrice(quote.close));
        this.high = new SimpleFloatProperty(MathTools.toPrice(quote.high));
        this.low = new SimpleFloatProperty(MathTools.toPrice(quote.low));
        this.open = new SimpleFloatProperty(MathTools.toPrice(quote.open));
        this.interest = new SimpleFloatProperty(MathTools.toPrice(quote.interest));
        this.volume = new SimpleFloatProperty(MathTools.toPrice(quote.volume));
        this.valid = new SimpleBooleanProperty(quote.isValid());

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

    public float getClose() {
        return close.get();
    }

    public FloatProperty closeProperty() {
        return close;
    }

    public void setClose(float close) {
        this.close.set(MathTools.toPrice(close));
    }

    public float getHigh() {
        return high.get();
    }

    public FloatProperty highProperty() {
        return high;
    }

    public void setHigh(float high) {
        this.high.set(MathTools.toPrice(high));
    }

    public float getLow() {
        return low.get();
    }

    public FloatProperty lowProperty() {
        return low;
    }

    public void setLow(float low) {
        this.low.set(MathTools.toPrice(low));
    }

    public float getOpen() {
        return open.get();
    }

    public FloatProperty openProperty() {
        return open;
    }

    public void setOpen(float open) {
        this.open.set(MathTools.toPrice(open));
    }

    public float getInterest() {
        return interest.get();
    }

    public FloatProperty interestProperty() {
        return interest;
    }

    public void setInterest(float interest) {
        this.interest.set(MathTools.toPrice(interest));
    }

    public float getVolume() {
        return volume.get();
    }

    public FloatProperty volumeProperty() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume.set(MathTools.toPrice(volume));
    }

    public boolean isValid() {
        return valid.get();
    }

    public BooleanProperty validProperty() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid.set(valid);
    }

//    public Quote getQuote() {
//        return quote;
//    }
//
//    public void setQuote(Quote quote) {
//        this.quote = quote;
//    }
}
