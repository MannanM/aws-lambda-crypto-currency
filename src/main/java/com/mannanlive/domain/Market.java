package com.mannanlive.domain;

public class Market {
    private String instrument;
    private String currency;

    public Market() {
    }

    public Market(String instrument, String currency) {
        this.instrument = instrument;
        this.currency = currency;
    }

    public String getInstrument() {
        return instrument;
    }

    public void setInstrument(String instrument) {
        this.instrument = instrument;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}