package client_1.model;

import client_1.inc.MathTools;
import client_1.metastock.model.Quote;
import javafx.beans.property.*;
import javafx.scene.Parent;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.util.HashMap;

public class RowModel {
    private StringProperty ticker;
    private FloatProperty refrishClose;
    private FloatProperty dataClose;
    private FloatProperty n;
    private FloatProperty u2;
    private FloatProperty u1;
    private FloatProperty aU;
    private FloatProperty dataH;
    private FloatProperty o;
    private FloatProperty m;
    private FloatProperty e;
    private FloatProperty dataL;
    private FloatProperty aD;
    private FloatProperty d1;
    private FloatProperty d2;
    private ObjectProperty<LocalDate> localDate;
    private IntegerProperty days;
    private InstrumentModel instrumentModel;

    public RowModel(InstrumentModel instrumentModel){
        this.instrumentModel = instrumentModel;
        this.ticker = new SimpleStringProperty("");
        this.localDate = new SimpleObjectProperty<>(null);
        this.days = new SimpleIntegerProperty(0);
        this.refrishClose = new SimpleFloatProperty(MathTools.toPrice(0));
        this.dataClose = new SimpleFloatProperty(MathTools.toPrice(0));
        this.n = new SimpleFloatProperty(MathTools.toPrice(0));
        this.u2 = new SimpleFloatProperty(MathTools.toPrice(0));
        this.u1 = new SimpleFloatProperty(MathTools.toPrice(0));
        this.aU = new SimpleFloatProperty(MathTools.toPrice(0));
        this.dataH = new SimpleFloatProperty(MathTools.toPrice(0));
        this.o = new SimpleFloatProperty(MathTools.toPrice(0));
        this.m = new SimpleFloatProperty(MathTools.toPrice(0));
        this.e = new SimpleFloatProperty(MathTools.toPrice(0));
        this.dataL = new SimpleFloatProperty(MathTools.toPrice(0));
        this.aD = new SimpleFloatProperty(MathTools.toPrice(0));
        this.d1 = new SimpleFloatProperty(MathTools.toPrice(0));
        this.d2 = new SimpleFloatProperty(MathTools.toPrice(0));

        // M = (dataH + dataL)/2
        this.m.bind(this.dataH.add(this.dataL).divide(2));

        // O = (dataH + M)/2
        this.o.bind(this.dataH.add(this.m).divide(2));

        // E = (M + dataL)/2
        this.e.bind(this.m.add(this.dataL).divide(2));

        // N = (dataH - dataL)/4
        this.n.bind(this.dataH.subtract(this.dataL).divide(4));

        // U1 = dataH + (N * 2.5)
        this.u1.bind(this.dataH.add(this.n.multiply(2.5)));

        // AU = (dataH + U1)/2
        this.aU.bind(this.dataH.add(this.u1).divide(2));

        // U2 = dataH + (N * 5.00)
        this.u2.bind(this.dataH.add(this.n.multiply(5.00)));

        // D1 = dataL - (N * 2.5)
        this.d1.bind(this.dataL.subtract(this.n.multiply(2.5)));

        // AD = (dataL + D1)/2
        this.aD.bind(this.dataL.add(this.d1).divide(2));

        // D2 = dataL - (N * 5.00)
        this.d2.bind(this.dataL.subtract(this.n.multiply(5.00)));


    }

    public RowModel(String symbol,float dataclose){

        this.instrumentModel = instrumentModel;
        this.ticker = new SimpleStringProperty("");
        this.localDate = new SimpleObjectProperty<>(null);
        this.days = new SimpleIntegerProperty(0);
        this.refrishClose = new SimpleFloatProperty(MathTools.toPrice(0));
        this.dataClose = new SimpleFloatProperty(MathTools.toPrice(0));
        this.n = new SimpleFloatProperty(MathTools.toPrice(0));
        this.u2 = new SimpleFloatProperty(MathTools.toPrice(0));
        this.u1 = new SimpleFloatProperty(MathTools.toPrice(0));
        this.aU = new SimpleFloatProperty(MathTools.toPrice(0));
        this.dataH = new SimpleFloatProperty(MathTools.toPrice(0));
        this.o = new SimpleFloatProperty(MathTools.toPrice(0));
        this.m = new SimpleFloatProperty(MathTools.toPrice(0));
        this.e = new SimpleFloatProperty(MathTools.toPrice(0));
        this.dataL = new SimpleFloatProperty(MathTools.toPrice(0));
        this.aD = new SimpleFloatProperty(MathTools.toPrice(0));
        this.d1 = new SimpleFloatProperty(MathTools.toPrice(0));
        this.d2 = new SimpleFloatProperty(MathTools.toPrice(0));

        // M = (dataH + dataL)/2
        this.m.bind(this.dataH.add(this.dataL).divide(2));

        // O = (dataH + M)/2
        this.o.bind(this.dataH.add(this.m).divide(2));

        // E = (M + dataL)/2
        this.e.bind(this.m.add(this.dataL).divide(2));

        // N = (dataH - dataL)/4
        this.n.bind(this.dataH.subtract(this.dataL).divide(4));

        // U1 = dataH + (N * 2.5)
        this.u1.bind(this.dataH.add(this.n.multiply(2.5)));

        // AU = (dataH + U1)/2
        this.aU.bind(this.dataH.add(this.u1).divide(2));

        // U2 = dataH + (N * 5.00)
        this.u2.bind(this.dataH.add(this.n.multiply(5.00)));

        // D1 = dataL - (N * 2.5)
        this.d1.bind(this.dataL.subtract(this.n.multiply(2.5)));

        // AD = (dataL + D1)/2
        this.aD.bind(this.dataL.add(this.d1).divide(2));

        // D2 = dataL - (N * 5.00)
        this.d2.bind(this.dataL.subtract(this.n.multiply(5.00)));

        this.setDataH(0.0f);
        this.setDataL(0.0f);
        this.setDataClose(dataclose);
        this.setTicker(symbol);

    }


