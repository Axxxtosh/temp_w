package com.example.admin.worldvisioncable.Models;

/**
 * Created by Ashlesh on 06-12-2017.
 */

public class PaymentHistoryModel {
    String Packageid;
    String PackageName;
    String TransactionId;
    String mmp_txn;
    String Type;
    String Amount;
    String Pain_on;
    String desc;

    public String getPackageid() {
        return Packageid;
    }

    public void setPackageid(String packageid) {
        Packageid = packageid;
    }

    public String getPackageName() {
        return PackageName;
    }

    public void setPackageName(String packageName) {
        PackageName = packageName;
    }

    public String getTransactionId() {
        return TransactionId;
    }

    public void setTransactionId(String transactionId) {
        TransactionId = transactionId;
    }

    public String getMmp_txn() {
        return mmp_txn;
    }

    public void setMmp_txn(String mmp_txn) {
        this.mmp_txn = mmp_txn;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getPain_on() {
        return Pain_on;
    }

    public void setPain_on(String pain_on) {
        Pain_on = pain_on;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
