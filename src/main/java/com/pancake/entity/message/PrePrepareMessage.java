package com.pancake.entity.message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pancake.entity.util.Const;

/**
 * Created by chao on 2017/11/24.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PrePrepareMessage extends Message{
    private String viewId;  // 当前视图编号
    private String seqNum;  // sequence number， 该请求是在视图v中被赋予了序号n
    private ClientMessage clientMsg;

    public PrePrepareMessage() {
    }

    public PrePrepareMessage(String viewId, String seqNum, ClientMessage clientMsg) {
        this.viewId = viewId;
        this.seqNum = seqNum;
        this.clientMsg = clientMsg;
    }

    public PrePrepareMessage(String msgId, String timestamp, String pubKey, String signature, String viewId,
                             String seqNum, ClientMessage clientMsg) {
        super(msgId, Const.PPM, timestamp, pubKey, signature);
        this.viewId = viewId;
        this.seqNum = seqNum;
        this.clientMsg = clientMsg;
    }

    public PrePrepareMessage(String msgId, String msgType, String timestamp, String pubKey, String signature,
                             String viewId, String seqNum, ClientMessage clientMsg) {
        super(msgId, msgType, timestamp, pubKey, signature);
        this.viewId = viewId;
        this.seqNum = seqNum;
        this.clientMsg = clientMsg;
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

    public String getViewId() {
        return viewId;
    }

    public void setViewId(String viewId) {
        this.viewId = viewId;
    }

    public String getSeqNum() {
        return seqNum;
    }

    public void setSeqNum(String seqNum) {
        this.seqNum = seqNum;
    }

    public ClientMessage getClientMsg() {
        return clientMsg;
    }

    public void setClientMsg(ClientMessage clientMsg) {
        this.clientMsg = clientMsg;
    }
}