    public RowModel initDaily(LocalDate from,LocalDate to) {
//        long betweenNumOfDays = 0;
        float tmpMaxDataH = 0;
        float tmpMaxDataL = 0;
        float tmpClose = 0;

//        LocalDate to = from.minusDays(numOfDays);
//        LocalDate from_p1 = from.plusDays(1);
//        Duration between = Duration.between(from.atStartOfDay(),from.minusDays(numOfDays).atStartOfDay());
        LocalDate lastWorkDay = to;
        int tmp = 1;
        while (lastWorkDay.getDayOfWeek().equals(DayOfWeek.FRIDAY) || lastWorkDay.getDayOfWeek().equals(DayOfWeek.SATURDAY)){
            lastWorkDay = to.minusDays(tmp);
            tmp++;
        }

//        for (QuoteModel q:this.instrumentModel.getRawQuotes()) {
////        for (QuoteModel q:this.instrumentModel.getQuotes()) {
////            betweenNumOfDays = Duration.between(from.atStartOfDay(), q.getDate().atStartOfDay()).toDays();
//            if((q.getDate().isBefore(to) && q.getDate().isAfter(from)) || q.getDate().isEqual(from) || q.getDate().isEqual(to)){
//                //tmpMaxDataH
//                if(tmpMaxDataH < q.getHigh()){
//                    tmpMaxDataH = q.getHigh();
//                }
//                //tmpMaxDataL
//                if(tmpMaxDataL < q.getLow()){
//                    tmpMaxDataL = q.getLow();
//                }
//                if(lastWorkDay.isEqual(q.getDate()) || lastWorkDay.isAfter(q.getDate())){
//                    tmpClose = q.getClose();
//                }
//            }
//        }

        this.setDataH(tmpMaxDataH);
        this.setDataL(tmpMaxDataL);
        this.setDataClose(tmpClose);
        this.setTicker(this.instrumentModel.getSymbol());

        return this;
    }

    public RowModel initDaily_v2(LocalDate from,LocalDate to) {
//        long betweenNumOfDays = 0;
        float tmpMaxDataH = Float.MIN_VALUE;
        float tmpMaxDataL = Float.MAX_VALUE;
        float tmpClose = 0;

//        LocalDate to = from.minusDays(numOfDays);
//        LocalDate from_p1 = from.plusDays(1);
//        Duration between = Duration.between(from.atStartOfDay(),from.minusDays(numOfDays).atStartOfDay());
        LocalDate lastWorkDay = to;
        int tmp = 1;
        while (lastWorkDay.getDayOfWeek().equals(DayOfWeek.FRIDAY) || lastWorkDay.getDayOfWeek().equals(DayOfWeek.SATURDAY)){
            lastWorkDay = to.minusDays(tmp);
            tmp++;
        }

        for (Quote q:this.instrumentModel.instrument.rawQuotes) {
            if((q.date.isBefore(to) && q.date.isAfter(from)) || q.date.isEqual(from) || q.date.isEqual(to)){
                //tmpMaxDataH
                if(tmpMaxDataH < q.high){
                    tmpMaxDataH = q.high;
                }
                //tmpMaxDataL
                if(tmpMaxDataL >= q.low){
                    tmpMaxDataL = q.low;
                }
                if(lastWorkDay.isEqual(q.date) || lastWorkDay.isAfter(q.date)){
                    tmpClose = q.close;
                }
            }
        }


//        for (QuoteModel q:this.instrumentModel.getRawQuotes()) {
////        for (QuoteModel q:this.instrumentModel.getQuotes()) {
////            betweenNumOfDays = Duration.between(from.atStartOfDay(), q.getDate().atStartOfDay()).toDays();
//            if((q.getDate().isBefore(to) && q.getDate().isAfter(from)) || q.getDate().isEqual(from) || q.getDate().isEqual(to)){
//                //tmpMaxDataH
//                if(tmpMaxDataH < q.getHigh()){
//                    tmpMaxDataH = q.getHigh();
//                }
//                //tmpMaxDataL
//                if(tmpMaxDataL < q.getLow()){
//                    tmpMaxDataL = q.getLow();
//                }
//                if(lastWorkDay.isEqual(q.getDate()) || lastWorkDay.isAfter(q.getDate())){
//                    tmpClose = q.getClose();
//                }
//            }
//        }


        if(tmpMaxDataH == Float.MIN_VALUE){
            tmpMaxDataH = 0;
        }
        if(tmpMaxDataL == Float.MAX_VALUE){
            tmpMaxDataL = 0;
        }

        this.setDataH(tmpMaxDataH);
        this.setDataL(tmpMaxDataL);
        this.setDataClose(tmpClose);
        this.setTicker(this.instrumentModel.getSymbol());

        return this;
    }


