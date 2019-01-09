package com.buyucoin.buyucoin.pojos;

public class History {
    private double amount;
    private String curr;
    private String open_time;
    private String open;
    private String status;
    private String tx_hash;
    private String address;
    private double fee;
    private double filled;
    private double price;
    private String type;
    private double value;

    public History(double amount, String curr, String open_time, String open, String status, String tx_hash, String address, double fee, double filled, double price, String type, double value) {
        this.amount = amount;
        this.curr = curr;
        this.open_time = open_time;
        this.open = open;
        this.status = status;
        this.tx_hash = tx_hash;
        this.address = address;
        this.fee = fee;
        this.filled = filled;
        this.price = price;
        this.type = type;
        this.value = value;
    }

    public double getAmount() {
        return amount;
    }

    public String getCurr() {
        return curr;
    }

    public String getOpen_time() {
        return open_time;
    }

    public String getOpen() {
        return open;
    }

    public String getStatus() {
        return status;
    }

    public String getTx_hash() {
        return tx_hash;
    }

    public String getAddress() {
        return address;
    }

    public double getFee() {
        return fee;
    }

    public double getFilled() {
        return filled;
    }

    public double getPrice() {
        return price;
    }

    public String getType() {
        return type;
    }

    public double getValue() {
        return value;
    }
}
