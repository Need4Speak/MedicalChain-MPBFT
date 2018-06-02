package com.pancake.entity.message;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.pancake.entity.util.Const;

/**
 * Created by chao on 2017/12/18.
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "msgType",
        visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = BlockMessage.class, name = Const.BM),
        @JsonSubTypes.Type(value = TransactionMessage.class, name = Const.TXM)})
public class ClientMessage extends Message {
    public ClientMessage() {
    }

    public ClientMessage(String msgId, String msgType, String timestamp, String pubKey, String signature) {
        super(msgId, msgType, timestamp, pubKey, signature);
    }
}