    public boolean initIntraday_v2(QuoteModel quoteModel){
        if(quoteModel != null){
            setRefrishClose(quoteModel.getInterest());
            return true;
        }else{
            return false;
        }
    }


    public String getTicker() {
        return ticker.get();
    }

    public StringProperty tickerProperty() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker.set(ticker);
    }

    public float getRefrishClose() {
        return refrishClose.get();
    }

    public FloatProperty refrishCloseProperty() {
        return refrishClose;
    }

    public void setRefrishClose(float refrishClose) {
        this.refrishClose.set(MathTools.toPrice(refrishClose));
    }

    public float getDataClose() {
        return dataClose.get();
    }

    public FloatProperty dataCloseProperty() {
        return dataClose;
    }

    public void setDataClose(float dataClose) {
        this.dataClose.set(MathTools.toPrice(dataClose));
    }

    public float getN() {
        return n.get();
    }

    public FloatProperty nProperty() {
        return n;
    }

    public void setN(float n) {
        this.n.set(MathTools.toPrice(n));
    }

    public float getU2() {
        return u2.get();
    }

    public FloatProperty u2Property() {
        return u2;
    }

    public void setU2(float u2) {
        this.u2.set(MathTools.toPrice(u2));
    }

    public float getU1() {
        return u1.get();
    }

    public FloatProperty u1Property() {
        return u1;
    }

    public void setU1(float u1) {
        this.u1.set(MathTools.toPrice(u1));
    }

    public float getaU() {
        return aU.get();
    }

    public FloatProperty aUProperty() {
        return aU;
    }

    public void setaU(float aU) {
        this.aU.set(MathTools.toPrice(aU));
    }

    public float getDataH() {
        return dataH.get();
    }

    public FloatProperty dataHProperty() {
        return dataH;
    }

    public void setDataH(float dataH) {
        this.dataH.set(MathTools.toPrice(dataH));
    }

    public float getO() {
        return o.get();
    }

    public FloatProperty oProperty() {
        return o;
    }

    public void setO(float o) {
        this.o.set(MathTools.toPrice(o));
    }

    public float getM() {
        return m.get();
    }

    public FloatProperty mProperty() {
        return m;
    }

    public void setM(float m) {
        this.m.set(MathTools.toPrice(m));
    }

    public float getE() {
        return e.get();
    }

    public FloatProperty eProperty() {
        return e;
    }

    public void setE(float e) {
        this.e.set(MathTools.toPrice(e));
    }

    public float getDataL() {
        return dataL.get();
    }

    public FloatProperty dataLProperty() {
        return dataL;
    }

    public void setDataL(float dataL) {
        this.dataL.set(MathTools.toPrice(dataL));
    }

    public float getaD() {
        return aD.get();
    }

    public FloatProperty aDProperty() {
        return aD;
    }

    public void setaD(float aD) {
        this.aD.set(MathTools.toPrice(aD));
    }

    public float getD1() {
        return d1.get();
    }

    public FloatProperty d1Property() {
        return d1;
    }

    public void setD1(float d1) {
        this.d1.set(MathTools.toPrice(d1));
    }

    public float getD2() {
        return d2.get();
    }

    public FloatProperty d2Property() {
        return d2;
    }

    public void setD2(float d2) {
        this.d2.set(MathTools.toPrice(d2));
    }

    public LocalDate getLocalDate() {
        return localDate.get();
    }

    public ObjectProperty<LocalDate> localDateProperty() {
        return localDate;
    }

    public void setLocalDate(LocalDate localDate) {
        this.localDate.set(localDate);
    }

    public int getDays() {
        return days.get();
    }

    public IntegerProperty daysProperty() {
        return days;
    }

    public void setDays(int days) {
        this.days.set(days);
    }

    @Override
    public String toString() {
        return "RowModel{" +
                "ticker=" + ticker.get() +
                ", refrishClose=" + refrishClose.get() +
                ", dataClose=" + dataClose.get() +
                ", n=" + n.get() +
                ", u2=" + u2.get() +
                ", u1=" + u1.get() +
                ", aU=" + aU.get() +
                ", dataH=" + dataH.get() +
                ", o=" + o.get() +
                ", m=" + m.get() +
                ", e=" + e.get() +
                ", dataL=" + dataL.get() +
                ", aD=" + aD.get() +
                ", d1=" + d1.get() +
                ", d2=" + d2.get() +
                ", localDate=" + localDate.get() +
                ", days=" + days.get() +
                '}';
    }
}
