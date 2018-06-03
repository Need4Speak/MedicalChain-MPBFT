package com.pancake.socket;

import com.pancake.entity.component.Transaction;
import com.pancake.entity.content.TxString;
import com.pancake.entity.enumeration.TxType;
import com.pancake.service.component.TransactionService;
import com.pancake.service.message.impl.TransactionMessageService;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by chao on 2017/11/25.
 */
public class Client {

    public static void main(String[] args) {
        TransactionMessageService txMsgService = TransactionMessageService.getInstance();
        TransactionService txService = TransactionService.getInstance();
        String serverName = "127.0.0.1";
        int port = 8000;
        try
        {
            System.out.println("连接到主机：" + serverName + " ，端口号：" + port);
            Socket client = new Socket(serverName, port);
            System.out.println("远程主机地址：" + client.getRemoteSocketAddress());

            OutputStream outToServer = client.getOutputStream();
            DataOutputStream out = new DataOutputStream(outToServer);
            List<Transaction> txList = new ArrayList<Transaction>();
            txList.add(txService.genTx(TxType.INSERT.getName(), new TxString("测试")));
            String txMsg = txMsgService.genInstance(txList).toString();
            out.writeUTF(txMsg);

            InputStream inFromServer = client.getInputStream();
            DataInputStream in = new DataInputStream(inFromServer);
            System.out.println("服务器响应： " + in.readUTF());
            client.close();
        }catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
