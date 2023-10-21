package client_1.model;

import client_1.inc.MathTools;
import client_1.metastock.model.Instrument;
import client_1.metastock.model.Quote;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class InstrumentModel {
    private StringProperty fileName;
    private IntegerProperty fileNum;
    private ObjectProperty<Character> fileType;
    private ObjectProperty<LocalDate> firstDate;
    private ObjectProperty<LocalDate> lastDate;
    private StringProperty issueName;
    private StringProperty symbol;
    private ObjectProperty<Character> timeFrame;
    private IntegerProperty idaTime;
    private IntegerProperty recordCount;
    private IntegerProperty recordLength;
    private BooleanProperty autorun;
    private BooleanProperty v28;
    private FloatProperty stakePerPoint;
    private BooleanProperty inUse;
    private IntegerProperty numberOfRecords;
    private IntegerProperty validQuotas;
//    private ObservableList<QuoteModel> rawQuotes;
//    private IntegerProperty rawQuotesLength;
//    private ObservableList<QuoteModel> quotes;
//    private IntegerProperty quotesLength;
    public Instrument instrument;

    public InstrumentModel(Instrument instrument){
        this.instrument = instrument;

        this.fileName = new SimpleStringProperty(instrument.getMasterFileRecord().getFileName());
        this.fileNum = new SimpleIntegerProperty(instrument.getMasterFileRecord().getFileNum());
        this.fileType = new SimpleObjectProperty<Character>(instrument.getMasterFileRecord().getFileType());
        this.firstDate = new SimpleObjectProperty<LocalDate>(LocalDateTime.ofInstant(instrument.getMasterFileRecord().getFirstDate().toInstant(), ZoneId.systemDefault()).toLocalDate());
        this.lastDate = new SimpleObjectProperty<LocalDate>(LocalDateTime.ofInstant(instrument.getMasterFileRecord().getLastDate().toInstant(), ZoneId.systemDefault()).toLocalDate());
        this.issueName = new SimpleStringProperty(instrument.getMasterFileRecord().getIssueName());
        this.symbol = new SimpleStringProperty(instrument.getMasterFileRecord().getSymbol());
        this.timeFrame = new SimpleObjectProperty<Character>(instrument.getMasterFileRecord().getTimeFrame());
        this.idaTime = new SimpleIntegerProperty(instrument.getMasterFileRecord().getIdaTime());
        this.recordCount = new SimpleIntegerProperty(instrument.getMasterFileRecord().getRecordCount());
        this.recordLength = new SimpleIntegerProperty(instrument.getMasterFileRecord().getRecordLength());
        this.autorun = new SimpleBooleanProperty(instrument.getMasterFileRecord().isAutorun());
        this.v28 = new SimpleBooleanProperty(instrument.getMasterFileRecord().isV28());
        this.stakePerPoint = new SimpleFloatProperty(MathTools.toPrice(instrument.stakePerPoint));
        this.inUse = new SimpleBooleanProperty(instrument.inUse);
        this.numberOfRecords = new SimpleIntegerProperty(instrument.getNumberOfRecords());
        this.validQuotas = new SimpleIntegerProperty(instrument.getValidQuotas());

//        this.rawQuotes = FXCollections.observableArrayList();
//        this.rawQuotesLength = new SimpleIntegerProperty(0);
//        this.rawQuotes.addListener(new ListChangeListener<QuoteModel>() {
//            @Override
//            public void onChanged(Change<? extends QuoteModel> c) {
//                rawQuotesLength.set(rawQuotes.size());
//            }
//        });
//        for (Quote q:instrument.rawQuotes) {
//            this.rawQuotes.add(new QuoteModel(q));
//        }
//
//        this.quotes = FXCollections.observableArrayList();
//        this.quotesLength = new SimpleIntegerProperty(0);
//        this.quotes.addListener(new ListChangeListener<QuoteModel>() {
//            @Override
//            public void onChanged(Change<? extends QuoteModel> c) {
//                quotesLength.set(quotes.size());
//            }
//        });
//        for (Quote q:instrument.quotes) {
//            this.quotes.add(new QuoteModel(q));
//        }

    }

    public String getFileName() {
        return fileName.get();
    }

    public StringProperty fileNameProperty() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName.set(fileName);
    }

    public int getFileNum() {
        return fileNum.get();
    }

    public IntegerProperty fileNumProperty() {
        return fileNum;
    }

    public void setFileNum(int fileNum) {
        this.fileNum.set(fileNum);
    }

    public Character getFileType() {
        return fileType.get();
    }

    public ObjectProperty<Character> fileTypeProperty() {
        return fileType;
    }

    public void setFileType(Character fileType) {
        this.fileType.set(fileType);
    }

    public LocalDate getFirstDate() {
        return firstDate.get();
    }

    public ObjectProperty<LocalDate> firstDateProperty() {
        return firstDate;
    }

    public void setFirstDate(LocalDate firstDate) {
        this.firstDate.set(firstDate);
    }

    public LocalDate getLastDate() {
        return lastDate.get();
    }

    public ObjectProperty<LocalDate> lastDateProperty() {
        return lastDate;
    }

    public void setLastDate(LocalDate lastDate) {
        this.lastDate.set(lastDate);
    }

    public String getIssueName() {
        return issueName.get();
    }

    public StringProperty issueNameProperty() {
        return issueName;
    }

    public void setIssueName(String issueName) {
        this.issueName.set(issueName);
    }

    public String getSymbol() {
        return symbol.get();
    }

    public StringProperty symbolProperty() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol.set(symbol);
    }

    public Character getTimeFrame() {
        return timeFrame.get();
    }

    public ObjectProperty<Character> timeFrameProperty() {
        return timeFrame;
    }

    public void setTimeFrame(Character timeFrame) {
        this.timeFrame.set(timeFrame);
    }

    public int getIdaTime() {
        return idaTime.get();
    }

    public IntegerProperty idaTimeProperty() {
        return idaTime;
    }

    public void setIdaTime(int idaTime) {
        this.idaTime.set(idaTime);
    }

    public int getRecordCount() {
        return recordCount.get();
    }

    public IntegerProperty recordCountProperty() {
        return recordCount;
    }

    public void setRecordCount(int recordCount) {
        this.recordCount.set(recordCount);
    }

    public int getRecordLength() {
        return recordLength.get();
    }

    public IntegerProperty recordLengthProperty() {
        return recordLength;
    }

    public void setRecordLength(int recordLength) {
        this.recordLength.set(recordLength);
    }

    public boolean isAutorun() {
        return autorun.get();
    }

    public BooleanProperty autorunProperty() {
        return autorun;
    }

    public void setAutorun(boolean autorun) {
        this.autorun.set(autorun);
    }

    public boolean isV28() {
        return v28.get();
    }

    public BooleanProperty v28Property() {
        return v28;
    }

    public void setV28(boolean v28) {
        this.v28.set(v28);
    }

    public float getStakePerPoint() {
        return stakePerPoint.get();
    }

    public FloatProperty stakePerPointProperty() {
        return stakePerPoint;
    }

    public void setStakePerPoint(float stakePerPoint) {
        this.stakePerPoint.set(MathTools.toPrice(stakePerPoint));
    }

    public boolean isInUse() {
        return inUse.get();
    }

    public BooleanProperty inUseProperty() {
        return inUse;
    }

    public void setInUse(boolean inUse) {
        this.inUse.set(inUse);
    }

    public int getNumberOfRecords() {
        return numberOfRecords.get();
    }

    public IntegerProperty numberOfRecordsProperty() {
        return numberOfRecords;
    }

    public void setNumberOfRecords(int numberOfRecords) {
        this.numberOfRecords.set(numberOfRecords);
    }

    public int getValidQuotas() {
        return validQuotas.get();
    }

    public IntegerProperty validQuotasProperty() {
        return validQuotas;
    }

    public void setValidQuotas(int validQuotas) {
        this.validQuotas.set(validQuotas);
    }

//    public ObservableList<QuoteModel> getRawQuotes() {
//        return rawQuotes;
//    }
//
//    public void setRawQuotes(ObservableList<QuoteModel> rawQuotes) {
//        this.rawQuotes = rawQuotes;
//    }

//    public ObservableList<QuoteModel> getQuotes() {
//        return quotes;
//    }
//
//    public void setQuotes(ObservableList<QuoteModel> quotes) {
//        this.quotes = quotes;
//    }

//    public Instrument getInstrument() {
//        return instrument;
//    }
//
//    public void setInstrument(Instrument instrument) {
//        this.instrument = instrument;
//    }

//    public int getRawQuotesLength() {
//        return rawQuotesLength.get();
//    }
//
//    public IntegerProperty rawQuotesLengthProperty() {
//        return rawQuotesLength;
//    }
//
//    public void setRawQuotesLength(int rawQuotesLength) {
//        this.rawQuotesLength.set(rawQuotesLength);
//    }

//    public int getQuotesLength() {
//        return quotesLength.get();
//    }
//
//    public IntegerProperty quotesLengthProperty() {
//        return quotesLength;
//    }
//
//    public void setQuotesLength(int quotesLength) {
//        this.quotesLength.set(quotesLength);
//    }
}
