/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package systems.cauldron.http.v1.http.header;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;

/**
 * @author Administrator
 */
public class HttpDate {
    //"EEE, dd-MMM-yyyy HH:mm:ss zzz"

    private enum TimeZone {

        GMT("GMT\r\n"),
        EST("EST\r\n"),
        CST("CST\r\n"),
        MST("MST\r\n"),
        PST("PST\r\n");
        private final byte[] byteValue;

        private TimeZone(String s) {
            this.byteValue = s.getBytes(StandardCharsets.US_ASCII);
        }

        public byte[] getBytes() {
            return byteValue;
        }
    }

    private enum Weekday {

        Sunday("Sun, "),
        Monday("Mon, "),
        Tuesday("Tue, "),
        Wednesday("Wed, "),
        Thursday("Thu, "),
        Friday("Fri, "),
        Saturday("Sat, ");
        private final byte[] byteValue;

        private Weekday(String s) {
            this.byteValue = s.getBytes(StandardCharsets.US_ASCII);
        }

        public byte[] getBytes() {
            return byteValue;
        }
    }

    private enum Month {

        January("Jan-"),
        February("Feb-"),
        March("Mar-"),
        April("Apr-"),
        May("May-"),
        June("Jun-"),
        July("Jul-"),
        August("Aug-"),
        September("Sep-"),
        October("Oct-"),
        November("Nov-"),
        December("Dec-");
        private final byte[] byteValue;

        private Month(String s) {
            this.byteValue = s.getBytes(StandardCharsets.US_ASCII);
        }

        public byte[] getBytes() {
            return byteValue;
        }
    }

    private ByteBuffer buffer;
    private Calendar calendar;

    public void putWeekday() {
        switch (calendar.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.SUNDAY:
                buffer.put(Weekday.Sunday.getBytes());
                break;
            case Calendar.MONDAY:
                buffer.put(Weekday.Monday.getBytes());
                break;
            case Calendar.TUESDAY:
                buffer.put(Weekday.Tuesday.getBytes());
                break;
            case Calendar.WEDNESDAY:
                buffer.put(Weekday.Wednesday.getBytes());
                break;
            case Calendar.THURSDAY:
                buffer.put(Weekday.Thursday.getBytes());
                break;
            case Calendar.FRIDAY:
                buffer.put(Weekday.Friday.getBytes());
                break;
            case Calendar.SATURDAY:
                buffer.put(Weekday.Saturday.getBytes());
                break;
            default:
                throw new RuntimeException();
        }
    }

    public void putDayOfMonth() {
        int n = calendar.get(Calendar.DAY_OF_MONTH);
        if (n < 10) {
            buffer.put((byte) 48).put((byte) (48 + n));
        } else {
            int x = n % 10;
            n /= 10;
            buffer.put((byte) (48 + n)).put((byte) (48 + x));
        }
    }

    public void putMonth() {
        switch (calendar.get(Calendar.MONTH)) {
            case Calendar.JANUARY:
                buffer.put(Month.January.getBytes());
                break;
            case Calendar.FEBRUARY:
                buffer.put(Month.February.getBytes());
                break;
            case Calendar.MARCH:
                buffer.put(Month.March.getBytes());
                break;
            case Calendar.APRIL:
                buffer.put(Month.April.getBytes());
                break;
            case Calendar.MAY:
                buffer.put(Month.May.getBytes());
                break;
            case Calendar.JUNE:
                buffer.put(Month.June.getBytes());
                break;
            case Calendar.JULY:
                buffer.put(Month.July.getBytes());
                break;
            case Calendar.AUGUST:
                buffer.put(Month.August.getBytes());
                break;
            case Calendar.SEPTEMBER:
                buffer.put(Month.September.getBytes());
                break;
            case Calendar.OCTOBER:
                buffer.put(Month.October.getBytes());
                break;
            case Calendar.NOVEMBER:
                buffer.put(Month.November.getBytes());
                break;
            case Calendar.DECEMBER:
                buffer.put(Month.December.getBytes());
                break;
            default:
                throw new RuntimeException();
        }
    }

    public void putYear() {
        //I reckon think this won't be used past the year 9999
        int a = calendar.get(Calendar.YEAR);
        int d = a % 10 + 48;
        a /= 10;
        int c = a % 10 + 48;
        a /= 10;
        int b = a % 10 + 48;
        a /= 10;
        buffer.put((byte) (a + 48))
                .put((byte) (b))
                .put((byte) (c))
                .put((byte) (d));
    }
}
