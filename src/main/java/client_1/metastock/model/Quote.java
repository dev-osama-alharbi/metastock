package client_1.metastock.model;


import java.time.LocalDate;
import java.time.ZoneId;

/**
 * Encapsulates that date, open, high, low and close value for a given
 * instrument on that particular date.
 *
 * @see Instrument
 */
public class Quote {
    private final int no;
    /**
     * The date corresponding to the OHLC fields below
     */
    public final LocalDate date;

    /**
     * The O of OHLC
     */
    public final float open;

    public final float time;

    /**
     * The H of OHLC
     */
    public final float high;

    /**
     * The L of OHLC
     */
    public final float low;

    /**
     * The C of OHLC
     */
    public final float close;

    /**
     * The Open Interest
     */
    public final float interest;

    /**
     * The volume
     */
    public final float volume;

    /**
     * Default constructor.
     */
    public Quote(int no,
                 NumberDate date
            , float open
            , float high
            , float low
            , float close
            , float interest
            , float volume,
                 float time) {

        this.no = no;
        this.date = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.interest = interest;
        this.volume = volume;
        this.time = time;
    }

    /**
     * Copy constructor.
     */
    public Quote(int no, Quote other) {
        this.no = no;
        this.date = other.date;
        this.open = other.open;
        this.high = other.high;
        this.low = other.low;
        this.close = other.close;
        this.interest = other.interest;
        this.volume = other.volume;
        this.time = other.time;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof Quote) {
            Quote q = (Quote) obj;
            return q.no == no && q.close == close && q.date.equals(date) && q.high == high && q.interest == interest && q.low == low && q.open == open && q.volume == volume;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return date == null ? 0 : date.hashCode();
    }

    /**
     * Just so that dumps are a bit more menaingful.
     */
    public String toString() {
        return "Date: " + date +
                ", open:" + open +
                ", high:" + high +
                ", low:" + low +
                ", close:" + close +
                ", interest:" + interest +
                ", volume:" + volume;
    }

    public boolean isValid() {
        if (close < 0) return false;
        if (high < 0) return false;
        if (low < 0) return false;
        if (open < 0) return false;
//		if (interest < 0) return false;
//		if (volume < 0) return false;
        return true;
    }
}
