package com.pancake.socket;

import com.pancake.entity.util.Const;
import com.pancake.entity.util.NetAddress;
import com.pancake.util.JsonUtil;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;



/**
 * 用于编写客户端向区块链发送请求的各种命令
 * Created by chao on 2017/11/9.
 */
public class ClientFrontEnd {

    /**
     * 根据 BlockChainConfigFile 连接验证器
     */
    public static void connValidators() {

        ThreadPoolExecutor es = (ThreadPoolExecutor) Executors.
                newCachedThreadPool();
        List<NetAddress> list = JsonUtil.getValidatorAddressList(Const.BlockChainConfigFile);
        NetAddress va = null;
        for(int index=0; index < list.size(); index++) {
            va = list.get(index);
            es.execute(new Task("task-"+index, va.getIp(), va.getPort()));
        }

        es.shutdown();
    }

    public static void connValidator(String ip, String port) {
        Task task = new Task("启动一个client", ip, Integer.parseInt(port));
        new Thread(task).start();
    }
    public static void main(String [] args) {
        connValidator("127.0.0.1", "8000");
//        connValidators();
    }
}
