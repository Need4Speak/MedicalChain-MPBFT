package com.pancake.entity.component;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pancake.entity.content.TxContent;

/**
 * Created by chao on 2017/11/11.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Transaction {
    private String txId;
    private String signature;  //客户端给该交易单的签名, 为string类型
    private String txType; //txType 用以标志当前 Transaction 所要进行的操作
    private String pubKey;  // 客户端该交易单签名的客户端的私钥所对应的公钥, 为string类型
    private TxContent content;  // 该交易单实际存储的内容
    private String timestamp;  // 交易单生成时的时间

    public Transaction() {
    }

    public Transaction(String txId, String signature, String txType, String pubKey, TxContent content, String timestamp) {
        this.txId = txId;
        this.signature = signature;
        this.txType = txType;
        this.pubKey = pubKey;
        this.content = content;
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        String rtn = null;
        try {
            rtn = (new ObjectMapper()).writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return rtn;
    }

    public String getTxId() {
        return txId;
    }

    public void setTxId(String txId) {
        this.txId = txId;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getTxType() {
        return txType;
    }

    public void setTxType(String txType) {
        this.txType = txType;
    }

    public String getPubKey() {
        return pubKey;
    }

    public void setPubKey(String pubKey) {
        this.pubKey = pubKey;
    }

    public TxContent getContent() {
        return content;
    }

    public void setContent(TxContent content) {
        this.content = content;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}


