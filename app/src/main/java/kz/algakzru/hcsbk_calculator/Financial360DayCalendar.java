package kz.algakzru.hcsbk_calculator;

import org.joda.time.LocalDate;

/**
 * Created by 816856 on 2/27/2018.
 * https://stackoverflow.com/questions/28277833/how-to-create-a-bank-calendar-with-30-days-each-month
 * https://github.com/OpenGamma/OG-Commons/blob/master/modules/basics/src/main/java/com/opengamma/basics/date/StandardDayCounts.java#L266-L284
 */

public class Financial360DayCalendar {
    private final int year;
    private final int month;
    private final int dayOfMonth;

    private Financial360DayCalendar(int year, int month, int dayOfMonth) {
        this.year = year;
        this.month = month;
        this.dayOfMonth = dayOfMonth;
    }

    public static Financial360DayCalendar of(LocalDate date) {
        int year = date.getYear();
        int month = date.getMonthOfYear();
        int dayOfMonth = date.getDayOfMonth();

        if (dayOfMonth == 31) {
            dayOfMonth = 30;
        }

        return new Financial360DayCalendar(year, month, dayOfMonth);
    }

    public int durationInDaysUntil(Financial360DayCalendar other) {
        // check also if other is not before this instance
        // ...
        // special algorithm (handling all intervening months as 30 days long)
        int interestDays = 30 - this.dayOfMonth; // current day excluded
        int monthDelta = other.getMonthCount() - this.getMonthCount();
        if (monthDelta > 0) {
            monthDelta--;
        }
        interestDays += (30 * monthDelta + other.dayOfMonth);
        return interestDays;
    }

    public double getYearFraction(Financial360DayCalendar other) {
        return durationInDaysUntil(other) / 360.0;
    }

    private int getMonthCount() {
        return this.year * 12 + this.month;
    }
}
