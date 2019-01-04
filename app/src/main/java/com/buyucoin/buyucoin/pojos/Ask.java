package com.buyucoin.buyucoin.pojos;

public class Ask {
    private Double ask_price;
    private String ask_value;
    private Double ask_volume;

    public Ask(Double ask_price, String ask_value, Double ask_volume) {
        this.ask_price = ask_price;
        this.ask_value = ask_value;
        this.ask_volume = ask_volume;
    }

    public Double getAsk_price() {
        return ask_price;
    }

    public String getAsk_value() {
        return ask_value;
    }

    public Double getAsk_volume() {
        return ask_volume;
    }
}
