package com.buyucoinApp.buyucoin.pojos;

import org.json.JSONArray;

public class ActiveP2pOrders {

    double amount;
    double boost;
    int currency;
    int duration;
    String end_timestamp;
    double fee;
    double filled;
    String filled_by;
    int id;
    double matched;
    JSONArray matched_by;
    Object matches;
    double min_amount;
    JSONArray modes;
    String upi_address;
    boolean pending;
    Object rejected_matches;
    int status;
    String timestamp;
    String tx_hash;
    int type;
    String note;
    int wallet_id;
    int user_id;
    int wfee_amount;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public JSONArray getModes() {
        return modes;
    }

    public void setModes(JSONArray modes) {
        this.modes = modes;
    }

    public String getNote() {
        return note;
    }



    public void setNote(String note) {
        this.note = note;
    }



    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getBoost() {
        return boost;
    }

    public void setBoost(double boost) {
        this.boost = boost;
    }

    public int getCurrency() {
        return currency;
    }

    public void setCurrency(int currency) {
        this.currency = currency;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getEnd_timestamp() {
        return end_timestamp;
    }

    public void setEnd_timestamp(String end_timestamp) {
        this.end_timestamp = end_timestamp;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public double getFilled() {
        return filled;
    }

    public void setFilled(double filled) {
        this.filled = filled;
    }

    public String getFilled_by() {
        return filled_by;
    }

    public void setFilled_by(String filled_by) {
        this.filled_by = filled_by;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getMatched() {
        return matched;
    }

    public void setMatched(double matched) {
        this.matched = matched;
    }

    public JSONArray getMatched_by() {
        return matched_by;
    }

    public void setMatched_by(JSONArray matched_by) {
        this.matched_by = matched_by;
    }

    public Object getMatches() {
        return matches;
    }

    public void setMatches(Object matches) {
        this.matches = matches;
    }

    public double getMin_amount() {
        return min_amount;
    }

    public void setMin_amount(double min_amount) {
        this.min_amount = min_amount;
    }


    public String getUpi_address() {
        return upi_address;
    }

    public void setUpi_address(String upi_address) {
        this.upi_address = upi_address;
    }

    public boolean isPending() {
        return pending;
    }

    public void setPending(boolean pending) {
        this.pending = pending;
    }

    public Object getRejected_matches() {
        return rejected_matches;
    }

    public void setRejected_matches(Object rejected_matches) {
        this.rejected_matches = rejected_matches;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getTx_hash() {
        return tx_hash;
    }

    public void setTx_hash(String tx_hash) {
        this.tx_hash = tx_hash;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }





    public int getWallet_id() {
        return wallet_id;
    }

    public void setWallet_id(int wallet_id) {
        this.wallet_id = wallet_id;
    }

    public int getWfee_amount() {
        return wfee_amount;
    }

    public void setWfee_amount(int wfee_amount) {
        this.wfee_amount = wfee_amount;
    }
}
