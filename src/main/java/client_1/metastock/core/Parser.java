package client_1.metastock.core;

import client_1.metastock.model.FileNotFoundException_stok;
import client_1.metastock.model.NumberDate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Calendar;


public final class Parser {

    private Parser() {
    }

    public static float
    readMicrosoftBASICfloat(byte[] buffer, int offset) throws ArrayIndexOutOfBoundsException{

        int b0 = (buffer[offset++] & 0x0ff);
        int b1 = (buffer[offset++] & 0x0ff);
        int b2 = (buffer[offset++] & 0x0ff);
        int b3 = (buffer[offset] & 0x0ff);

        // Now, this is big endian:

        int msf = b3 << 24 | b2 << 16 | b1 << 8 | b0;

        int val = (msf & 0x007fffff);
        int exp = ((msf >> 24) & 0x0ff) - 2;
        int sig = (msf >> 16) & 0x080;

        val |= exp << 23;
        val |= sig << 24;

        return Float.intBitsToFloat(val);
    }


    static NumberDate readDate(byte[] buffer, int offset) {
        float f = readMicrosoftBASICfloat(buffer, offset);
        NumberDate d = new NumberDate(f);
        byte[] buf = Arrays.copyOfRange(buffer, offset, offset + 4);
        d.setBytes(buf);
        return d;
    }


    public static NumberDate readDate(int integer) {
        int date = integer;
        int day = date % 100;
        date /= 100;
        int month = date % 100;
        date /= 100;
        int year = 1900 + date;

        Calendar c = Calendar.getInstance();
        c.clear();
        c.set(year, month - 1, day);
        return new NumberDate(c.getTime(), integer);
    }


    static byte
    readByte(byte[] buffer, int offset) {
        return buffer[offset];
    }


    static char
    readChar(byte[] buffer, int offset) {
        return (char) buffer[offset];
    }


    static short
    readShort(byte[] buffer, int offset) {
        int b1 = buffer[offset++];
        int b2 = buffer[offset];

        return (short) ((b2 << 8) | (b1 & 0xff));
    }


    static byte[] readFrom(File f){//TODO make thrwos FileNotFoundException
        byte[] data = null;
        FileInputStream fis = null;
        try {
            int length = (int) f.length();
            data = new byte[length];
            fis = new FileInputStream(f);
            fis.read(data);
        } catch (FileNotFoundException e) {
            if(e.getMessage().endsWith("being used by another process)")){
                return null;
            }
        }  catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return data;
    }

    public static int readInt(byte buf[], int offset) {
        return (buf[offset] & 0xFF) + ((buf[offset + 1] & 0xFF) << 8) + ((buf[offset + 2] & 0xFF) << 16) + ((buf[offset + 3]) << 24);
    }

    public static NumberDate readDateInt(byte[] data, int offset) {
        int di = readInt(data, offset);
        Calendar cal = Calendar.getInstance();
        cal.clear();
        int v = di % 10000;
        di = (di - v) / 10000;
        di += 1900;
        cal.set(Calendar.YEAR, di);
        di = (v - v % 100) / 100;
        cal.set(Calendar.MONTH, di - 1);
        di = v % 100;
        cal.set(Calendar.DAY_OF_MONTH, di);
        NumberDate nd = new NumberDate(cal.getTime(), di);
        byte[] buf = Arrays.copyOfRange(data, offset, offset + 4);
        nd.setBytes(buf);
        return nd;
    }
}