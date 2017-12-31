package com.mannanlive.domain;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MarketValueTweet {
    private String status;

    public MarketValueTweet(MarketValue marketValue) {
        this.status = convert(marketValue);
    }

    private String convert(MarketValue marketValue) {
        return String.format(Locale.US, "The current value of %s at %s is %s %s.\n#bitcoin #australia",
                marketValue.getInstrument(),
                getAestTime(marketValue.getTimestamp()),
                getMoneyAsString(marketValue), //marketValue.getLastPrice(),
                marketValue.getCurrency());
    }

    private String getMoneyAsString(MarketValue marketValue) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        return formatter.format(marketValue.getLastPrice());
    }

    private String getAestTime(long timestamp) {
        Calendar cal = Calendar.getInstance(); // creates calendar
        cal.setTime(new Date(timestamp * 1000L)); // sets calendar time/date and add milliseconds
        cal.add(Calendar.HOUR_OF_DAY, 10); // adds ten hours
        cal.getTime(); // returns new date object, one hour in the future
        String time = new SimpleDateFormat("HH:mm:ss").format(cal.getTime());
        String date = new SimpleDateFormat("dd/MM/yyyy").format(cal.getTime());
        return String.format("%s on %s (AEST)", time, date);
    }

    public String getStatus() {
        return status;
    }
}
