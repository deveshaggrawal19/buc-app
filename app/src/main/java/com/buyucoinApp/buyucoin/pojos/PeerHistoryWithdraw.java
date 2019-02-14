package com.buyucoinApp.buyucoin.pojos;
import org.json.JSONArray;

/**
 * Awesome Pojo Generator
 * */
public class PeerHistoryWithdraw{
  private Integer amount;
  private Integer boost;
  private Integer currency;
  private Integer duration;
  private String end_timestamp;
  private Integer fee;
  private Integer filled;
  private Object filled_by;
  private Integer id;
  private String note;
  private JSONArray modes;
  private Integer min_amount;
  private Boolean pending;
  private Object tx_hash;
  private String timestamp;
  private Integer type;
  private Object matches;
  private Integer wallet_id;
  private Object rejected_match;
  private Integer user_id;
  private Integer matched;
  private Integer wfee_amount;
  private Integer status;

  public void setNote(String note){
   this.note=note;
  }
  public String getNote(){
   return note;
  }
  public void setAmount(Integer amount){
   this.amount=amount;
  }
  public Integer getAmount(){
   return amount;
  }
  public void setModes(JSONArray modes){
   this.modes=modes;
  }
  public Object getModes(){
   return modes;
  }
  public void setFee(Integer fee){
   this.fee=fee;
  }
  public Integer getFee(){
   return fee;
  }
  public void setMin_amount(Integer min_amount){
   this.min_amount=min_amount;
  }
  public Integer getMin_amount(){
   return min_amount;
  }
  public void setPending(Boolean pending){
   this.pending=pending;
  }
  public Boolean getPending(){
   return pending;
  }
  public void setFilled(Integer filled){
   this.filled=filled;
  }
  public Integer getFilled(){
   return filled;
  }
  public void setTx_hash(Object tx_hash){
   this.tx_hash=tx_hash;
  }
  public Object getTx_hash(){
   return tx_hash;
  }
  public void setType(Integer type){
   this.type=type;
  }
  public Integer getType(){
   return type;
  }
  public void setMatches(Object matches){
   this.matches=matches;
  }
  public Object getMatches(){
   return matches;
  }
  public void setDuration(Integer duration){
   this.duration=duration;
  }
  public Integer getDuration(){
   return duration;
  }
  public void setWallet_id(Integer wallet_id){
   this.wallet_id=wallet_id;
  }
  public Integer getWallet_id(){
   return wallet_id;
  }
  public void setRejected_match(Object rejected_match){
   this.rejected_match=rejected_match;
  }
  public Object getRejected_match(){
   return rejected_match;
  }
  public void setEnd_timestamp(String end_timestamp){
   this.end_timestamp=end_timestamp;
  }
  public String getEnd_timestamp(){
   return end_timestamp;
  }
  public void setUser_id(Integer user_id){
   this.user_id=user_id;
  }
  public Integer getUser_id(){
   return user_id;
  }
  public void setBoost(Integer boost){
   this.boost=boost;
  }
  public Integer getBoost(){
   return boost;
  }
  public void setCurrency(Integer currency){
   this.currency=currency;
  }
  public Integer getCurrency(){
   return currency;
  }
  public void setMatched(Integer matched){
   this.matched=matched;
  }
  public Integer getMatched(){
   return matched;
  }
  public void setWfee_amount(Integer wfee_amount){
   this.wfee_amount=wfee_amount;
  }
  public Integer getWfee_amount(){
   return wfee_amount;
  }
  public void setId(Integer id){
   this.id=id;
  }
  public Integer getId(){
   return id;
  }
  public void setFilled_by(Object filled_by){
   this.filled_by=filled_by;
  }
  public Object getFilled_by(){
   return filled_by;
  }
  public void setStatus(Integer status){
   this.status=status;
  }
  public Integer getStatus(){
   return status;
  }
  public void setTimestamp(String timestamp){
   this.timestamp=timestamp;
  }
  public String getTimestamp(){
   return timestamp;
  }


}