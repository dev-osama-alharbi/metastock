package client_1.inc;

import javafx.scene.control.DatePicker;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DateTime {
	
	
	public static class General{
		public static final String date = "yyyy-MM-dd";
		public static final DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern(date,Locale.ENGLISH);
		public static String localDateToString(LocalDate date) {
			if(date == null)return null;
			return date.format(formatterDate);
		}
		public static LocalDate stringToLocalDate(String date) {
			if(date == null)return null;
			return LocalDate.parse(date, formatterDate);
		}
		
		public static final String timeHMS = "HH:mm:ss";
		public static final DateTimeFormatter formatterTimeHMS = DateTimeFormatter.ofPattern(timeHMS,Locale.ENGLISH);
		public static String localTimeToString_HMS(LocalTime time) {
			if(time == null)return null;
			return time.format(formatterTimeHMS);
		}
		public static LocalTime stringToLocalTime_HMS(String time) {
			if(time == null)return null;
			return LocalTime.parse(time, formatterTimeHMS);
		}
		
		public static final String dateTimeHMS = date+" "+timeHMS;
		public static final DateTimeFormatter formatterDateTimeHMS = DateTimeFormatter.ofPattern(dateTimeHMS,Locale.ENGLISH);
		public static String localDateTimeToString_HMS(LocalDateTime dateTime) {
			if(dateTime == null)return null;
			return dateTime.format(formatterDateTimeHMS);
		}
		public static LocalDateTime stringToLocalDateTime_HMS(String dateTime) {
			if(dateTime == null)return null;
			return LocalDateTime.parse(dateTime, formatterDateTimeHMS);
		}
		
		public static final String dateTimeHMS_db = date+" "+timeHMS+".S";
		public static final DateTimeFormatter formatterDateTimeHMS_db = DateTimeFormatter.ofPattern(dateTimeHMS_db,Locale.ENGLISH);
		public static String localDateTimeToString_HMS_db(LocalDateTime dateTime_db) {
			if(dateTime_db == null)return null;
			return dateTime_db.format(formatterDateTimeHMS_db);
		}
		public static LocalDateTime stringToLocalDateTime_HMS_db(String dateTime_db) {
			if(dateTime_db == null)return null;
			return LocalDateTime.parse(dateTime_db, formatterDateTimeHMS_db);
		}
	}
	
	public static class View{
		public static final String date = "yyyy/MM/dd";
		public static final DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern(date,Locale.getDefault());
		public static String localDateToString(LocalDate date) {
			if(date == null)return null;
			return date.format(formatterDate);
		}
		public static LocalDate stringToLocalDate(String date) {
			if(date == null)return null;
			return LocalDate.parse(date, formatterDate);
		}
		
		public static final String timeHMS = "hh:mm:ss a";
		public static final DateTimeFormatter formatterTimeHMS = DateTimeFormatter.ofPattern(timeHMS,Locale.getDefault());
		public static String localTimeToString_HMS(LocalTime time) {
			if(time == null)return null;
			return time.format(formatterTimeHMS);
		}
		public static LocalTime stringToLocalTime_HMS(String time) {
			if(time == null)return null;
			return LocalTime.parse(time, formatterTimeHMS);
		}
		
		public static final String timeHMA = "hh:mm a";
		public static final DateTimeFormatter formatterTimeHMA = DateTimeFormatter.ofPattern(timeHMA,Locale.getDefault());
		public static String localTimeToString_HMA(LocalTime time) {
			if(time == null)return null;
			return time.format(formatterTimeHMA);
		}
		public static LocalTime stringToLocalTime_HMA(String time) {
			if(time == null)return null;
			return LocalTime.parse(time, formatterTimeHMA);
		}
		
		public static final String dateTimeHMS = date+" "+timeHMS;
		public static final DateTimeFormatter formatterDateTimeHMS = DateTimeFormatter.ofPattern(dateTimeHMS,Locale.getDefault());
		public static String localDateToStringTime_HMS(LocalDateTime dateTime) {
			if(dateTime == null)return null;
			return dateTime.format(formatterDateTimeHMS);
		}
		public static LocalDateTime stringToLocalDateTime_HMS(String dateTime) {
			if(dateTime == null)return null;
			return LocalDateTime.parse(dateTime, formatterDateTimeHMS);
		}
	}
	
	
	
	//SELECT * FROM StudentsAttendance WHERE StudentsAttendance_date between DATE('NOW', '-20 day') AND DATE('NOW', 'LOCALTIME')
//	private static String formatDate = "yyyy-MM-dd";
//	public static DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern(formatDate,Locale.ENGLISH);
//
//	private static String formatTime = "HH:mm";
//	public static DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern(formatTime,Locale.ENGLISH);
//
//	private static String formatTimeWithA = "a hh:mm";
//	public static DateTimeFormatter formatterTimeWithA = DateTimeFormatter.ofPattern(formatTimeWithA,Locale.ENGLISH);
//
//	private static String formatTimeSocend = "HH:mm:ss";
//	public static DateTimeFormatter formatterTimeSocend = DateTimeFormatter.ofPattern(formatTimeSocend,Locale.ENGLISH);
//
//	private static String formatDateTime = "yyyy-MM-dd HH:mm";
//	public static DateTimeFormatter formatterDateTime = DateTimeFormatter.ofPattern(formatDateTime,Locale.ENGLISH);
//
//	private static String formatDateTimeSocend = "yyyy-MM-dd HH:mm:ss";
//	public static DateTimeFormatter formatterDateTimeSocend = DateTimeFormatter.ofPattern(formatDateTimeSocend,Locale.ENGLISH);
//	
//	private static String formatDay = "a";
//	public static DateTimeFormatter formatterDay = DateTimeFormatter.ofPattern(formatDay,Locale.ENGLISH);
//	
//	private static String formatHour = "HH";
//	public static DateTimeFormatter formatterHour = DateTimeFormatter.ofPattern(formatHour,Locale.ENGLISH);
//	
//	private static String formatMinuet = "mm";
//	public static DateTimeFormatter formatterMinuet = DateTimeFormatter.ofPattern(formatMinuet,Locale.ENGLISH);
//	public static void date(JFXDatePicker date) {
//		StringConverter<LocalDate> converter = new StringConverter<LocalDate>() {
//            @Override
//            public String toString(LocalDate date) {
//                if (date != null) {
//                    return formatterDate.format(date);
//                } else {
//                    return "";
//                }
//            }
//            @Override
//            public LocalDate fromString(String string) {
//                if (string != null && !string.isEmpty()) {
//                    return LocalDate.parse(string, formatterDate);
//                } else {
//                    return null;
//                }
//            }
//        };   
//        date.setConverter(converter);
//        date.setPromptText(formatDate);
//	}
	public static void date(DatePicker date) {
		StringConverter<LocalDate> converter = new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return General.formatterDate.format(date);
                } else {
                    return "";
                }
            }
            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, General.formatterDate);
                } else {
                    return null;
                }
            }
        };   
        date.setConverter(converter);
        date.setPromptText(General.date);
	}
}
