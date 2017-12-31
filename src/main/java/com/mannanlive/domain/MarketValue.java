package com.mannanlive.domain;

public class MarketValue extends Market {
    private float bestBid;
    private float bestAsk;
    private float lastPrice;
    private long timestamp;
    private float volume24h;

    public float getVolume24h() {
        return volume24h;
    }

    public void setVolume24h(float volume24h) {
        this.volume24h = volume24h;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public float getLastPrice() {
        return lastPrice;
    }

    public float getBestAsk() {
        return bestAsk;
    }

    public void setBestAsk(float bestAsk) {
        this.bestAsk = bestAsk;
    }

    public float getBestBid() {
        return bestBid;
    }

    public void setBestBid(float bestBid) {
        this.bestBid = bestBid;
    }

    public void setLastPrice(float lastPrice) {
        this.lastPrice = lastPrice;
    }
}