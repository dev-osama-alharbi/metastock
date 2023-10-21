package client_1.metastock.core;

import client_1.inc.DateTime;
import client_1.metastock.model.FileNotFoundException_stok;
import client_1.metastock.model.Instrument;
import client_1.metastock.model.NumberDate;
import client_1.metastock.model.Quote;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@SuppressWarnings({"unchecked"})
public class Reader {
    private static final String[] monthNames = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};


    public static final float MARKER = -1.0f;

    private static final String MASTER = "MASTER";

    private static final int SIZE_OF_FLOAT = 4;

    public Instrument[] instruments;

    protected final String path;

    private final List<ErrorRecord> errorRecords;

    protected Quote[] createQuoteArray(int quoteCount) {
        if(quoteCount < 0){
            return new Quote[0];
        }
        return new Quote[quoteCount];
    }

    protected Quote createQuotee(int no, NumberDate date, float open, float high, float low, float close, float interest, float volume,float time) {

        return new Quote(no, date, open, high, low, close, interest, volume,time);
    }


    public Reader(String path, List<ErrorRecord> errorRecords,boolean isDaily) {
        this(path, false, errorRecords,isDaily);
    }


    public Reader(String path, boolean dump, List<ErrorRecord> errorRecords,boolean isDaily) {
        this.errorRecords = errorRecords;

        this.path = path;
        byte[] data = new byte[0];
        data = Parser.readFrom(new File(path + MASTER));
        if(data != null){
            int instrumentCount = (data.length / MasterFileDescriptor.MASTER_RECORD_LENGTH) - 1;
            if(instrumentCount > 0){
                instruments = new Instrument[instrumentCount];
                float perOneProgress = instrumentCount/70;
                for (int i = 0; i < instrumentCount; ++i) {
                    MasterFileRecord r = new MasterFileRecord(data, i);


                    Quote[] quotes;
                    AtomicInteger norec = new AtomicInteger(-1);
                    try {
                        quotes = importQuotes(r, norec, path,isDaily);
//                        for (Quote q:quotes) {
//                            System.out.println(DateTime.View.localDateToString(q.date) + "  "+isDaily+ "  "+quotes.length);
//                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        quotes = new Quote[0];
                    }
                    instruments[i] = new Instrument(r, quotes);
                    instruments[i].setNorec(norec.get());
                    onRecordRead(r);
                    if (dump) {
                        exportTab(instruments[i], r.fileNum, path);
                    }
                }
                try{
                    postProcess();
                }catch (NullPointerException n){
//                    System.out.println("Zzzzzzzzzzzzzz");
                }
            }
        }

    }

    public void onRecordRead(MasterFileRecord r) {
    }

    private void exportTab(Instrument instr, int index, String path) {
        File f = new File(path + "I" + index + ".txt");
        try {
            if (!f.createNewFile()) {
                throw new IOException("Cannot create file '" + f.getAbsolutePath() + "'!");
            }
            FileOutputStream fos = new FileOutputStream(f);
            PrintWriter pw = new PrintWriter(fos);

            Quote[] quotes = instr.rawQuotes;

            pw.println(instr.name + "\t" + instr.symbol);
            pw.println("Date\tO\tH\tL\tC\tI\tV");
            for (Quote q : quotes) {
                pw.println(dateToString(Date.from(q.date.atStartOfDay(ZoneId.systemDefault()).toInstant())) + "\t" + q.open + "\t" + q.high + "\t" + q.low + "\t" + q.close + "\t" + q.interest + "\t" + q.volume);
            }
            pw.close();
        } catch (Exception e) {
            throw new RuntimeException("Error processing instrument '" + instr + " index=" + index + ", from path='" + path + "'.", e);
        }
    }

    protected void postProcess() {
        int instrumentCount = instruments.length;

        Date[] wholePeriod = createTradingDays();

        int dayCount = wholePeriod.length;
        Quote[][] newQuotes = new Quote[instrumentCount][0];
        int[] dateIndex = new int[instrumentCount];
        int[] dateLimit = new int[instrumentCount];

        for (int i = 0; i < instrumentCount; ++i) {
            if (instruments[i].rawQuotes == null || instruments[i].rawQuotes.length == 0) {
                continue;
            }
            dateIndex[i] = 0;
            dateLimit[i] = instruments[i].rawQuotes.length;
            newQuotes[i] = createQuoteArray(dayCount);
        }

        for (int p = 0; p < dayCount; ++p) {

            NumberDate d = new NumberDate(wholePeriod[p]);

            for (int i = 0; i < instrumentCount; ++i) {
                Quote[] raw = instruments[i].rawQuotes;
                if (raw == null || raw.length == 0) {
                    continue;
                }
                int index = dateIndex[i];
                if (index < dateLimit[i]) {
                    Quote q = raw[index];
                    if (d.compareTo(Date.from(q.date.atStartOfDay(ZoneId.systemDefault()).toInstant())) == 0) {
                        newQuotes[i][p] = q;
                        ++dateIndex[i];
                        continue;
                    }
                }

                newQuotes[i][p] = createQuotee(p, d, MARKER, MARKER, MARKER, MARKER, MARKER, MARKER,MARKER);
            }
        }

        for (int i = 0; i < instrumentCount; ++i) {
            instruments[i].quotes = newQuotes[i];
            instruments[i].stakePerPoint = Instrument.DEFAULT_STAKE;
        }
    }

    protected Date[] createTradingDays() {
        Date earliest = null;
        Date latest = null;
        Date[] wholePeriod;

        int instrumentCount = instruments.length;

        for (Instrument instrument : instruments) {
            Quote[] raw = instrument.rawQuotes;
            if (raw == null || raw.length == 0) {
                continue;
            }
            Date first = Date.from(raw[0].date.atStartOfDay(ZoneId.systemDefault()).toInstant());
            Date last = Date.from(raw[raw.length - 1].date.atStartOfDay(ZoneId.systemDefault()).toInstant());

            if ((earliest == null) || (earliest.compareTo(first) > 0))
                earliest = first;

            if ((latest == null) || (latest.compareTo(last) < 0))
                latest = last;
        }


        Vector<Object> dates = new Vector<Object>();
        Calendar c = Calendar.getInstance();

            c.clear();
            c.setTime((Date) earliest.clone());

            do {

                int weekDay = c.get(Calendar.DAY_OF_WEEK);
                if ((weekDay != Calendar.SATURDAY) && (weekDay != Calendar.SUNDAY)) {
                    dates.add(c.getTime().clone());
                }
                c.add(Calendar.DATE, 1);

            } while (c.getTime().compareTo(latest) <= 0);

            wholePeriod = dates.toArray(new Date[dates.size()]);
            return wholePeriod;



    }

    @SuppressWarnings("finally")
    private Quote[] importQuotes(MasterFileRecord r, AtomicInteger norec, String dataPath,boolean isDays) {

        Quote[] quotes = null;

        try {
            File in = new File(dataPath + r.getFileName());
            if (!in.exists()) {
                errorRecords.add(ErrorRecord.warn("Data file (" + r.getFileName() + ") for " + r.getSymbol() + " is missing."));
                return new Quote[0];
            }
            byte[] data = Parser.readFrom(in);
            if(data == null){
                return new Quote[0];
            }
            int length = data.length;
            int quoteCount = (length / r.recordLength) - 1;
            int quoteIndex = 0;
            float[] row = new float[Math.min(r.recordCount, 7)];

            if (row.length < 7) {
                return new Quote[0];
            }

            quotes = createQuoteArray(quoteCount);

            try {
                norec.set(Parser.readInt(data, 2));
            }catch (ArrayIndexOutOfBoundsException e){
                return new Quote[0];
            }
            try {
                for (int i = r.recordLength; i < length; i += r.recordLength, ++quoteIndex) {
                    for (int col = 0; col < r.recordCount; ++col) {
                        if(col >= 7) {
                            continue;
                        }
                        row[col] = Parser.readMicrosoftBASICfloat(data, i + (col * SIZE_OF_FLOAT));
                    }
                    for (int col = r.recordCount; col < row.length; ++col) {
                        row[col] = 0.0f;
                    }
                    byte[] dbuf = Arrays.copyOfRange(data, i, i + SIZE_OF_FLOAT);
                    NumberDate nd = new NumberDate(row[0]);
                    nd.setBytes(dbuf);

//                Quote q = null;
//                if(row[7] != null){
//
//                }
                    Quote q = createQuotee(quoteIndex, nd, row[1], row[2], row[3], row[4], row[5], row[6],0);

//                q = createQuotee(quoteIndex, nd, row[1], row[2], row[3], row[4], row[5], row[6],row[7]);
                    quotes[quoteIndex] = q;
                }
            }catch (ArrayIndexOutOfBoundsException e){
//                System.out.println("WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW");
                return new Quote[0];
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return quotes;
    }

    public int getInstrumentCount() {
        return instruments.length;
    }

    @SuppressWarnings("rawtypes")
    public Enumeration<Instrument> getInstruments() {
        return new Enumeration() {
            int index = 0;

            public boolean hasMoreElements() {
                return index < instruments.length;
            }

            public Object nextElement() {
                return instruments[index++];
            }
        };
    }

    public Instrument getInstrument(int index) {
        return instruments[index];
    }


    private static String dateToString(Date d) {
        StringBuilder sb = new StringBuilder();
        Calendar c = Calendar.getInstance();
        c.clear();
        c.setTime(d);
        sb.append(Integer.toString(c.get(Calendar.DATE)));
        sb.append("-");
        sb.append(monthNames[c.get(Calendar.MONTH)]);
        sb.append("-");
        //	sb.append(Integer.toString(c.get(Calendar.YEAR)+1900)) ;
        sb.append(Integer.toString(c.get(Calendar.YEAR)));
        return sb.toString();
    }
}