package client_1.model;

import client_1.inc.DateTime;
import client_1.metastock.core.Reader;
import client_1.metastock.model.FileNotFoundException_stok;
import client_1.metastock.model.Instrument;
import client_1.metastock.model.Quote;
import com.jfoenix.controls.JFXProgressBar;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;

public class WorkGroupModel {
    private StringProperty path;
    private ObservableList<InstrumentModel> instrumentModel;
//    private Reader reader;
//    private JFXProgressBar progLoad;

    public static WorkGroupModel genereate(String path,JFXProgressBar progLoad,boolean isDaily) {
        WorkGroupModel w = new WorkGroupModel(path,progLoad,isDaily);
        if(w.instrumentModel.size() == 0){
            return null;
        }
        return w;
    }

    private WorkGroupModel(String path,JFXProgressBar progLoad,boolean isDaily) {
//        this.progLoad = progLoad;
        this.path = new SimpleStringProperty(path);
        this.instrumentModel = FXCollections.observableArrayList();
        Reader reader = null;
        reader = new Reader(this.path.get(),new ArrayList<>(),isDaily);
        if(reader.instruments != null){
            Enumeration<Instrument> instruments = reader.getInstruments();
            float perOneProgress = reader.getInstrumentCount()/70;
            int i = 1;

            while (instruments.hasMoreElements()){
                Instrument in = instruments.nextElement();
//            if(in.symbol.equals("8040")){
//                for (Quote q: in.rawQuotes) {
//                }
//            }
//                LocalDateTime ldtF = LocalDateTime.ofInstant(in.getMasterFileRecord().getFirstDate().toInstant(), ZoneId.systemDefault());
//                LocalDateTime ldtL = LocalDateTime.ofInstant(in.getMasterFileRecord().getLastDate().toInstant(), ZoneId.systemDefault());
//
//                System.out.println(DateTime.View.localDateToStringTime_HMS(ldtF)+"  "+DateTime.View.localDateToStringTime_HMS(ldtL));

                this.instrumentModel.add(new InstrumentModel(in));
                progLoad.setProgress(((i/perOneProgress)/100)+0.1);
                i++;
            }
        }
    }

    public HashMap<String,QuoteModel> getLastQuoteMap(){
        HashMap<String,QuoteModel> arr = new HashMap<>();

        for (InstrumentModel i :instrumentModel) {
            Quote tmpQuote = null;
            float open = 0;
            for (Quote q:i.instrument.rawQuotes) {
                if(tmpQuote == null){
                    tmpQuote = q;
                    open = q.open;
                }else if(q.date.isAfter(tmpQuote.date)){
                    tmpQuote = q;
                    open = q.open;
                }else if(q.date.equals(tmpQuote.date)){
                    if(open <= q.open){
                        tmpQuote = q;
                        open = q.open;
                    }
                }
            }

            if(tmpQuote != null){
//                QuoteModel lastQuoteModel = new QuoteModel(tmpQuote);


//                System.out.println(i.getSymbol()+"  "+DateTime.View.localDateToString(tmpQuote.date));
                arr.put(i.getSymbol(),new QuoteModel(tmpQuote));
            }else {
                arr.put(i.getSymbol(),null);
            }
        }

        return arr;
    }

    public String getPath() {
        return path.get();
    }

    public StringProperty pathProperty() {
        return path;
    }

    public void setPath(String path) {
        this.path.set(path);
    }

    public ObservableList<InstrumentModel> getInstrumentModel() {
        return instrumentModel;
    }

    public void setInstrumentModel(ObservableList<InstrumentModel> instrumentModel) {
        this.instrumentModel = instrumentModel;
    }

//    public Reader getReader() {
//        return reader;
//    }
//
//    public void setReader(Reader reader) {
//        this.reader = reader;
//    }
}
