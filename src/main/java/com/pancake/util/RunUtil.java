package com.pancake.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pancake.entity.component.Block;
import com.pancake.entity.component.Transaction;
import com.pancake.entity.util.Const;
import com.pancake.entity.util.NetAddress;
import com.pancake.service.component.impl.BlockService;
import com.pancake.service.component.impl.TransactionService;
import com.pancake.service.message.impl.BlockMessageService;
import com.pancake.socket.Blocker;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * 使用 junit 以单独运行一个方法
 * Created by chao on 2017/12/11.
 */
public class RunUtil {
    private final static ObjectMapper objMapper = new ObjectMapper();
    private BlockService blockService = BlockService.getInstance();
    private BlockMessageService blockMsgServ = BlockMessageService.getInstance();
    private Blocker blocker = new Blocker();

    @Test
    public void sendGenesisBlock() {
        String txId = "-1";
        List<String> txIdList = new ArrayList<String>();
        txIdList.add(txId);
        Block block = blockService.genBlock(Const.GENESIS_BLOCK_ID, txIdList);
        System.out.println("block: " + block);
        blocker.sendBlock(block, NetUtil.getPrimaryNode());
    }
    /**
     * 统计各个集合中记录的数量
     */
    @Test
    public void countRecordQuantity() {
        String ip = "127.0.0.1";
        String url;
        String ppmCollection;
        String pmCollection;
        String pdmCollection;
        String cmtmCollection;
        String cmtdmCollection;
        String blockChainCollection;
        String txCollection;

        // 1. 检索 Validator 上的所有集合
        for (int port = 8000; port < 8004; port++) {
            url = ip + ":" + port;
            ppmCollection = url + "." + Const.PPM;
            pmCollection = url + "." + Const.PM;
            pdmCollection = url + "." + Const.PDM;
            cmtmCollection = url + "." + Const.CMTM;
            cmtdmCollection = url + "." + Const.CMTDM;
            blockChainCollection = url + "." + Const.BLOCK_CHAIN;
            txCollection = url + "." + Const.TX;
            String lbiCollection = url + "." + Const.LAST_BLOCK_ID;
            String txIdCollectorColl = "TxIdCollector" + ip + ":" + (port + 1000) + ".TxIds";

            long ppmCount = MongoUtil.countRecords(ppmCollection);
            long pmCount = MongoUtil.countRecords(pmCollection);
            long pdmCount = MongoUtil.countRecords(pdmCollection);
            long cmtmCount = MongoUtil.countRecords(cmtmCollection);
            long cmtdmCount = MongoUtil.countRecords(cmtdmCollection);
            long blockChainCount = MongoUtil.countRecords(blockChainCollection);
            long txCount = MongoUtil.countRecords(txCollection);
            int blockIdCount = MongoUtil.countValuesByKey("blockId", blockChainCollection);
            long txIdsCount = MongoUtil.countRecords(txIdCollectorColl);
            String lastBlockId = blockService.getLastBlockId(lbiCollection);

            System.out.println("主机 [ " + url + " ] < ppmCount: " + ppmCount
                    + ", pmCount: " + pmCount
                    + ", pdmCount: " + pdmCount
                    + ", cmtmCount: " + cmtmCount
                    + ", cmtdmCount: " + cmtdmCount
                    + ", blockChainCount: " + blockChainCount
                    + ", txCount: " + txCount
                    + ", blockIdCount: " + blockIdCount
                    + ", txIdsCount: " + txIdsCount
                    + ", lastBlockId: " + lastBlockId);
        }

        System.out.println("=================================================================================");

        // 2. 检索 blocker 上的所有集合
        String lbiCollection;
        String txIdCollection;
        String txIdMsgCollection;
        String blockMsgCollection;
        List<NetAddress> blockerList = JsonUtil.getBlockerAddressList(Const.BlockChainNodesFile);
        for(NetAddress blockerAddr : blockerList) {
            blockChainCollection = blockerAddr + "." + Const.BLOCK_CHAIN;
            lbiCollection = blockerAddr + "." + Const.LAST_BLOCK_ID;
            txIdCollection = blockerAddr + "." + Const.TX_ID;
            txIdMsgCollection = blockerAddr + "." + Const.TIM;
            blockMsgCollection = blockerAddr + "." + Const.BM;

            long blockChainCount = MongoUtil.countRecords(blockChainCollection);
            long blockMsgCount = MongoUtil.countRecords(blockMsgCollection);
            String lastBlockId = blockService.getLastBlockId(lbiCollection);
            long txIdsCount = MongoUtil.countRecords(txIdCollection);
            long txIdMsgCount = MongoUtil.countRecords(txIdMsgCollection);
            System.out.println("主机 [ " + blockerAddr + " ] <  blockChainCount: " + blockChainCount
                    + ", blockMsgCount: " + blockMsgCount
                    + ", txIdsCount: " + txIdsCount
                    + ", txIdMsgCount: " + txIdMsgCount
                    + ", lastBlockId: " + lastBlockId);
        }
    }

    /**
     * 清空所有集合
     *
     * @throws Exception
     */
    @Test
    public void dropAllCollections() throws Exception {
        MongoUtil.dropAllCollections();
    }

    /**
     * 向队列中添加 tx
     */
    @SuppressWarnings("Duplicates")
    @Test
    public void addTxToQueue() {
        RabbitmqUtil rmq = new RabbitmqUtil(Const.TX_QUEUE);
        List<Transaction> txList = new ArrayList<Transaction>();
        try {
            for (int i = 0; i < 100; i++) {
                Transaction tx = TransactionService.genTx("string" + i, "测试" + i);
//                if(i<4) {
//                    txList.add(tx);
//                }
                rmq.push(tx.toString());
            }
//            rmq.push(objectMapper.writeValueAsString(txList));
//            logger.info(objectMapper.writeValueAsString(txList).substring(0,1));
        } catch (Exception e) {
            e.printStackTrace();
        }
//        List<String> msgList = rmq.pull(100000, 4.0/1024.0);
//        for(String msg : msgList) {
//            System.out.println(msg);
//        }
    }

    @SuppressWarnings("Duplicates")
    @Test
    public void addVerifiedTxToQueue() {
        RabbitmqUtil rmq = new RabbitmqUtil(Const.VERIFIED_TX_QUEUE);
        try {
            for (int i = 0; i < 50; i++) {
                Transaction tx = TransactionService.genTx("string" + i, "测试" + i);
                rmq.push(tx.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("Duplicates")
    @Test
    public void addTxIdToQueue() {
        RabbitmqUtil rmq = new RabbitmqUtil(Const.TX_ID_QUEUE);
        try {
            for (int i = 0; i < 1000; i++) {
                Transaction tx = TransactionService.genTx("string" + i, "测试" + i);
                rmq.push(tx.getTxId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void countBlocks() {
        String realIp = NetUtil.getRealIp();
        String url;
        String blockChainCollection;
        for (int port = 8000; port < 8004; port++) {
            url = realIp + ":" + port;
            blockChainCollection = url + "." + Const.BLOCK_CHAIN;
            MongoUtil.findValuesByKey("blockId", blockChainCollection);
        }
    }

    @Test
    public void showBlockChain() {
        String realIp = NetUtil.getRealIp();
        String url = realIp + ":" + 8000;
        String blockChainCollection = url + "." + Const.BLOCK_CHAIN;
        List<Block> blockList = blockService.getAllBlocks(blockChainCollection);
        int blockNum = 0;
        for (Block block : blockList) {
            System.out.println("Block" + blockNum + ": " + block.toString());
            blockNum++;
        }
    }

}
