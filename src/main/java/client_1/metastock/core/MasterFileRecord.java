package client_1.metastock.core;

import client_1.metastock.model.NumberDate;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Date;

public final class MasterFileRecord {
    private static final String FPREFIX = "F";
    private static final String FSUFFIX_DAT = ".DAT";
    private static final String FSUFFIX_MWD = ".MWD";

    int fileNum;
    private final char fileType;
    final int recordLength;
    final int recordCount;
    private final String issueName;
    private final boolean v28;
    private final NumberDate firstDate;
    private final NumberDate lastDate;
    private final char timeFrame;
    private final int idaTime;
    private final String symbol;
    private final boolean autorun;

    public MasterFileRecord(byte[] data, int recNum) {
        int recordBase = (recNum + 1) * MasterFileDescriptor.MASTER_RECORD_LENGTH;

        fileNum = Parser.readByte(data, recordBase + MasterFileDescriptor.FILE_NUM);
//        if(isDaily){
//            fileNum = (recNum + 1);
//        }
        fileNum = (recNum + 1);
//        fileNum = fn;
        if (fileNum < 0) fileNum = Byte.MAX_VALUE - fileNum;
        fileType = (char) Parser.readShort(data, recordBase + MasterFileDescriptor.FILE_TYPE_0);
        recordLength = Parser.readByte(data, recordBase + MasterFileDescriptor.RECORD_LENGTH);
        recordCount = Parser.readByte(data, recordBase + MasterFileDescriptor.RECORD_COUNT);




        String tmp = "";

        ByteArrayOutputStream bb = new ByteArrayOutputStream();
        bb.write(data, recordBase + MasterFileDescriptor.ISSUE_NAME_0, MasterFileDescriptor.ISSUE_NAME_LEN);

        byte[] tmpbyt = bb.toByteArray();//getBytes();
        String c = "Windows-1256";
        tmp = new String(tmpbyt,Charset.forName(c));
//        System.out.println("tmp = "+tmp);

        issueName = tmp;

        v28 = Parser.readByte(data, recordBase + MasterFileDescriptor.CT_V2_8_FLAG) == (byte) 'Y';

        firstDate = Parser.readDate(data, recordBase + MasterFileDescriptor.FIRST_DATE_0);
        lastDate = Parser.readDate(data, recordBase + MasterFileDescriptor.LAST_DATE_0);

        timeFrame = Parser.readChar(data, recordBase + MasterFileDescriptor.TIME_FRAME);
        idaTime = Parser.readShort(data, recordBase + MasterFileDescriptor.IDA_TIME_0);

        symbol = new String(data, recordBase + MasterFileDescriptor.SYMBOL_0, MasterFileDescriptor.SYMBOL_LEN).trim();
        autorun = Parser.readByte(data, recordBase + MasterFileDescriptor.CT_V2_8_FLAG) == (byte) '*';
        try {
            bb.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (fileNum == 0) throw new RuntimeException("Invalid Recored::" + recNum + "  " + this);
    }

    public String toString() {
        char[] ft = {fileType};
        char[] tf = {timeFrame};
        return "fileNum:" + fileNum + "\n" + "fileType:     " + ft + "\n" + "recordLength: " + Integer.toString(recordLength) + "\n" + "recordCount:  " + Integer.toString(recordCount) + "\n" + "issueName:    " + issueName + "\n" + "v28:          " + (v28 ? "true" : "false") + "\n" + "firstDate:    " + (firstDate == null ? "??/??/????" : firstDate.toString()) + "\n" + "lastDate:     " + (lastDate == null ? "??/??/????" : lastDate.toString()) + "\n" + "timeFrame:    " + new String(tf) + "\n" + "idaTime:      " + Integer.toString(idaTime) + "\n" + "symbol:       " + symbol + "\n" + "autorun:      " + (autorun ? "true" : "false") + "\n";
    }

    public String getFileName() {
        return FPREFIX + String.valueOf(fileNum) + (fileNum > 256 ? FSUFFIX_MWD : FSUFFIX_DAT);
    }

    public int getFileNum() {
        return fileNum;
    }

    public char getFileType() {
        return fileType;
    }

    public int getRecordLength() {
        return recordLength;
    }

    public int getRecordCount() {
        return recordCount;
    }

    public String getIssueName() {
        return issueName;
    }

    public boolean isV28() {
        return v28;
    }

    public Date getFirstDate() {
        return firstDate;
    }

    public Date getLastDate() {
        return lastDate;
    }

    public char getTimeFrame() {
        return timeFrame;
    }

    public int getIdaTime() {
        return idaTime;
    }

    public String getSymbol() {
        return symbol;
    }

    public boolean isAutorun() {
        return autorun;
    }

}

