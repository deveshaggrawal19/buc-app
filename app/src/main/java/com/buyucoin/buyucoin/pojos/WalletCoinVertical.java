package com.buyucoin.buyucoin.pojos;

public class WalletCoinVertical {
    private String coinname;
    private String address;
    private String amount;
    private String pending;
    private String rate;
    private String diff;

    public WalletCoinVertical() {
    }

    public void setCoinname(String coinname) {
        this.coinname = coinname;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setPending(String pending) {
        this.pending = pending;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public void setDiff(String diff) {
        this.diff = diff;
    }

    public String getCoinname() {
        return coinname;
    }

    public String getAddress() {
        return address;
    }

    public String getAmount() {
        return amount;
    }

    public String getPending() {
        return pending;
    }

    public String getRate() {
        return rate;
    }

    public String getDiff() {
        return diff;
    }

    public WalletCoinVertical(String coinname, String address, String amount, String pending, String rate, String diff) {
        this.coinname = coinname;
        this.address = address;
        this.amount = amount;
        this.pending = pending;
        this.rate = rate;
        this.diff = diff;
    }
}
