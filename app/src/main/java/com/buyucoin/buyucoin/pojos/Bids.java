package com.buyucoin.buyucoin.pojos;

public class Bids {
    private Double bid_price;
    private String bid_value;
    private Double bid_volume;

    public Bids(Double bid_price, String bid_value, Double bid_volume) {
        this.bid_price = bid_price;
        this.bid_value = bid_value;
        this.bid_volume = bid_volume;
    }

    public Double getBid_price() {
        return bid_price;
    }

    public String getBid_value() {
        return bid_value;
    }

    public Double getBid_volume() {
        return bid_volume;
    }
}
